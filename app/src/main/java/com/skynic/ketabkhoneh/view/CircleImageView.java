package com.skynic.ketabkhoneh.view;
//this is java class
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap fullSizeBitmap = drawable.getBitmap();

        int scaledWidth = getMeasuredWidth();
        int scaledHeight = getMeasuredHeight();

        Bitmap mScaledBitmap;
        if (scaledWidth == fullSizeBitmap.getWidth()
                && scaledHeight == fullSizeBitmap.getHeight()) {
            mScaledBitmap = fullSizeBitmap;
        } else {
            mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap,
                    scaledWidth, scaledHeight, true /* filter */);
        }

        // Bitmap roundBitmap = getRoundedCornerBitmap(mScaledBitmap);

        // Bitmap roundBitmap = getRoundedCornerBitmap(getContext(),
        // mScaledBitmap, 10, scaledWidth, scaledHeight, false, false,
        // false, false);
        // canvas.drawBitmap(roundBitmap, 0, 0, null);

        Bitmap circleBitmap = getCircledBitmap(mScaledBitmap);

        Bitmap result = Bitmap.createBitmap(circleBitmap.getWidth(),
                circleBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        c.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        //canvas.drawRect(0, 0, result.getWidth(), result.getHeight(),paint);
        c.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2,
                circleBitmap.getHeight() / 2, paint);
        Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
        Rect rect2 = new Rect(10, 10, circleBitmap.getWidth() - 10, circleBitmap.getHeight() - 10);
        c.drawBitmap(circleBitmap, rect, rect2, paint);
        circleBitmap = getCircledBitmap(result);

        result = Bitmap.createBitmap(circleBitmap.getWidth(),
                circleBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(result);
        paint = new Paint();
        paint.setAntiAlias(true);
        c.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFFFF588B);
        //canvas.drawRect(0, 0, result.getWidth(), result.getHeight(),paint);
        c.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2,
                circleBitmap.getHeight() / 2, paint);
        rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
        rect2 = new Rect(10, 10, circleBitmap.getWidth() - 10, circleBitmap.getHeight() - 10);
        c.drawBitmap(circleBitmap, rect, rect2, paint);
        circleBitmap = getCircledBitmap(result);

        canvas.drawBitmap(circleBitmap, 0, 0, null);

    }

    Bitmap getCircledBitmap(Bitmap bitmap) {

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);

        int color = Color.BLUE;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getHeight() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return result;
    }

}