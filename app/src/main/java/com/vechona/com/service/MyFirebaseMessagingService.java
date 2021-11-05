package com.vechona.com.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.UserResponse;
import com.vechona.com.ui.activity.OrderDetailActivity;
import com.vechona.com.ui.model.UserRegisterToken;
import com.vechona.com.ui.utils.BundleParams;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public static void sendRegistrationToServer(final String token, final Context context) {
        ApiService apiService = RemoteServiceHelper
                .createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(context));
        apiService.
                registerToken(new UserRegisterToken(PreferenceDataHelper
                        .getInstance(context)
                        .getUser()
                        .getUserID(), token))
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                        UserResponse.Data data = response.body().getData();
//                        if (response.isSuccessful() && data != null) {
//                            ViewUtils.showToast(context, data.getResult());
//                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        // ViewUtils.showToast(getApplicationContext(), getString(R.string.error_connection_failed));
                    }
                });
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        PreferenceDataHelper preferenceDataHelper = PreferenceDataHelper.getInstance(getApplicationContext());
        preferenceDataHelper.saveFCMToken(token);
        if (!TextUtils.isEmpty(preferenceDataHelper.getAccessToken())) {
            sendRegistrationToServer(token, getApplicationContext());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String orderId = remoteMessage.getData().values().toArray()[0].toString();
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), orderId);
        }
    }

    private void sendNotification(String body, String title, String orderId) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(BundleParams.USER_ORDER_ID, orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("FCM Notification channel",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}