package com.example.yash.image.filter;
import android.graphics.ColorMatrix;


/**
 * Ref: JH lab: HSB Filter
 */
public class HSBAdjustFilter {

    public float hFactor, sFactor, bFactor;
    public boolean canFilterIndexColorModel;
    private float[] hsb = new float[3];
    /**
     * Luminance constants for computing saturation
     */
    public final float lum_r = 0.3086f, lum_g = 0.6094f, lum_b = 0.082f;
    //public final float lum_r = 0.299f, lum_g = 0.587f, lum_b = 0.114f;


    public HSBAdjustFilter() {
        this(0, 0, 0);
    }

    public HSBAdjustFilter(float h, float s, float b) {
        hFactor = h;
        sFactor = s;
        bFactor = b;
        canFilterIndexColorModel = true;
    }

    public static ColorMatrix filterRGB(double h, float s, float b) {

        double hue = h;
        float sFactor = s;
        float bFactor = b;

        //build hue rotation matrix
        double[][] hueMat = new double[][]
                {{1, 0, 0, 0, 0 },
                {0, 1, 0, 0, 0 },
                {0, 0, 1, 0, 0 }, {0, 0, 0, 1, 0 },{0, 0, 0, 0, 1}};
        hueMat = imgMath.rotateMat_55(hueMat, 'x', Math.PI/4);
        hueMat = imgMath.rotateMat_55(hueMat, 'y', Math.atan((double)-1/Math.sqrt(2)));
        hueMat = imgMath.rotateMat_55(hueMat, 'z', hue);
        hueMat = imgMath.rotateMat_55(hueMat, 'y', Math.atan((double)1/Math.sqrt(2)));
        hueMat = imgMath.rotateMat_55(hueMat, 'x', -Math.PI/4);


        double[][] foo = new double[][]
                {{1, 0, 0, 0, 0 },
                {0, 1, 0, 0, 0 },
                {0, 0, 1, 0, 0 }, {0, 0, 0, 1, 0 }};
        //build contrast matrix
        //build saturation matrix
        //build brightness matrix

        double[][] result_double = imgMath.matMul(foo, hueMat);


        float[] result_float = new float[result_double.length * result_double[0].length];
        System.out.println("HSB.filter(:  length:   "+result_float.length);

        for (int i = 0 ; i < result_double.length; i++)
        {
            for(int j = 0 ; j < result_double[0].length; j++)
            result_float[result_double[0].length * i + j] = (float) result_double[i][j];
        }
        //argb = Color.HSVToColor(hsb);
        return new ColorMatrix(result_float);
    }

    public String toString() {
        return "Colors/Adjust HSB...";
    }
}
