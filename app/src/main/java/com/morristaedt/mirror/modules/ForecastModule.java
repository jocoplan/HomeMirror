package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.morristaedt.mirror.R;
import com.morristaedt.mirror.requests.ForecastRequest;
import com.morristaedt.mirror.requests.ForecastResponse;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class ForecastModule {

    public interface ForecastListener {
        void onWeatherNow(ForecastResponse.DataPoint now);
        void onWeatherToday(ForecastResponse.DataPoint today);
        void onWeatherTomorrow(ForecastResponse.DataPoint tomorrow);
    }

    public static void getHourlyForecast(final Resources resources, final double lat, final double lon, final ForecastListener listener) {
        new AsyncTask<Void, Void, ForecastResponse>() {

            @Override
            protected ForecastResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(resources.getString(R.string.forecast_endpoint))
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "Forecast error: " + cause);
                                return null;
                            }
                        })
                        .build();

                ForecastRequest service = restAdapter.create(ForecastRequest.class);
                String excludes = "alerts,minutely,flags";
                String units = "us";
                Log.d("mirror", "backgrounddd");
                return service.getHourlyForecast(resources.getString(R.string.dark_sky_api_key), lat, lon, excludes, units);
            }

            @Override
            protected void onPostExecute(ForecastResponse forecastResponse) {
                if (forecastResponse != null) {
                    listener.onWeatherNow(forecastResponse.currently);

                    if (forecastResponse.daily != null
                            && forecastResponse.daily.data != null
                            && forecastResponse.daily.data.size() >= 1)
                        listener.onWeatherToday(forecastResponse.daily.data.get(0));
                    else
                        listener.onWeatherToday(null);

                    if (forecastResponse.daily != null
                            && forecastResponse.daily.data != null
                            && forecastResponse.daily.data.size() >= 2)
                        listener.onWeatherTomorrow(forecastResponse.daily.data.get(1));
                    else
                        listener.onWeatherTomorrow(null);
                }
            }
        }.execute();

    }
}
