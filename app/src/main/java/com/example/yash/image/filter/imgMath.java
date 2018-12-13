package com.example.yash.image.filter;

public class imgMath {

    /**
     * Linear interpolation.
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static int lerp(float t, int a, int b) {
        return (int)(a + t * (b - a));
    }

    /**
     * Linear interpolation of ARGB values.
     * @param t the interpolation parameter
     * @param rgb1 the lower interpolation range
     * @param rgb2 the upper interpolation range
     * @return the interpolated value
     */
    public static int mixColors(float t, int rgb1, int rgb2) {
        int a1 = (rgb1 >> 24) & 0xff;
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;
        int a2 = (rgb2 >> 24) & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;
        a1 = lerp(t, a1, a2);
        r1 = lerp(t, r1, r2);
        g1 = lerp(t, g1, g2);
        b1 = lerp(t, b1, b2);
        return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
    }

    /**
     * Bilinear interpolation of ARGB values.
     * @param x the X interpolation parameter 0..1
     * @param y the y interpolation parameter 0..1
     * @param nw array of four ARGB values in the order NW, NE, SW, SE
     * @return the interpolated value
     */
    public static int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
        float m0, m1;
        int a0 = (nw >> 24) & 0xff;
        int r0 = (nw >> 16) & 0xff;
        int g0 = (nw >> 8) & 0xff;
        int b0 = nw & 0xff;
        int a1 = (ne >> 24) & 0xff;
        int r1 = (ne >> 16) & 0xff;
        int g1 = (ne >> 8) & 0xff;
        int b1 = ne & 0xff;
        int a2 = (sw >> 24) & 0xff;
        int r2 = (sw >> 16) & 0xff;
        int g2 = (sw >> 8) & 0xff;
        int b2 = sw & 0xff;
        int a3 = (se >> 24) & 0xff;
        int r3 = (se >> 16) & 0xff;
        int g3 = (se >> 8) & 0xff;
        int b3 = se & 0xff;

        float cx = 1.0f-x;
        float cy = 1.0f-y;

        m0 = cx * a0 + x * a1;
        m1 = cx * a2 + x * a3;
        int a = (int)(cy * m0 + y * m1);

        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int)(cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int)(cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int)(cy * m0 + y * m1);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static double[][] rotateMat_55(double[][] m, char axis, double angle){
        System.out.println("rotateMat:   angle:   " + Math.toDegrees(angle));

        switch(axis) {

            case 'x':
                m = matMul(new double[][]
                        {{1, 0, 0, 0, 0},
                                {0, Math.cos(angle), -Math.sin(angle), 0, 0},
                                {0, Math.sin(angle), Math.cos(angle), 0, 0},
                                {0, 0, 0, 1, 0},
                                {0, 0, 0, 0, 1}}, m);
                System.out.println("rotateMat: enter case x");
                break;
            case 'y':
                m = matMul(new double[][]
                        {{Math.cos(angle), 0, Math.sin(angle), 0, 0},
                                {0, 1, 0, 0, 0},
                                {-Math.sin(angle), 0, Math.cos(angle), 0, 0},
                                {0, 0, 0, 1, 0},
                                {0, 0, 0, 0, 1}}, m);
                System.out.println("rotateMat: enter case y");
                break;
            case 'z':
                m = matMul(new double[][]
                        {{Math.cos(angle), -Math.sin(angle), 0, 0, 0},
                                {Math.sin(angle), Math.cos(angle), 0, 0, 0},
                                {0, 0, 1, 0, 0},
                                {0, 0, 0, 1, 0},
                                {0, 0, 0, 0, 1}}, m);
                System.out.println("rotateMat: enter case z");
                break;
        }
        return m;
    }

    public static double[][] matMul(double[][] m1, double[][] m2){

        if(m1[0].length != m2.length){

            System.out.println("Matrix dimension not match!");
            return null;
        }
        //counters
        int i, j, k, m, n;
        //rows and columns for each matrix
        int rowsA = m1.length;
        int colsA = m1[0].length;
        //int rowsB = m2.length;
        int colsB = m2[0].length;
        //new matrix to hold result
        double[][] myMatrixC = new double[rowsA][colsB];
        //start across rows of A
        for (i = 0; i < rowsA; i++) {
            //work across cols of B
            for(j = 0; j < colsB; j++) {
                //now complete the addition and multiplication
                for(k = 0; k < colsA; k++) {
                    myMatrixC[i][j] += m1 [i][k] * m2[k][j];
                }
            }
        }

        System.out.println("imgMat.matmul(:  length:   "+myMatrixC.length);
        System.out.println("imgMat.matmul(:  length:   "+myMatrixC[0].length);

        return myMatrixC;
    }
}
