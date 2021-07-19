package com.abdul.sharinglib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Util Class to handle the sharing
 */
public class ShareUtil {

    /**
     * Utility method to share text, URL and images
     *
     * @param context
     * @param sharingText
     * @param imageUri
     */
    public static void share(Context context, String sharingText, Uri imageUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (imageUri != null) {
//            shareIntent.setType("*/*");
            shareIntent.setType("image/jpeg");
            Uri imgUri = convertUriBitmapUtil(context, imageUri);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        } else {
            shareIntent.setType("text/plain");
        }
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Email Subject");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
        context.startActivity(Intent.createChooser(shareIntent, "Select"));
    }

    private static Uri convertUriBitmapUtil(Context context, Uri uri) {
        Bitmap bitmap;
        Uri imageUri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), uri));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            imageUri = Uri.parse(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }
}
