package com.morristaedt.mirror.requests;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class ForecastResponse {

    float latitude;
    float longitude;

    public DataPoint currently;
    public DataBlock hourly;
    public DataBlock daily;

    public class DataBlock {
        public ArrayList<DataPoint> data;
    }

    public class DataPoint {
        public long time; // in seconds
        public String summary;
        public String icon;
        public String precipType;
        public float precipProbability;
        public float temperature;
        public float temperatureMin;
        public float temperatureMax;

        public Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time * 1000);
            return calendar;
        }

        private String toDisplay(float t)
        {
            return String.valueOf(Math.round(t)) + (char) 0x00B0;
        }

        public String getDisplayTemperature() {
            return toDisplay(temperature);
        }

        public String getHighTemperature() {
            return toDisplay(temperatureMax);
        }

        public String getLowTemperature() {
            return toDisplay(temperatureMin);
        }
    }
}
