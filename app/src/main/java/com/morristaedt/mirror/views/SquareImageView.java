package com.morristaedt.mirror.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by orion on 9/23/15.
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int oWidth = MeasureSpec.getSize(widthMeasureSpec);
        int oHeight = MeasureSpec.getSize(heightMeasureSpec);

        int finalLength = Math.min(oWidth, oHeight);

//        super.onMeasure(
//                MeasureSpec.makeMeasureSpec(finalLength, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(finalLength, MeasureSpec.EXACTLY)
//        );
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
