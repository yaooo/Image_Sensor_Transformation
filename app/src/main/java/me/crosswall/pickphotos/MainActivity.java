package me.crosswall.pickphotos;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;

public class MainActivity extends AppCompatActivity {
    Button imageUpload;
    ImageView imageView;
    static final int PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView= (ImageView) findViewById(R.id.imageView);
        imageUpload= (Button) findViewById(R.id.buttonImage);
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICTURE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICTURE && resultCode==RESULT_OK && null !=data)
        {

            Uri uri = data.getData();
            String[] prjection ={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,prjection,null,null,null);
            cursor.moveToFirst();

            int columnIndex=cursor.getColumnIndex(prjection[0]);
            String path=cursor.getString(columnIndex);
            cursor.close();

            Bitmap selectFile = BitmapFactory.decodeFile(path);


            Drawable d = new BitmapDrawable(selectFile);
            d = convertToGrayscale(d);
            imageView.setImageDrawable(d);


            // imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        }


    }

    protected Drawable convertToGrayscale(Drawable drawable)
    {
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
