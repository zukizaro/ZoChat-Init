package com.zochat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zochat.models.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityDetailUser extends AppCompatActivity {
    final int RESULT_LOAD_IMAGE = 99;
    ImageView imgUser;
    private DatabaseReference listuser;
    private DatabaseReference datachat;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        listuser = database.getReference("listuser");
        datachat = database.getReference("datachat");

        tvEmail.setText(user.getEmail()+"");

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        });

        listuser.child(user.getUid()).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    String strImg = dataSnapshot.getValue().toString();
                    byte[] bImg = Base64.decode(strImg, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
                    imgUser.setImageBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri imgUri = data.getData();
            Picasso.with(ActivityDetailUser.this).load(imgUri).resize(240,240).into(imgUser);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == android.R.id.home){

           if(Friend.imagViewToByte(imgUser) != null){
               String strImage = Base64.encodeToString(Friend.imagViewToByte(imgUser),Base64.DEFAULT);
               listuser.child(user.getUid()).child("image").setValue(strImage);
           }

           Intent intent = new Intent(ActivityDetailUser.this,ActivityFriend.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           startActivity(intent);
           finish();
       }
        return super.onOptionsItemSelected(item);
    }
}
