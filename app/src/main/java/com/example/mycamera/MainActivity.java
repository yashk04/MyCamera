package com.example.mycamera;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        //open camera

        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(v);
            }
        });

        frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                camera.startPreview();
                return true;
            }
        });
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file  = getOutputMediaFile();
            if(picture_file == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();
//                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if(!folder_gui.exists()) {
                folder_gui.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputFile = new File(folder_gui, "IMG_"+ timeStamp +".jpg");
            return outputFile;
        }
    }

    public void captureImage(View v) {
        if(camera != null) {
            camera.takePicture(null,null,mPictureCallback);
        }
    }

}