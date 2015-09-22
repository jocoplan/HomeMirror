package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.morristaedt.mirror.R;
import com.morristaedt.mirror.requests.StravaActivity;
import com.morristaedt.mirror.requests.StravaRequest;

import org.joda.time.DateTime;

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
                                Log.w("mirror", "Forecast error: " + cause);
                                return null;
                            }
                        })
                        .build();

                DateTime dateTime = new DateTime();
                dateTime = dateTime.withTime(0, 0, 0, 0); // Roll the time to the start of the day
                dateTime = dateTime.minusDays(7); // One week ago

                final long after = dateTime.toInstant().getMillis();

                StravaRequest service = restAdapter.create(StravaRequest.class);
                return service.getActivitiesSince(athlete_token, after);
            }

            @Override
            protected void onPostExecute(List<StravaActivity> stravaResponse) {
                if (stravaResponse != null) {

                }
            }
        }.execute();

    }
}
