package com.example.lucie.materielsmobiles;



import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");

        mList = (RecyclerView) findViewById(R.id.list_images);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));

    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.lignes_activity,
                PostViewHolder.class,
                mDatabase

            ){
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDate(model.getDate());
                viewHolder.setGps(model.getGps());
                viewHolder.setImage(getApplicationContext(), model.getImage());

            }
        };

        mList.setAdapter(firebaseRecyclerAdapter);
        }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View mview;

        public PostViewHolder(View imageView) {
            super(imageView);
            mview = imageView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mview.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDate(String date) {
            TextView post_date = (TextView) mview.findViewById(R.id.post_date);
            post_date.setText(date);
        }

        public void setGps(String gps) {
            TextView post_gps = (TextView) mview.findViewById(R.id.post_gps);
            post_gps.setText(gps);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class) );
        }
        return super.onOptionsItemSelected(item);
    }
}
