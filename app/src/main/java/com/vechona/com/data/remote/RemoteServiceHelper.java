package com.vechona.com.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.vechona.app.BuildConfig;
import com.vechona.com.app.AppConstants;
import com.vechona.com.app.MyApplication;
import com.vechona.com.data.local.PreferenceDataHelper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created  on 17/06/17.
 */

public class RemoteServiceHelper {

    private static final String AUTHORIZATION = "Authorization";
    private static Retrofit instance;

    private RemoteServiceHelper() {
    }

    /**
     * Creates new unique retrofit instance if not created already
     *
     * @param context
     * @return
     */
    public static synchronized Retrofit getRetrofitInstance(final Context context) {
        if (instance == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            if (BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
            httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
            httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
//            httpClientBuilder.authenticator(new TokenAuthenticator((MyApplication) context.getApplicationContext()));
            httpClientBuilder.addInterceptor(new HeaderInterceptor(
                    new BaseHeaders(PreferenceDataHelper.getInstance(context)), PreferenceDataHelper.getInstance(context)));

            httpClientBuilder.addInterceptor(logging);
            /*if (BuildConfig.DEBUG) {
                httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
            }
*/
            instance = new Retrofit
                    .Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        }
        return instance;
    }

    /**
     * Creates service to make network requests to endpoints
     *
     * @param apiService
     * @param retrofit
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> apiService, Retrofit retrofit) {
        return retrofit.create(apiService);
    }

    public static synchronized void destroyInstance() {
        instance = null;
    }

    private static Response makeTokenRefreshCall(Request request, Interceptor.Chain chain, PreferenceDataHelper preferenceDataHelper) throws IOException {
        String newToken = getToken();
        preferenceDataHelper.saveAccessToken(newToken);
        request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        if (newToken != null) {
            requestBuilder.addHeader(AUTHORIZATION, newToken);
        }
        Request newRequest = requestBuilder.build();
        Response response = chain.proceed(newRequest);
        return response;
    }

    public static String getToken() {
        final StringBuilder token = new StringBuilder();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                token.append(task.getResult().getToken());
                                countDownLatch.countDown();
                            }
                        }
                    });
        }
        try {
            countDownLatch.await(5L, TimeUnit.SECONDS);
            return token.toString();
        } catch (InterruptedException ie) {
            return null;
        }
    }

    /**
     * Stores common Headers needed in each request
     */
    private static class BaseHeaders {
        private PreferenceDataHelper preferenceDataHelper;

        public BaseHeaders(PreferenceDataHelper preferenceDataHelper) {
            this.preferenceDataHelper = preferenceDataHelper;
        }

        public String getAccessToken() {
            return preferenceDataHelper.getAccessToken();
        }
    }

    public static class HeaderInterceptor implements okhttp3.Interceptor {
        PreferenceDataHelper preferenceDataHelper;
        BaseHeaders baseHeaders;

        public HeaderInterceptor(BaseHeaders baseHeaders, PreferenceDataHelper instance) {
            preferenceDataHelper = instance;
            this.baseHeaders = baseHeaders;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();

            if (baseHeaders.getAccessToken() != null) {
                requestBuilder.addHeader(AUTHORIZATION, baseHeaders.getAccessToken());
            }
            Request request = requestBuilder.build();
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                Response newResponse = makeTokenRefreshCall(request, chain, preferenceDataHelper);
                return newResponse;
            }
            return response;
        }
    }

    private static class TokenAuthenticator implements Authenticator {
        private MyApplication applicationContext;

        public TokenAuthenticator(MyApplication applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Nullable
        @Override
        public Request authenticate(Route route, Response response) {
            applicationContext.logoutUser();
            return null;
        }
    }
}
