package com.example.cameraapp;

import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    protected static final int CAMERA_REQUEST_ID = 1;
    private Uri file;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newPhoto = (Button) findViewById(R.id.newPhoto);
        newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

    }

    public void launchCamera(){
        Log.i("TAG","Opening camera");
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getFile();

        Log.i("TAG","Putting image onto image file");
        intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, file);

        if(intentTakePhoto.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intentTakePhoto, CAMERA_REQUEST_ID);
        }

    }

    private Uri getFile(){
        //File folder = Environment.getExternalStoragePublicDirectory("/CameraApp/photos");
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File folder = Environment.getExternalStorageDirectory();


        //Creates folder if it doesn't exist already
        if(!folder.exists()){
            folder.mkdir();
        }

        //Name of file
        String fileName = "CameraApp_" + String.valueOf(System.currentTimeMillis());

        //Creates file name with suffix and saves into directory
        File imageFile = null;
        try{
            imageFile = File.createTempFile(fileName, ".jpg", folder);
        }catch(IOException e){
            e.printStackTrace();
        }

        Log.i("TAG","Getting File and looking/creating folder");

        //Gets URI of photo file
        Uri photoURI = null;
        if(imageFile != null){
            photoURI = FileProvider.getUriForFile(this, "com.example.android.provider", imageFile);
        }

            return photoURI;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAG", "On Activity");
        if(requestCode == CAMERA_REQUEST_ID){
            if(resultCode == Activity.RESULT_OK){
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                    Log.i("TAG", "Photo was taken");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else if (resultCode == RESULT_CANCELED){
                Log.e("TAG", "Photo was not taken");
            }
        }
    }
}
