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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zochat.R;
import com.zochat.models.Message;
import com.zochat.models.RoundedBitmapDrawable;

import java.util.ArrayList;

/**
 * Created by IT on 11/9/2017.
 */

public class AdapterCustomLV extends ArrayAdapter<Message> {

    private Context context;
    private int layout;
    private ArrayList<Message> arrMessage;
    Bitmap bmpFriend = null, myBmmp = null;

    public void setImage(String imgFriend, String myImg){
        Log.d("TAG imgFriend", imgFriend);
        Log.d("TAG myImg", myImg);
        byte[] bImgFriend = Base64.decode(imgFriend, Base64.DEFAULT);
        byte[] mybyte = Base64.decode(myImg, Base64.DEFAULT);
        bmpFriend = BitmapFactory.decodeByteArray(bImgFriend, 0, bImgFriend.length);
        myBmmp = BitmapFactory.decodeByteArray(mybyte, 0, mybyte.length);
    }

    public AdapterCustomLV(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Message> objects) {
        super(context, resource, objects);
        this.arrMessage = objects;
        this.layout = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,parent,false);

        TextView tvContent;
        ImageView imgV;
        LinearLayout linearLayout;
        Log.d("TAG message",arrMessage.get(position).toString());
        if(arrMessage.get(position).isType()){
            tvContent = (TextView) convertView.findViewById(R.id.tvContentR);
            imgV = (ImageView) convertView.findViewById(R.id.imgvR);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.linearR);
            //imgV.setImageBitmap(Bitmap.createScaledBitmap(myBmmp, 24, 24,false));
            if(myBmmp != null){
                imgV.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(myBmmp, 120, 120,false)));
            }

        }else{
            tvContent = (TextView) convertView.findViewById(R.id.tvContentL);
            imgV = (ImageView) convertView.findViewById(R.id.imgvL);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.linearL);
            //imgV.setImageBitmap(Bitmap.createScaledBitmap(bmpFriend, 24, 24,false));
            if(bmpFriend != null){
                imgV.setImageDrawable(RoundedBitmapDrawable.createRoundedBitmap(Bitmap.createScaledBitmap(bmpFriend, 120, 120,false)));
            }

        }
        tvContent.setText(arrMessage.get(position).getContent());
        linearLayout.setVisibility(View.VISIBLE);

        return convertView;
    }
}
