package com.morristaedt.mirror;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.morristaedt.mirror.modules.BirthdayModule;
import com.morristaedt.mirror.modules.DayModule;
import com.morristaedt.mirror.modules.ForecastModule;
import com.morristaedt.mirror.modules.StravaModule;
import com.morristaedt.mirror.modules.StravaModule.StravaListener;
import com.morristaedt.mirror.modules.XKCDModule;
import com.morristaedt.mirror.requests.ForecastResponse;
import com.morristaedt.mirror.requests.StravaActivity;
import com.morristaedt.mirror.views.MoonView;
import com.morristaedt.mirror.views.WeatherSubView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MirrorActivity extends AppCompatActivity {

    private static final boolean DEMO_MODE = false;

    private TextView mBirthdayText;
    private TextView mDayText;
    private TextView mMilesRan;
    private TextView mMilesBiked;
    private MoonView moonView;
    private WeatherSubView mWeatherNow;
    private WeatherSubView mWeatherToday;
    private WeatherSubView mWeatherTomorrow;
    private ImageView mXKCDImage;

    private XKCDModule.XKCDListener mXKCDListener = new XKCDModule.XKCDListener() {
        @Override
        public void onNewXKCDToday(String url) {
            if (TextUtils.isEmpty(url)) {
                mXKCDImage.setVisibility(View.GONE);
            } else {
                Picasso.with(MirrorActivity.this).load(url).into(mXKCDImage);
                mXKCDImage.setVisibility(View.VISIBLE);
            }
        }
    };

    private StravaListener stravaListener = new StravaListener() {
        @Override
        public void receiveWeeklyActivities(List<StravaActivity> activities) {
            float totalRunMeters = 0;
            float totalBikeMeters = 0;
            for (StravaActivity activity : activities)
            {
                if ("Ride".equals(activity.type))
                    totalBikeMeters += activity.distance;
                if ("Run".equals(activity.type))
                    totalRunMeters += activity.distance;
            }

            // Convert meters to miles
            float totalRunMiles = totalRunMeters / 1609.34f;
            float totalBikeMiles = totalBikeMeters / 1609.34f;

            mMilesRan.setText(String.format("Ran: %.2f mi", totalRunMiles));
            mMilesBiked.setText(String.format("Biked: %.2f mi", totalBikeMiles));
        }
    };

    private ForecastModule.ForecastListener mForecastListener = new ForecastModule.ForecastListener() {
        @Override
        public void onWeatherNow(ForecastResponse.DataPoint now) {
            if (now == null) {
                mWeatherNow.setVisibility(View.INVISIBLE);
                return;
            }

            mWeatherNow.setIconAndText(now.icon, now.getDisplayTemperature());
        }

        public void doHighLowIcon(WeatherSubView view, ForecastResponse.DataPoint day)
        {
            if (day == null)
            {
                view.setVisibility(View.INVISIBLE);
                return;
            }


            String display = String.format("%s / %s",
                    day.getHighTemperature(),
                    day.getLowTemperature());
            display = display.trim();

            view.setIconAndText(day.icon, display);
        }

        @Override
        public void onWeatherToday(ForecastResponse.DataPoint today) {
            doHighLowIcon(mWeatherToday, today);
            moonView.setMoonPhase(today.moonPhase);
        }

        @Override
        public void onWeatherTomorrow(ForecastResponse.DataPoint tomorrow) {
            doHighLowIcon(mWeatherTomorrow, tomorrow);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mBirthdayText = (TextView) findViewById(R.id.birthday_text);
        mDayText = (TextView) findViewById(R.id.day_text);
        moonView = (MoonView) findViewById(R.id.moon_view);
        mWeatherNow = (WeatherSubView) findViewById(R.id.weather_now);
        mWeatherToday = (WeatherSubView) findViewById(R.id.weather_today);
        mWeatherTomorrow = (WeatherSubView) findViewById(R.id.weather_tomorrow);
        mXKCDImage = (ImageView) findViewById(R.id.xkcd_image);
        mMilesRan = (TextView) findViewById(R.id.miles_ran);
        mMilesBiked = (TextView) findViewById(R.id.miles_biked);

        //Negative of XKCD image
        float[] colorMatrixNegative = {
                -1.0f, 0, 0, 0, 255, //red
                0, -1.0f, 0, 0, 255, //green
                0, 0, -1.0f, 0, 255, //blue
                0, 0, 0, 1.0f, 0 //alpha
        };
        ColorFilter colorFilterNegative = new ColorMatrixColorFilter(colorMatrixNegative);
//        mXKCDImage.setColorFilter(colorFilterNegative); // not inverting for now

        setViewState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setViewState();
    }

    private void setViewState() {
        String birthday = BirthdayModule.getBirthday();
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayText.setVisibility(View.GONE);
        } else {
            mBirthdayText.setVisibility(View.VISIBLE);
            mBirthdayText.setText(getString(R.string.happy_birthday, birthday));
        }

        mDayText.setText(DayModule.getDay());

        ForecastModule.getHourlyForecast(getResources(),
                Float.parseFloat(getResources().getString(R.string.lat)),
                Float.parseFloat(getResources().getString(R.string.lon)),
                mForecastListener);

        XKCDModule.getXKCDForToday(mXKCDListener);

        StravaModule.getWeeklyExercise(getResources(),
                getResources().getString(R.string.danielle_token),
                stravaListener);
    }

    private void showDemoMode() {
        if (DEMO_MODE) {
        }
    }
}
