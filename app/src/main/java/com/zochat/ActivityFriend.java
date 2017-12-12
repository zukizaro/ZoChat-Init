package com.zochat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zochat.adapters.AdapterFriendNotification;
import com.zochat.adapters.AdapterListFriend;
import com.zochat.adapters.AdapterListFriendSearch;
import com.zochat.models.Friend;
import com.zochat.models.NotificationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityFriend extends AppCompatActivity {
    final String NOTIFICATION = "notification";
    final String FRIENDS = "friends";

    Menu menu;
    MenuItem itemNoti;
    DrawerLayout drawerNotifi;
    ListView lvFriend, lvNotifi, lvSearch;
    ActionBarDrawerToggle toggle;
    FrameLayout frameLayout;
    LinearLayout lnSearch;
    EditText etSearch;
    Button btnSearch;
    TextView tvNoresult;

    AdapterFriendNotification adapterFriendNotification;
    AdapterListFriend adapterListFriend;
    AdapterListFriendSearch adapterListFriendSearch;
    ArrayList<NotificationItem> notificationItemArrayList;
    ArrayList<Friend> friendArrayList, searchListFriend;

    ArrayList<String> arrMyFriends;
    ArrayList<Friend> arrAllUser;
    //String arrFriends[], strFriends = "";

    DatabaseReference myRef, drInfoFriend, drListFriends;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        lvSearch = (ListView) findViewById(R.id.lvSearch);
        lvFriend = (ListView) findViewById(R.id.lvFriend);
        lvNotifi = (ListView) findViewById(R.id.lvNotifi);
        lnSearch = (LinearLayout) findViewById(R.id.lnSearch);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvNoresult = (TextView) findViewById(R.id.tvNoresult);

        drawerNotifi = (DrawerLayout) findViewById(R.id.drawerNotifi);
        toggle = new ActionBarDrawerToggle(ActivityFriend.this, drawerNotifi, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerNotifi.setDrawerListener(toggle);
        toggle.syncState();

        notificationItemArrayList = new ArrayList<>();
        friendArrayList = new ArrayList<>();
        searchListFriend = new ArrayList<>();

        adapterFriendNotification = new AdapterFriendNotification(ActivityFriend.this, R.layout.layout_item_notification, notificationItemArrayList);
        lvNotifi.setAdapter(adapterFriendNotification);

        adapterListFriend = new AdapterListFriend(ActivityFriend.this,R.layout.layout_item_friend,friendArrayList);
        lvFriend.setAdapter(adapterListFriend);

        adapterListFriendSearch = new AdapterListFriendSearch(ActivityFriend.this, R.layout.layout_item_search, searchListFriend);
        lvSearch.setAdapter(adapterListFriendSearch);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("listuser").child(user.getUid()).child(NOTIFICATION);
        drInfoFriend = database.getReference("listuser");
        drListFriends = database.getReference("listuser").child(user.getUid()).child(FRIENDS);

        arrMyFriends = new ArrayList<>();
        arrAllUser = new ArrayList<>();
        getAllUser();
        actionNotification();

        actionListFriend();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchListFriend.clear();
                adapterListFriendSearch.notifyDataSetChanged();
                tvNoresult.setVisibility(View.GONE);
                lvSearch.setVisibility(View.VISIBLE);
                actionSearchFriend();
                if(searchListFriend.size() == 0){
                    tvNoresult.setVisibility(View.VISIBLE);
                    lvSearch.setVisibility(View.GONE);
                }
            }
        });

        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityFriend.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",friendArrayList.get(i).getEmail());
                bundle.putString("uid",friendArrayList.get(i).getUid());
                bundle.putString("img",friendArrayList.get(i).getImg());
                intent.putExtra("bundle",bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                //finish();
            }
        });

    }

    private void getAllUser() {
        drInfoFriend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals(null)){
                    final String uid = dataSnapshot.getKey();
                    drInfoFriend.child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                Log.d("TAG uid", searchListFriend.size()+"");
                                arrAllUser.add(new Friend("",dataSnapshot.getValue().toString(),uid));
                            }
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

    private void actionSearchFriend() {
        for(Friend f : arrAllUser){
            if(!arrMyFriends.contains(f.getUid()) && etSearch.getText().toString().equals(f.getEmail())){
                searchListFriend.add(f);
            }
        }
        adapterListFriendSearch.notifyDataSetChanged();
        /*drInfoFriend.child("listuid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String listuid[] = dataSnapshot.getValue(String.class).split(";");
                for (String uid : listuid){
                    if(!uid.equals(user.getUid()) && !strFriends.contains(uid)){
                        addInfoFriendSearch(uid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void addInfoFriendSearch(final String uid) {
        /*drInfoFriend.child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    Friend friend = new Friend();
                    friend.setEmail(dataSnapshot.getValue().toString());
                    friend.setUid(uid);
                    searchListFriend.add(friend);
                    adapterListFriendSearch.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        drInfoFriend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("TAG searchfriend",dataSnapshot.getChildren().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void actionListFriend() {
        /*drListFriends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendArrayList.clear();
                adapterListFriend.notifyDataSetChanged();
                if(dataSnapshot.getValue() != null) {
                    strFriends = dataSnapshot.getValue().toString();
                    arrFriends = strFriends.split(";");
                    for (String uid : arrFriends) {
                        addInfoFriend(uid);
                        Log.d("TAG friend uid", ": " + uid);
                    }
                }
                //adapterListFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        drListFriends.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null){
                    String key = dataSnapshot.getKey().toString();
                    drListFriends.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            addInfoFriend(dataSnapshot.getValue().toString());
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

    private void addInfoFriend(final String uid) {
        drInfoFriend.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("email").getValue()!= null){
                    Friend friend = new Friend();
                    friend.setUid(uid);
                    friend.setEmail(dataSnapshot.child("email").getValue().toString());
                    if(dataSnapshot.child("image").getValue() != null){
                        friend.setImg(dataSnapshot.child("image").getValue().toString());
                    }
                    friendArrayList.add(friend);
                    arrMyFriends.add(uid);
                }
                adapterListFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void actionNotification() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                itemNoti.setIcon(R.drawable.ic_add_alert_green_24dp);

                Log.d("TAG get uid", ": " + dataSnapshot.getKey().toString() + "; " + s);
                addInfoFriendNotifi(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeItemNotifi(dataSnapshot.getKey().toString());
                if(notificationItemArrayList.size() == 0){
                    itemNoti.setIcon(R.drawable.ic_add_alert_24dp);
                }
                Log.d("TAG remove child", ": " + dataSnapshot.getKey().toString());
                adapterFriendNotification.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeItemNotifi(String uid){
        for(int i = 0; i < notificationItemArrayList.size(); i++){
            if(notificationItemArrayList.get(i).getUid().equals(uid)){
                Log.d("TAG remove item", ": " + notificationItemArrayList.get(i).toString());
                notificationItemArrayList.remove(i);
                break;
            }
        }
    }

    private void addInfoFriendNotifi(String uid){
        final NotificationItem item = new NotificationItem();
        item.setUid(uid);
        drInfoFriend.child(uid).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                item.setEmail(dataSnapshot.getValue().toString());
                Log.d("TAG email", ": " + item.toString());
                notificationItemArrayList.add(item);
                adapterFriendNotification.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_activity_friend, menu);
        itemNoti = menu.findItem(R.id.menuNotify);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddFriend:
                Animation anim_leftRightIn = AnimationUtils.loadAnimation(this,R.anim.anim_left_to_right_in);
                if (drawerNotifi.isDrawerOpen(frameLayout) && lnSearch.getVisibility() == View.VISIBLE) {
                    drawerNotifi.closeDrawer(frameLayout);
                } else if(drawerNotifi.isDrawerOpen(frameLayout) && lnSearch.getVisibility() == View.GONE) {
                    lnSearch.setAnimation(anim_leftRightIn);
                    lnSearch.setVisibility(View.VISIBLE);
                    lvNotifi.setVisibility(View.GONE);
                }else if(!drawerNotifi.isDrawerOpen(frameLayout) && lnSearch.getVisibility() == View.GONE){
                    lnSearch.setVisibility(View.VISIBLE);
                    lvNotifi.setVisibility(View.GONE);
                    drawerNotifi.openDrawer(frameLayout);
                }else if(!drawerNotifi.isDrawerOpen(frameLayout) && lnSearch.getVisibility() == View.VISIBLE){
                    drawerNotifi.openDrawer(frameLayout);
                }
                break;
            case R.id.menuSetting:
                Intent intent = new Intent(ActivityFriend.this, ActivityDetailUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.menuNotify:
                Animation anim_leftRightOut = AnimationUtils.loadAnimation(this,R.anim.anim_right_to_left_out);
                if (drawerNotifi.isDrawerOpen(frameLayout) && lvNotifi.getVisibility() == View.VISIBLE) {
                    drawerNotifi.closeDrawer(frameLayout);
                } else if(drawerNotifi.isDrawerOpen(frameLayout) && lvNotifi.getVisibility() == View.GONE){
                    lnSearch.setAnimation(anim_leftRightOut);
                    lnSearch.setVisibility(View.GONE);
                    lvNotifi.setVisibility(View.VISIBLE);
                }else if(!drawerNotifi.isDrawerOpen(frameLayout) && lvNotifi.getVisibility() == View.GONE){
                    lvNotifi.setVisibility(View.VISIBLE);
                    lnSearch.setVisibility(View.GONE);
                    drawerNotifi.openDrawer(frameLayout);
                }else if(!drawerNotifi.isDrawerOpen(frameLayout) && lvNotifi.getVisibility() == View.VISIBLE){
                    drawerNotifi.openDrawer(frameLayout);
                }
                break;
        }
        //if(toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
}
