package com.vjnicacio.smartgames.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageUtils {

    // Método para configurar uma imagem em um ImageView a partir de uma representação Base64
    public static void setBase64Image(ImageView imageView, String base64Image) {
        // Decodifica a representação Base64 para obter os bytes da imagem
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

        // Converte os bytes decodificados em um objeto Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        // Define o bitmap no ImageView para exibir a imagem
        imageView.setImageBitmap(bitmap);
    }
}
