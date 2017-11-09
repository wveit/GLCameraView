package com.example.androidu.glcamera.ar_framework.graphics3d.billboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.androidu.glcamera.ar_framework.graphics3d.helper.*;

public class BillboardMaker {
    public static SizedBillboard make(Context context, int scale, int iconResourceId, String title, String text){
        SizedBillboard billboard = new SizedBillboard();
        billboard.setScale(scale);

        Bitmap bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);

        canvas.drawARGB(255, 200, 200, 200);

        Bitmap icon = TextureHelper.bitmapFromResource(context, iconResourceId);
        canvas.drawBitmap(icon, new Rect(0, 0, 512, 512), new Rect(20, 60, 100, 140), paint);

        paint.setTextSize(30);
        paint.setFakeBoldText(true);
        canvas.drawText(title, 120, 80, paint);

        paint.setFakeBoldText(false);
        paint.setTextSize(20);
        canvas.drawText(text, 120, 120, paint);

        billboard.setBitmap(bitmap);
        bitmap.recycle();
        icon.recycle();

        return billboard;
    }
}
