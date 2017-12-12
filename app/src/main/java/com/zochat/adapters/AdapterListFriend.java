package com.zochat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zochat.R;
import com.zochat.models.Friend;
import com.zochat.models.RoundedBitmapDrawable;

import java.util.ArrayList;

/**
 * Created by zukizaro on 18/11/2017.
 */

public class AdapterListFriend extends ArrayAdapter<Friend> {

    Context context;
    int layout;
    ArrayList<Friend> friends;

    public AdapterListFriend(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Friend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imgUser = convertView.findViewById(R.id.img);

        if(!friends.get(position).getImg().equals("")){
            byte[] bImg = Base64.decode(friends.get(position).getImg(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
            //imgUser.setImageBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false));
            imgUser.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(bmp, 240, 240, false)));
        }

        tvName.setText(friends.get(position).getEmail());

        Log.d("TAG friend", ": " + friends.get(position).getEmail());
        return  convertView;
    }
}
