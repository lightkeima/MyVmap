package vmap.a2016.khkt.myvmap.AppActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import vmap.a2016.khkt.myvmap.R;

public class img_activity extends AppCompatActivity {
    ArrayList<String> images;  // array of images
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_layout_activity);
        // get The references of ViewFlipper
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mynew-2639a.appspot.com/LocationImg");
        final ViewFlipper simpleViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper); // get the reference of ViewFlipper
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("getIMG");
        images = new ArrayList<>();
        images = packageFromCaller.getStringArrayList("img");
        // loop for creating ImageView's
        dialog = ProgressDialog.show(img_activity.this, "", "Đang tải vui lòng đợi", true,false);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // do the thing that takes a long time

                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        dialog.dismiss();
                    }
                });
            }
        }).start();
        for (int i = 0; i < images.size(); i++) {
            // create the object of ImageView
            try{

                final File localFile = File.createTempFile("locationimg", "jpg");
                StorageReference storageRefChild = storageRef.child(images.get(i));
                storageRefChild.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        ImageView imageView =  new ImageView(img_activity.this);
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                        simpleViewFlipper.addView(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
            catch (IOException e){
                throw new Error("Error");
            }
            // add the created
           // TextView textView = (TextView) findViewById(R.id.mytest);
            //textView.setText(images.get(i));
            // ImageView in ViewFlipper
        }
        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        // set the animation type's to ViewFlipper
        simpleViewFlipper.setInAnimation(in);
        simpleViewFlipper.setOutAnimation(out);
        simpleViewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewFlipper.showNext();
            }
        });

        // set interval time for flipping between views
        simpleViewFlipper.setFlipInterval(3000);
        // set auto start for flipping between views
        simpleViewFlipper.setAutoStart(false);
        dialog.dismiss();

    }

}





















