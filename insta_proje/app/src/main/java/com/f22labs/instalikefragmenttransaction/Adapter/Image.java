package com.f22labs.instalikefragmenttransaction.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class Image extends AppCompatImageView {
    public Image(Context context) {
        super(context);
    }

    public Image(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Image(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
