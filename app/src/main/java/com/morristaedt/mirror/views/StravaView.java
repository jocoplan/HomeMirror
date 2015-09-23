package com.morristaedt.mirror.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morristaedt.mirror.R;

/**
 * Created by orion on 9/18/15.
 */
public class StravaView extends LinearLayout {

    public ImageView icon;
    public TextView text;

    public StravaView(Context context)
    {
        super(context);
        init(context, null, 0);
    }

    public StravaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StravaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View.inflate(context, R.layout.strava_view, this);
        icon = (ImageView) findViewById(R.id.strava_icon);
        text = (TextView) findViewById(R.id.strava_text);

        int[] styleList = {android.R.attr.textSize};
        // Set the style to TextView
        TypedArray style = context.getTheme().obtainStyledAttributes(
                attrs,
                styleList,
                defStyle,
                0
        );
        int size = style.getDimensionPixelSize(0, 12);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        style = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StravaView,
                defStyle,
                0
        );

        String type = style.getString(R.styleable.StravaView_activity_type);

        if ("bicycle".equals(type))
            icon.setImageResource(R.drawable.bicycle);
        else
            icon.setImageResource(R.drawable.runner);
    }

    public void setDistance(float distance)
    {
        text.setText( String.format("%.2f mi", distance) );
    }
}
