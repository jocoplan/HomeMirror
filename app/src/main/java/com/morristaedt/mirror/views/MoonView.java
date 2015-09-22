package com.morristaedt.mirror.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by orion on 9/19/15.
 */
public class MoonView extends TextView {

    public MoonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public MoonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MoonView(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
//        View.inflate(context, R.layout.moon_view, null);

        int[] styleList = {android.R.attr.textSize};
        // Set the style across both TextFields
        TypedArray style = context.getTheme().obtainStyledAttributes(
                attrs,
                styleList,
                defStyle,
                0
        );
        int size = style.getDimensionPixelSize(0, 12);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/weathericons-regular-webfont.ttf");
        this.setTypeface(font);
    }

    public void setMoonPhase(float moonPhase)
    {
        byte intMapping = (byte) (Math.round(moonPhase * 28) % 28);
        int baseUnicode = 0xf095;
        int icon = (baseUnicode + intMapping);
        setText( String.valueOf(Character.toChars(icon)) );
    }
}
