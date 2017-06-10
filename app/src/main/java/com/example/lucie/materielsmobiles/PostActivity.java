package com.example.lucie.materielsmobiles;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class PostActivity extends AppCompatActivity {

    private ImageButton mImageBtn;
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_CAPTURE = 1;
    private Button btnCamera;
    private EditText mPostTitle;
    private Button btnSend;
    private Uri imageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Bitmap photo = null;
    private DatabaseReference mDatabase;
    private Calendar c;
    private Date d;
    private LocationManager locManager;
    private String StrPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mProgress = new ProgressDialog(this);

        mImageBtn = (ImageButton) findViewById(R.id.ButtonSelect);
        mPostTitle = (EditText) findViewById(R.id.TitleField);
        btnSend = (Button) findViewById(R.id.ButtonSend);

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        btnCamera = (Button) findViewById(R.id.TakePicture);
        btnCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
                startActivity(new Intent(PostActivity.this, MainActivity.class) );
            }
        });
    }



    private void startPosting() {
        mProgress.setMessage("Uploading ...");
        c = Calendar.getInstance();
        d = c.getTime();
        getLocation();


        final String title_val = mPostTitle.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && imageUri != null ){

            mProgress.show();
            StorageReference filepath = mStorage.child("Blog_images").child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadURL = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("Title").setValue(title_val);
                    newPost.child("Image").setValue(downloadURL.toString());
                    newPost.child("Date").setValue(d.toString());
                    newPost.child("gps").setValue(StrPosition);

                    mProgress.dismiss();
                    Toast.makeText(PostActivity.this,"Uploading Finished ...",Toast.LENGTH_LONG).show();}
                    public void main(String[] args) throws IOException {
                        PostExample example = new PostExample();
                        String json = example.bowlingJson();
                        String response = example.post("http://www.roundsapp.com/post", json);
                        System.out.println(response);
                    }

                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            mImageBtn.setImageURI(imageUri);
        }

        if(requestCode != GALLERY_REQUEST){
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), photo, "Title", null);
            imageUri= Uri.parse(path);
            mImageBtn.setImageURI(imageUri);

            //mImageBtn.setImageBitmap(photo);

        }
    }

    public void getLocation(){
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    Log.e("Latitude :", "" + location.getLatitude());
                    Log.e("Longitude :", "" + location.getLongitude());
                } }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { } };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double latitudeDouble=0;
        double longitudeDouble=0;
        latitudeDouble = location.getLatitude();
        longitudeDouble = location.getLongitude();
        String StrLatitude= String.valueOf(latitudeDouble);
        String StrLongitude = String.valueOf(longitudeDouble);
        StrPosition = "Latitude: "+StrLatitude+", Longitude: "+StrLongitude;

    }


}
