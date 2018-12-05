package com.example.yash.image.filter;

import android.graphics.Bitmap;
import android.util.Log;

import com.mukesh.image_processing.ImageProcessor;

import java.text.BreakIterator;
import java.util.Random;

public class filter {

    public static Bitmap random(Bitmap bitmap){

        Bitmap resize = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.6), (int)(bitmap.getHeight()*0.6), true);

        ImageProcessor imageProcessor = new ImageProcessor();

        int random = new Random().nextInt(6)+1; // [0, 10]

        switch (random){
            case 1:  return imageProcessor.doInvert(resize);
            case 2:   return imageProcessor.applyGaussianBlur(resize);
            case 3:   return imageProcessor.doGreyScale(resize);
                
            case 4:   return imageProcessor.applyBlackFilter(resize);
                
            case 5:  return imageProcessor.applySnowEffect(resize);
                
            case 6:   return imageProcessor.roundCorner(resize, 5 );
                
            case 7:   return imageProcessor.doBrightness(resize, 40);
                
            default:
                return resize;
                
        }
    }

}
