package com.morristaedt.mirror.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.morristaedt.mirror.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by orion on 9/18/15.
 */
public class WeatherSubView extends LinearLayout {

    private static Map<String,String> iconMapping;
    static {
        iconMapping = new HashMap<>();
        iconMapping.put("wi-forecast-io-clear-day", "\uf00d"); //day-sunny
        iconMapping.put("wi-forecast-io-clear-night", "\uf02e"); //night-clear
        iconMapping.put("wi-forecast-io-rain", "\uf019"); //rain
        iconMapping.put("wi-forecast-io-snow", "\uf01b"); //snow
        iconMapping.put("wi-forecast-io-sleet", "\uf0b5"); //sleet
        iconMapping.put("wi-forecast-io-wind", "\uf050"); //strong-wind
        iconMapping.put("wi-forecast-io-fog", "\uf014"); //fog
        iconMapping.put("wi-forecast-io-cloudy", "\uf013"); //cloudy
        iconMapping.put("wi-forecast-io-partly-cloudy-day", "\uf002"); //day-cloudy
        iconMapping.put("wi-forecast-io-partly-cloudy-night", "\uf031"); //night-cloudy
        iconMapping.put("wi-forecast-io-hail", "\uf015"); //hail
        iconMapping.put("wi-forecast-io-thunderstorm", "\uf01e"); //thunderstorm
        iconMapping.put("wi-forecast-io-tornado", "\uf056"); //tornado
    }

    public TextView icon;
    public TextView text;

    public WeatherSubView(Context context)
    {
        super(context);
        init(context, null, 0);
    }

    public WeatherSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WeatherSubView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View.inflate(context, R.layout.weather_sub_view, this);
        icon = (TextView) findViewById(R.id.weather_icon);
        text = (TextView) findViewById(R.id.weather_text);

        int[] styleList = {android.R.attr.textSize};
        // Set the style across both TextFields
        TypedArray style = context.getTheme().obtainStyledAttributes(
                attrs,
                styleList,
                defStyle,
                0
        );
        int size = style.getDimensionPixelSize(0, 12);
        icon.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/weathericons-regular-webfont.ttf");
        icon.setTypeface(font);
    }

    public void setIcon(String iconStr)
    {
        String iconCode = iconMapping.get("wi-forecast-io-" + iconStr);
        if (iconCode != null)
        {
            icon.setText(iconCode);
            icon.setVisibility(View.VISIBLE);
        }
        else
            icon.setVisibility(View.INVISIBLE);
    }

    public void setText(String textStr)
    {
        if (!TextUtils.isEmpty(textStr)) {
            text.setText(textStr);
            text.setVisibility(View.VISIBLE);
        }
        else
            text.setVisibility(View.INVISIBLE);
    }

    public void setIconAndText(String iconStr, String textStr) {
        setIcon(iconStr);
        setText(textStr);
        this.setVisibility(
                text.getVisibility() == View.VISIBLE || icon.getVisibility() == View.VISIBLE ?
                        View.VISIBLE : View.INVISIBLE
        );
    }
}
