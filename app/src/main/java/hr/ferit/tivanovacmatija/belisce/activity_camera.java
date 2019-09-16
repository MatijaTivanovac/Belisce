package hr.ferit.tivanovacmatija.belisce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class activity_camera extends AppCompatActivity {

    Button send;
    ImageView imageView;
    String pathToFile;
    protected static final int CAMERA_PIC_REQUEST = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        send = (Button) findViewById(R.id.bSend);


        Button camera = (Button) findViewById(R.id.bTakePic);
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction();

            }
        });
        imageView = findViewById(R.id.image);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tivanovacmatija@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Complaint");
                i.setType("image/png");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activity_camera.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void dispatchPictureTakerAction(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(activity_camera.this, "com.codepath.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, 1);
            }
        }
    }

    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("myLog", "Excep: " + e.toString());
        }
        return image;
    }


}























