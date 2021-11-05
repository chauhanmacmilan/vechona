package com.vechona.com.data.local;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.SenderDetailsItem;
import com.vechona.com.ui.model.ShippingAddressItem;
import com.vechona.com.ui.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.google.gson.Gson;
//import com.squareinfosoft.allanalytics.ui.model.User;

/**
 * Created on 20/03/17.
 */

public class PreferenceDataHelper {

    private static final String FCM_TOKEN = "fcm_token";
    private static final String IS_COD_ENABLE = "is_cod_enable";
    //private static final String RETURN_POLICY = "return_policy";
    private static PreferenceDataHelper instance;
    private static final String USER = "user";
    private static final String BANK_ACCOUNT_DETAILS = "bank_account_details";
    private static final String FAVORITES = "favorites";
    private static final String SHARED_CATALOG = "shared_catalog";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String CART_LIST = "cart_list";
    private static final String CURRENT_CART_SUPPLIER = "current_cart_supplier";
    private static final String SHIPPING_ADDRESS = "shipping_address";
    private static final String SENDER_DETAILS = "sender_details";

    private SharedPreferenceHelper sharedPreferenceHelper;

    private PreferenceDataHelper(Context context) {
        SharedPreferenceHelper.initialize(context);
        this.sharedPreferenceHelper = SharedPreferenceHelper.getInstance();
    }

    public static synchronized PreferenceDataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceDataHelper(context);
        }
        return instance;
    }

    public void firstTimeAskingPermission(String permission, boolean isFirstTime) {
        sharedPreferenceHelper.setBoolean(permission, isFirstTime);
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        return sharedPreferenceHelper.getBoolean(permission, true);
    }

    public String getAccessToken() {
        return sharedPreferenceHelper.getString(ACCESS_TOKEN, null);
    }

    public void saveAccessToken(String newToken) {
        sharedPreferenceHelper.setString(ACCESS_TOKEN, newToken);
    }

    public String getFcmToken() {
        return sharedPreferenceHelper.getString(FCM_TOKEN, null);
    }

    public void saveFCMToken(String fcmToken) {
        sharedPreferenceHelper.setString(FCM_TOKEN, fcmToken);
    }

    public void saveUser(User user) {
        String userString = new Gson().toJson(user, User.class);
        sharedPreferenceHelper.setString(USER, userString);
    }

    public User getUser() {
        return new Gson().fromJson(sharedPreferenceHelper.getString(USER), User.class);
    }

    public void clearUser() {
        sharedPreferenceHelper.removeKey(USER);
        sharedPreferenceHelper.removeKey(ACCESS_TOKEN);
        sharedPreferenceHelper.removeKey(FAVORITES);
        sharedPreferenceHelper.removeKey(SHARED_CATALOG);
        sharedPreferenceHelper.removeKey(CART_LIST);
        sharedPreferenceHelper.removeKey(SENDER_DETAILS);
        sharedPreferenceHelper.removeKey(BANK_ACCOUNT_DETAILS);
        sharedPreferenceHelper.removeKey(CURRENT_CART_SUPPLIER);
        sharedPreferenceHelper.removeKey(SHIPPING_ADDRESS);
        sharedPreferenceHelper.removeKey(FCM_TOKEN);
    }

    public List<Catalog> getFavorite() {
        List<Catalog> favorites;
        favorites = new Gson().fromJson(sharedPreferenceHelper.getString(FAVORITES), new TypeToken<List<Catalog>>() {
        }.getType());
        if (favorites == null)
            return null;

        return favorites;
    }

    public void addFavorite(Catalog catalog, boolean isWishList) {
        List<Catalog> favorites = isWishList ? getFavorite() : getSharedItem();
        if (favorites == null)
            favorites = new ArrayList<>();
        for (Catalog fav : favorites) {
            if (catalog.getCatalogId() == fav.getCatalogId())
                return;
        }
        favorites.add(catalog);
        saveFavorites(favorites, isWishList);
    }

    public List<Catalog> getSharedItem() {
        List<Catalog> sharedItems;
        sharedItems = new Gson().fromJson(sharedPreferenceHelper.getString(SHARED_CATALOG), new TypeToken<List<Catalog>>() {
        }.getType());
        if (sharedItems == null)
            return null;
        return sharedItems;
    }

    public void removeFaverite(Catalog catalog, boolean isWishList) {
        List<Catalog> favorites = getFavorite();
        if (favorites != null && favorites.size() > 0) {
            int index = 0;
            for (Catalog fav : favorites) {
                if (catalog.getCatalogId() == fav.getCatalogId())
                    break;
                index++;
            }
            if (index < favorites.size()) {
                favorites.remove(index);
                saveFavorites(favorites, isWishList);
            }
        }
    }

    public void saveFavorites(List<Catalog> favorites, boolean isWishList) {
        if (isWishList)
            sharedPreferenceHelper.setString(FAVORITES, new Gson().toJson(favorites));
        else
            sharedPreferenceHelper.setString(SHARED_CATALOG, new Gson().toJson(favorites));
    }

    public boolean isWishOrShareList(Catalog catalog, boolean isWishList) {
        List<Catalog> favorites = isWishList ? getFavorite() : getSharedItem();
        if (favorites != null) {
            for (Catalog fav : favorites) {
                if (catalog.getCatalogId() == fav.getCatalogId())
                    return true;
            }
        }
        return false;
    }


    public List<CartItem> getCartList() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList = new Gson().fromJson(sharedPreferenceHelper.getString(CART_LIST), new TypeToken<List<CartItem>>() {
        }.getType());
        if (cartItemList == null)
            return null;
        return cartItemList;
    }

    public void addCartItem(CartItem cartItem) {
        List<CartItem> cartItemList = getCartList();
        if (cartItemList == null)
            cartItemList = new ArrayList<CartItem>();
        for (Iterator<CartItem> iterator = cartItemList.iterator(); iterator.hasNext(); ) {
            CartItem cartItem1 = iterator.next();
            if (cartItem1.getStockId() == cartItem.getStockId()) {
                cartItem1.setQuantity(cartItem1.getQuantity() + cartItem.getQuantity());
                saveCartList(cartItemList);
                return;
            }
        }
        cartItemList.add(cartItem);
        saveCartList(cartItemList);
    }

    public void removeCartItem(int position) {
        List<CartItem> cartItemList = getCartList();
        if (cartItemList != null) {
            cartItemList.remove(position);
            saveCartList(cartItemList);
        }
    }

    public void clearCart() {
        List<CartItem> cartItems = getCartList();
        cartItems.clear();
        saveCartList(cartItems);
    }

    public int getCartItemCount() {
        int cartCount = 0;
        List<CartItem> cartItems = getCartList();
        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                cartCount += cartItem.getQuantity();
            }
        }
        return cartCount;
    }

    public void saveCartList(List<CartItem> cartItemList) {
        sharedPreferenceHelper.setString(CART_LIST, new Gson().toJson(cartItemList));
    }

    public String getCurrentCartSupplier() {
        return sharedPreferenceHelper.getString(CURRENT_CART_SUPPLIER);
    }

    public void setCurrentCartSupplier(String supplier) {
        sharedPreferenceHelper.setString(CURRENT_CART_SUPPLIER, supplier);
    }

    public void addShippingAddress(ShippingAddressItem shippingAddressItem) {
        List<ShippingAddressItem> shippingAddressItems = getShippingAddressList();
        if (shippingAddressItems == null)
            shippingAddressItems = new ArrayList<ShippingAddressItem>();
        shippingAddressItems.add(shippingAddressItem);
        saveShippingAddressList(shippingAddressItems);
    }

    public List<ShippingAddressItem> getShippingAddressList() {
        List<ShippingAddressItem> addressItemList = new ArrayList<>();
        addressItemList = new Gson().fromJson(sharedPreferenceHelper.getString(SHIPPING_ADDRESS), new TypeToken<List<ShippingAddressItem>>() {
        }.getType());
        return addressItemList;
    }

    public ShippingAddressItem getSelectedShippingAddress() {
        List<ShippingAddressItem> addressItemList = getShippingAddressList();
        for (ShippingAddressItem shippingAddressItem : addressItemList) {
            if (shippingAddressItem.isSelected())
                return shippingAddressItem;
        }
        return null;
    }

    public void saveShippingAddressList(List<ShippingAddressItem> shippingAddressList) {
        sharedPreferenceHelper.setString(SHIPPING_ADDRESS, new Gson().toJson(shippingAddressList));
    }

    public void addSenderDetail(SenderDetailsItem senderDetailsItem) {
        List<SenderDetailsItem> senderDetailsItems = getSenderDetailsList();
        if (senderDetailsItems == null)
            senderDetailsItems = new ArrayList<SenderDetailsItem>();
        senderDetailsItems.add(senderDetailsItem);
        saveSenderDetailsList(senderDetailsItems);
    }

    public List<SenderDetailsItem> getSenderDetailsList() {
        List<SenderDetailsItem> senderDetailsItems = new ArrayList<>();
        senderDetailsItems = new Gson().fromJson(sharedPreferenceHelper.getString(SENDER_DETAILS), new TypeToken<List<SenderDetailsItem>>() {
        }.getType());
        return senderDetailsItems;
    }

    public SenderDetailsItem getSelectedSenderDetail() {
        List<SenderDetailsItem> senderDetailsList = getSenderDetailsList();
        for (SenderDetailsItem senderDetailsItem : senderDetailsList) {
            if (senderDetailsItem.isSelected())
                return senderDetailsItem;
        }
        return null;
    }

    public void saveSenderDetailsList(List<SenderDetailsItem> senderDetailsList) {
        sharedPreferenceHelper.setString(SENDER_DETAILS, new Gson().toJson(senderDetailsList));
    }


    public void setCODEnable(boolean codEnable) {
        sharedPreferenceHelper.setBoolean(IS_COD_ENABLE, codEnable);
    }

    public boolean isCOdEnable() {
        return sharedPreferenceHelper.getBoolean(IS_COD_ENABLE);
    }

    /*public String getReturnPolicyPeriod() {
        return sharedPreferenceHelper.getString(RETURN_POLICY);
    }

    public void setReturnPolicyPeriod(String codEnable) {
        sharedPreferenceHelper.setString(RETURN_POLICY, codEnable);
    }*/
}

