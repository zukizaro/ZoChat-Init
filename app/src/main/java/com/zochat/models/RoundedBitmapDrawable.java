package com.zochat.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by zukizaro on 19/11/2017.
 */

public class RoundedBitmapDrawable {
    public static android.support.v4.graphics.drawable.RoundedBitmapDrawable createRoundedBitmap(Bitmap srcBitmap){
        // Initialize a new Paint instance
        Paint paint = new Paint();

        // Get source bitmap width and height
        int srcBitmapWidth = srcBitmap.getWidth();
        int srcBitmapHeight = srcBitmap.getHeight();

                /*
                    IMPORTANT NOTE : You should experiment with border and shadow width
                    to get better circular ImageView as you expected.
                    I am confused about those size.
                */
        // Define border and shadow width
        int borderWidth = 15;
        int shadowWidth = 8;

        // destination bitmap width
        int dstBitmapWidth = Math.min(srcBitmapWidth,srcBitmapHeight)+borderWidth*2;
        //float radius = Math.min(srcBitmapWidth,srcBitmapHeight)/2;

        // Initializing a new bitmap to draw source bitmap, border and shadow
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);

        // Initialize a new canvas
        Canvas canvas = new Canvas(dstBitmap);

        // Draw a solid color to canvas
        canvas.drawColor(Color.WHITE);

        // Draw the source bitmap to destination bitmap by keeping border and shadow spaces
        canvas.drawBitmap(srcBitmap, (dstBitmapWidth - srcBitmapWidth) / 2, (dstBitmapWidth - srcBitmapHeight) / 2, null);

        // Use Paint to draw border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth * 2);
        paint.setColor(Color.WHITE);

        // Draw the border in destination bitmap
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, paint);

        // Use Paint to draw shadow
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(shadowWidth);

        // Draw the shadow on circular bitmap
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()/2,paint);

                /*
                    RoundedBitmapDrawable
                        A Drawable that wraps a bitmap and can be drawn with rounded corners. You
                        can create a RoundedBitmapDrawable from a file path, an input stream, or
                        from a Bitmap object.
                */
        // Initialize a new RoundedBitmapDrawable object to make ImageView circular
        android.support.v4.graphics.drawable.RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), dstBitmap);

                /*
                    setCircular(boolean circular)
                        Sets the image shape to circular.
                */
        // Make the ImageView image to a circular image
        roundedBitmapDrawable.setCircular(true);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
        roundedBitmapDrawable.setAntiAlias(true);
        return roundedBitmapDrawable;
    }
}
