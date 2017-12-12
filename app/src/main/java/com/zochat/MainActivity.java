package com.zochat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zochat.adapters.AdapterCustomLV;
import com.zochat.models.Friend;
import com.zochat.models.Message;
import com.zochat.models.RoundedBitmapDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Message> arrMessages;
    AdapterCustomLV adapter;
    private ListView lsv;
    private EditText etContent;
    private Button btnSend;
    private FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference refDataUser;
    DatabaseReference refDatachat;
    DatabaseReference refFriend;
    DatabaseReference refChatUid;

    Friend friend;
    String myImg = "";
    String imgFriend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        friend = new Friend(bundle.getString("img"),bundle.getString("email"),bundle.getString("uid"));

        getSupportActionBar().setTitle(friend.getEmail());

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        refDataUser = database.getReference("listuser");
        refDatachat = database.getReference("datachat");
        refFriend = refDatachat.child(friend.getUid()).child("chat").child(user.getUid());
        refChatUid = refDatachat.child(user.getUid()).child("chat").child(friend.getUid());

        //Toast.makeText(MainActivity.this,user.getEmail()+"; " + user.getUid(), Toast.LENGTH_LONG).show();
        Log.d("TAG user uid", user.getUid());
        Log.d("TAG friend uid", friend.getUid());
        arrMessages = new ArrayList<>();
        adapter = new AdapterCustomLV(MainActivity.this, R.layout.layout_message_listview, arrMessages);

        refDataUser.child(user.getUid()).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    myImg = dataSnapshot.getValue().toString();
                    if(!friend.getImg().equals("")){
                        byte[] bImg = Base64.decode(friend.getImg(), Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
                        //getSupportActionBar().setLogo(R.drawable.ic_account_circle_24dp);
                        getSupportActionBar().setLogo(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(bmp, 80, 80, false)));
                    }else{
                        getSupportActionBar().setLogo(R.drawable.ic_account_circle_24dp);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refDataUser.child(friend.getUid()).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    imgFriend = dataSnapshot.getValue().toString();

                }
                adapter.setImage(imgFriend,myImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        init();

        lsv.setAdapter(adapter);
        btnSend.setEnabled(false);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setContent(etContent.getText().toString().trim());
                Log.d("Tag date",message.getDate());

                message.setType(false);
                refFriend.push().setValue(message);
                message.setType(true);
                refChatUid.push().setValue(message);
                etContent.setText("");
                //arrMessages.add(message);
                //adapter.notifyDataSetChanged();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etContent.getText().toString().equals("")){
                    btnSend.setEnabled(false);
                }else btnSend.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TODO update list chat
        refChatUid.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null){
                    String key = dataSnapshot.getKey();
                    refChatUid.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d("TAG date",dataSnapshot.child("date").getValue().toString());
                            String date = dataSnapshot.child("date").getValue().toString();
                            String content = dataSnapshot.child("content").getValue().toString();
                            boolean type = (boolean) dataSnapshot.child("type").getValue();
                            arrMessages.add(new Message(content,type,date));
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        lsv = (ListView) findViewById(R.id.lvMessage);
        etContent = (EditText) findViewById(R.id.edContent);
        btnSend = (Button) findViewById(R.id.btnSend);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
