package com.morristaedt.mirror.modules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class BirthdayModule {

    private static HashMap<String, String> mBirthdayMap;

    static {
        mBirthdayMap = new HashMap<>();
        mBirthdayMap.put("08/16", "Justin");
        mBirthdayMap.put("10/4", "Danielle");
    }

    public static String getBirthday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/d", Locale.US);
        return mBirthdayMap.get(simpleDateFormat.format(new Date()));
    }
}
