package com.example.yash.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yash.image.filter.HSBAdjustFilter;
import com.example.yash.image.filter.filter;
import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mProximity, mAccelerometer;
    private SensorEventListener mSensorListener;

    private static final int SENSOR_SENSITIVITY = 4;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;

    private static String path = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = findViewById(R.id.imageView);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        mSensorListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                    System.out.println(sensor);
                if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                    if(imageView.getDrawable() != null){



                        String magentic = "Magnetic Field\n" + "X：" + event.values[0] + "\n" + "Y:"
                            + event.values[1] + "\n" + "Z:" + event.values[2] + "\n";
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    float sum = (x*x + y*y + z*z);
                    float mag = (float) Math.sqrt(sum);


                    float expectedMag = 50;
                    if (mag > 1.8*expectedMag || mag < 0.2*expectedMag) {
                            Toast.makeText(getApplicationContext(),magentic, Toast.LENGTH_SHORT).show();
                            Drawable d = imageView.getDrawable();

                            //TODO
                            d = convertToGrayscale(d);
                            imageView.setImageDrawable(d);
                        }
                    }else{
                        imageView.setImageDrawable(Drawable.createFromPath(path));
                    }
                }

                //TODO
                if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if(imageView.getDrawable() != null) {
                        if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                            Drawable d = convertToGrayscale(Drawable.createFromPath(path));
                            imageView.setImageDrawable(d);
                            Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                        } else {
                            //far
                            imageView.setImageDrawable(Drawable.createFromPath(path));
                            Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                //if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            }
        };


        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                Toast.makeText(getApplicationContext(), "Device shaken!", Toast.LENGTH_SHORT).show();

                //Drawable d = imageView.getDrawable();
                imageView.buildDrawingCache();
                Bitmap imageToSave = imageView.getDrawingCache();

                Bitmap newImg = filter.random(imageToSave);

                imageView.setImageBitmap(newImg);
            }
        });

        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener, mProximity, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);

        Button buttonLoadImage = findViewById(R.id.buttonImage);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        ShakeDetector.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }

    public void resetPhoto(View v){
        if(path.length()>1)
            imageView.setImageDrawable(Drawable.createFromPath(path));
    }


    public void savePhoto(View v) throws IOException {
        if(path.length()>1) {
            imageView.buildDrawingCache();
            Bitmap imageToSave = imageView.getDrawingCache();

            createDirectoryAndSaveFile(imageToSave);

            Toast.makeText(getApplicationContext(), "Saved to Sensor folder", Toast.LENGTH_LONG).show();

        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave) {
        String fileName = makePhotoPath();
        File direct = new File(Environment.getExternalStorageDirectory() + "/Sensor");

        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/Sensor");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File(Environment.getExternalStorageDirectory() + "/Sensor"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String makePhotoPath(){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            return "Sensor_" + currentDateandTime+".jpg";
    }

    private Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            path = picturePath;

        }


    }

    protected Drawable convertToGrayscale(Drawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }


    protected Drawable randomFiler(Drawable drawable){
        return null;
    }

    protected Drawable magneticFilter(Bitmap source){
        return null;
    }

    protected Drawable ColorFilterTransformation(BitmapDrawable source)
    {

        int mColor = 2;

        Bitmap image = source.getBitmap();
        int width = image.getWidth();
        int height = image.getHeight();


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(image, 0, 0, paint);

        image.recycle();

        return new BitmapDrawable(getResources(), image);

    }


    protected Drawable circleImageTransform(BitmapDrawable feedIn) {

        Bitmap source = feedIn.getBitmap();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        source.recycle();

        return new BitmapDrawable(getResources(), source);
    }

}
