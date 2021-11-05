package com.vechona.com.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ImageDownloadUtils {

    private static ArrayList<Uri> imageUriArray = new ArrayList<>();
    private static Catalog catalog1;

    public static void shareImages(final Context context, final Catalog catalog, final Product product) {
        if (catalog != null) {
            catalog1 = catalog;
            for (int i = 0; i < catalog.getProducts().size(); i++) {
                Product product1 = catalog.getProducts().get(i);
                if (product1.getProductsImageUrls().size() > 0) {
                    String imageUrl = product1.getProductsImageUrls().get(0).getImageUrl();
                    String imageFileName = imageUrl.substring(imageUrl.indexOf("AW/") + 3);
                    String filePath = Environment.getExternalStorageDirectory() + "/ECOM/" + imageFileName;
                    File file = new File(filePath);
                    if (file.exists())
                        galleryAddPic(context, product1, filePath, i);
                    else
                        downloadImage(context, product1, i);
                }
            }
        } else {
            catalog1 = null;
            int productImageUrlSize = product.getProductsImageUrls().size();
            for (int i = 0; i < productImageUrlSize; i++) {
                String imageUrl = product.getProductsImageUrls().get(i).getImageUrl();
                String imageFileName = imageUrl.substring(imageUrl.indexOf("AW/") + 3);
                String filePath = Environment.getExternalStorageDirectory() + "/ECOM/" + imageFileName;
                File file = new File(filePath);
                if (file.exists())
                    galleryAddPic(context, product, filePath, i);
                else
                    downloadImage(context, product, i);
            }
        }
    }

    private static void downloadImage(final Context context, final Product product, final int i) {
        String imageUrl;
        if (catalog1 != null)
            imageUrl = product.getProductsImageUrls().get(0).getImageUrl();
        else
            imageUrl = product.getProductsImageUrls().get(i).getImageUrl();
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(context, product, resource, i);
                    }
                });
    }

    private static void saveImage(Context context, Product product, Bitmap resource, int i) {
        String savedImagePath;
        String imageUrl;
        if (catalog1 != null)
            imageUrl = product.getProductsImageUrls().get(0).getImageUrl();
        else
            imageUrl = product.getProductsImageUrls().get(i).getImageUrl();
        String imageFileName = imageUrl.substring(imageUrl.indexOf("AW/") + 3);
        String filePath = Environment.getExternalStorageDirectory() + "/ECOM";
        File storageDir = new File(filePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = new File(storageDir, imageFileName);
        savedImagePath = imageFile.getAbsolutePath();
        try {
            OutputStream fOut = new FileOutputStream(imageFile);
            resource.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        galleryAddPic(context, product, savedImagePath, i);

    }

    private static void galleryAddPic(Context context, Product product, String savedImagePath, int i) {
        File f = new File(savedImagePath);
        Uri contentUri = Uri.fromFile(f);
        imageUriArray.add(contentUri);
        if (catalog1 != null) {
            if (i == catalog1.getProducts().size() - 1) {
                WhatsAppShareUtils.shareImages(imageUriArray, context);
                imageUriArray.clear();
            }
        } else {
            if (i == product.getProductsImageUrls().size() - 1) {
                WhatsAppShareUtils.shareImages(imageUriArray, context);
                imageUriArray.clear();
            }
        }
    }
}
