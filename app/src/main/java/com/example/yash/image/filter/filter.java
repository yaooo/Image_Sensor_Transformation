package com.example.yash.image.filter;
import android.graphics.Bitmap;
import com.mukesh.image_processing.ImageProcessor;
import java.util.Random;

public class filter {

    /**
     * Randomly pick a filter effect from seven filters: invert, blur, greyscale, blackwhite, snoweffect, roundcorner, bright
     * @param bitmap source image used
     * @return image after modification
     */
    public static Bitmap random(Bitmap bitmap){
        // Reduce the size of the image in order to reduce response time
        Bitmap resize = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.6), (int)(bitmap.getHeight()*0.6), true);
        ImageProcessor imageProcessor = new ImageProcessor();
        int random = new Random().nextInt(6)+1;
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

    public static Bitmap magneticFilter(Bitmap bitmap){
        Bitmap resize = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.6), (int)(bitmap.getHeight()*0.6), true);
        ImageProcessor imageProcessor = new ImageProcessor();
            return imageProcessor.applyFleaEffect(resize);
    }
}
