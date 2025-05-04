package com.example.bennettmitchell_final.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageHandler {
    // this was officially the point where i had to start putting stuff into seperate modules.

    private static Resources res;
    // Converts from bitmap to byte array
    public static byte[] convertBitmap(Bitmap img){
        if (img != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        else return null;
    }
    // Converts from byte array to bitmap
    public static Bitmap convertBitmap(byte[] img){
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            return bitmap;
        }
        else return null;
    }

    public static Bitmap getImageFromWeb(String url){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = executor.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                if (url != null) {
                    try {
                        InputStream inputStream = (InputStream) new URL(url).getContent();
                        Bitmap draw = BitmapFactory.decodeStream(inputStream);
//                      Drawable draw = Drawable.createFromStream(inputStream, "Box Art");
                        return draw;
                    } catch (MalformedURLException e) {
                        Log.e("Image Error", "Malformed URL");
                    } catch (IOException e) {
                        Log.e("Image Error", "IO Exception");
                    } catch (Exception e) {
                        Log.e("Image Error", e.toString());
                    }
                }
                return null;
            }
        });
        try{
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
    public static void setRes(Resources r){res = r;}

    public static Bitmap getResource(int id){
        return BitmapFactory.decodeResource(res, id);
    }
}
