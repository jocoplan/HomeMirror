package com.morristaedt.mirror.requests;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * https://www.strava.com/api/v3/athlete/activities
 * http://strava.github.io/api/v3/activities/#get-activities
 * Created by orion on 9/19/15.
 */
public interface StravaRequest {
    @GET("/athlete/activities")
    List<StravaActivity> getActivitiesSince(@Query("access_token") String access_token, @Query("after") long after);
}
