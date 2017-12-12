package com.zochat.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zochat.R;
import com.zochat.models.NotificationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by zukizaro on 18/11/2017.
 */

public class AdapterFriendNotification extends ArrayAdapter<NotificationItem> {

    Context context;
    int layout;
    ArrayList<NotificationItem> arrayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef, friendRef;
    String myfriends = "", friends = "";

    public AdapterFriendNotification(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NotificationItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.arrayList = objects;
        myRef = database.getReference("listuser").child(user.getUid()).child("friends");
        friendRef = database.getReference("listuser");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        Button btnNhan = (Button) convertView.findViewById(R.id.btnNhan);

        tvTitle.setText(arrayList.get(position).getEmail() + " " + context.getResources().getString(R.string.ketBan));
        Log.d("TAG item", ": " + arrayList.get(position).toString());
        btnNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFriends(arrayList.get(position).getUid());
                //sendFriends(arrayList.get(position).getUid());
                myRef.push().setValue(arrayList.get(position).getUid());
                friendRef.child(arrayList.get(position).getUid()).child("friends").push().setValue(user.getUid());
                database.getReference("listuser").child(user.getUid()).child("notification").child(arrayList.get(position).getUid()).removeValue();
            }
        });

        /*friendRef.child(arrayList.get(position).getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends = "";
                if(dataSnapshot.getValue() != null){
                    friends = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    myfriends = dataSnapshot.getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        return convertView;
    }

    /*private void sendFriends(String uidSendFriend) {
        if (!friends.equals("")) {
            friendRef.child(uidSendFriend).child("friends").setValue(friends + ";" + user.getUid());
        } else {
            friendRef.child(uidSendFriend).child("friends").setValue(user.getUid());
        }
    }

    void getFriends(final String uid) {
        if (!myfriends.equals("")) {
            myRef.setValue(myfriends + ";" + uid);
        } else {
            myRef.setValue(uid);
        }
        database.getReference("listuser").child(user.getUid()).child("notification").child(uid).removeValue();
        Log.d("TAG friends ", ": " + friends);
        myfriends = "";
    }*/
}
