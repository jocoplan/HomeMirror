package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.morristaedt.mirror.R;
import com.morristaedt.mirror.requests.StravaActivity;
import com.morristaedt.mirror.requests.StravaRequest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by orion on 9/21/15.
 */
public class StravaModule {

    public interface StravaListener
    {
        void receiveWeeklyActivities(List<StravaActivity> activities);
    }

    public static void getNineDayCycle(final Resources resources, final String athlete_token, final StravaListener listener) {
        new AsyncTask<Void, Void, List<StravaActivity>>() {

            @Override
            protected List<StravaActivity> doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(resources.getString(R.string.strava_endpoint))
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "Strava error: " + cause);
                                return null;
                            }
                        })
                        .build();

                DateTime today = new DateTime()
                                    .withTime(0, 0, 0, 0); // Roll the time to the start of the day;

                DateTime dateTime = today
                                    .withDate(2016, 6, 28);

                while ( today.isAfter(dateTime.plusDays(8)) )
                    dateTime = dateTime.plusDays(9);

                //  dateTime = dateTime.minusDays(7); // One week ago

                final long after = dateTime.toInstant().getMillis() / 1000; // After is in seconds after the UNIX epoch


                StravaRequest service = restAdapter.create(StravaRequest.class);
                return service.getActivitiesSince(athlete_token, after);
            }

            @Override
            protected void onPostExecute(List<StravaActivity> stravaResponse) {
                if (stravaResponse != null) {
                    listener.receiveWeeklyActivities(stravaResponse);
                }
            }
        }.execute();

    }

    public static void getWeeklyExercise(final Resources resources, final String athlete_token, final StravaListener listener) {
        new AsyncTask<Void, Void, List<StravaActivity>>() {

            @Override
            protected List<StravaActivity> doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(resources.getString(R.string.strava_endpoint))
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "Strava error: " + cause);
                                return cause;
                            }
                        })
                        .build();

                DateTime dateTime = new DateTime();
                dateTime = dateTime.withTime(0, 0, 0, 0); // Roll the time to the start of the day
                dateTime = dateTime.withDayOfWeek(DateTimeConstants.MONDAY);
                //  dateTime = dateTime.minusDays(7); // One week ago

                final long after = dateTime.toInstant().getMillis() / 1000; // After is in seconds after the UNIX epoch


                StravaRequest service = restAdapter.create(StravaRequest.class);
                try {
                    return service.getActivitiesSince(athlete_token, after);
                }catch ( RetrofitError e )
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<StravaActivity> stravaResponse) {
                if (stravaResponse != null) {
                    listener.receiveWeeklyActivities(stravaResponse);
                }
            }
        }.execute();

    }
}
