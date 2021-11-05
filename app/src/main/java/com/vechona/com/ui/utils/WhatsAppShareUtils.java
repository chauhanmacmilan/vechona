package com.vechona.com.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.ArrayList;

public class WhatsAppShareUtils {

    public static void shareImages(ArrayList<Uri> imageUriArray, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareIntent);
    }

    public static void shareText(String text, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.setType("text");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(shareIntent);
    }

    public static void shareImagesWithText(ArrayList<Uri> imageUriArray, String text, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareIntent);
    }

    public static boolean isWhatsAppInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo("com.whatsapp", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
