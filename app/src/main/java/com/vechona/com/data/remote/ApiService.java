package com.vechona.com.data.remote;

import com.vechona.com.data.remote.apiResponse.AuthResponse;
import com.vechona.com.data.remote.apiResponse.BankDetailsResponse;
import com.vechona.com.data.remote.apiResponse.CartAddUpdateResponse;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponse;
import com.vechona.com.data.remote.apiResponse.CollectionResponse;
import com.vechona.com.data.remote.apiResponse.CreateOrderResponse;
import com.vechona.com.data.remote.apiResponse.DeliveryPincodeResponse;
import com.vechona.com.data.remote.apiResponse.GetCartResponse;
import com.vechona.com.data.remote.apiResponse.HomeSliderResponse;
import com.vechona.com.data.remote.apiResponse.OrderDetailsResponse;
import com.vechona.com.data.remote.apiResponse.ReviewResponse;
import com.vechona.com.data.remote.apiResponse.SettingResponse;
import com.vechona.com.data.remote.apiResponse.TopCategory;
import com.vechona.com.data.remote.apiResponse.TopCategoryResponse;
import com.vechona.com.data.remote.apiResponse.UpdateBankDetailsResponse;
import com.vechona.com.data.remote.apiResponse.UserOrderDetailsResponse;
import com.vechona.com.data.remote.apiResponse.UserOrderListResponse;
import com.vechona.com.data.remote.apiResponse.UserResponse;
import com.vechona.com.data.remote.apiResponse.UserUpdateResponse;
import com.vechona.com.data.remote.apiResponse.WishOrSharedListResponse;
import com.vechona.com.ui.model.BankAccountDetails;
import com.vechona.com.ui.model.CancelOrder;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.CreateOrder;
import com.vechona.com.ui.model.DeleteCart;
import com.vechona.com.ui.model.GetPincodeAvailablePrice;
import com.vechona.com.ui.model.OrderDetails;
import com.vechona.com.ui.model.OrderUpdate;
import com.vechona.com.ui.model.PincodeCheckModel;
import com.vechona.com.ui.model.Review;
import com.vechona.com.ui.model.User;
import com.vechona.com.ui.model.UserRegisterToken;
import com.vechona.com.ui.model.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/users/auth")
    Call<AuthResponse> authUser(@Body UserRequest user);

    @POST("/users/logout")
    Call<UserResponse> logoutUser(@Body User user);

    @POST("/users/register-notification-token")
    Call<UserResponse> registerToken(@Body UserRegisterToken userRegisterToken);

    @POST("/user_account_details/add")
    Call<BankDetailsResponse> bankUser(@Body BankAccountDetails bankAccountDetails);

    @POST("/user_account_details/update")
    Call<UpdateBankDetailsResponse> updateBankDetail(@Body BankAccountDetails bankAccountDetails);

    //home
    @GET("/catalogs/home-search/{SEARCH}")
    Call<TopCategory> getHomeSearch(@Path("SEARCH") String categoryName);

    @GET("/home_sliders/list")
    Call<HomeSliderResponse> getHomeSliderData();

    @GET("/categories/top-list")
    Call<TopCategoryResponse> getTopCategoryResponse();

    @GET("/home_catalogs/item-list")
    Call<CollectionItemResponse> getHomeCatalog();

    @GET("/catalogs/get-with-top-category/{CATEGORY_NAME}")
    Call<TopCategory> getTopCategory(@Path("CATEGORY_NAME") String categoryName);

    @GET("/collections/list")
    Call<CollectionResponse> getCollections();

    @GET("/collection_items/item-list/{COLLECTION_ID}/")
    Call<CollectionItemResponse> getCatalogList(@Path("COLLECTION_ID") int collectionId);

    @POST("/users/update")
    Call<UserUpdateResponse> updateUser(@Body User user);

    //Cart
    @GET("/carts/cart-items/{USER_ID}")
    Call<GetCartResponse> getUserCart(@Path("USER_ID") int userId);

    @POST("/carts/add")
    Call<CartAddUpdateResponse> addCart(@Body CartItem cartItem);

    @POST("/carts/update")
    Call<CartAddUpdateResponse> updateCart(@Body CartItem cartItem);

    @HTTP(method = "DELETE", path = "/carts/delete/", hasBody = true)
    Call<CartAddUpdateResponse> deleteCart(@Body DeleteCart deleteCart);


    //Order
    @POST("/orders/create")
    Call<CreateOrderResponse> createOrder(@Body CreateOrder createOrder);

    @POST("/orders/update")
    Call<OrderDetailsResponse> updateOrder(@Body OrderUpdate orderUpdate);

    @POST("/orders/cancel")
    Call<OrderDetailsResponse> orderCancel(@Body CancelOrder cancelOrder);

    @POST("/orders/order-details")
    Call<UserOrderDetailsResponse> getOrderDetails(@Body OrderDetails orderDetails);

    @GET("/orders/user-order-list/{user_id}")
    Call<UserOrderListResponse> getUserOrderList(@Path("user_id") int userId);

    //wishlist
    @GET("/wishlists/list/{USER_ID}")
    Call<CollectionItemResponse> getWishList(@Path("USER_ID") int userID);

    @POST("/wishlists/add")
    Call<WishOrSharedListResponse> addWishList(@Body Catalog catalog);

    @DELETE("/wishlists/delete/{USER_ID}/{CATALOG_ID}")
    Call<WishOrSharedListResponse> deleteWishList(@Path("USER_ID") int userID, @Path("CATALOG_ID") int catalogID);

    //Sharedlist
    @POST("/shared_items/add")
    Call<WishOrSharedListResponse> addSharedItem(@Body Catalog catalog);

    @GET("/shared_items/list/{USER_ID}")
    Call<CollectionItemResponse> getSharedList(@Path("USER_ID") int userId);

    //check pincode availability
    @GET("/pincodes/is-pincode-available/{pincode}")
    Call<DeliveryPincodeResponse> checkAvailability(@Path("pincode") String pincode);

    //check pincode availability New By oor Developer
    @POST("/pincodes/is-pincode-available")
    Call<GetPincodeAvailablePrice> checkAvailabilityNew(@Body PincodeCheckModel pincodeCheckModel);

    //check pincode availability for COD
    @GET("/pincodes/is-cod-pincode-available/{pincode}")
    Call<DeliveryPincodeResponse> checkCODAvailability(@Path("pincode") String pincode);

    //setting
    @GET("/settings/list")
    Call<SettingResponse> getSetting();

    //review
    @GET("/product_reviews/catalog/{CATALOG_ID}")
    Call<ReviewResponse> getCatalogReviews(@Path("CATALOG_ID") int catalogID);

    @GET("/product_reviews/product/{PRODUCT_ID}")
    Call<ReviewResponse> getProductReviews(@Path("PRODUCT_ID") int productID);

    @POST("/product_reviews/add")
    Call<ReviewResponse> addProductReview(@Body Review review);

}
