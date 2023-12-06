package com.vjnicacio.smartgames.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageUtils {
    public static void setBase64Image(ImageView imageView, String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        imageView.setImageBitmap(bitmap);
    }
}
