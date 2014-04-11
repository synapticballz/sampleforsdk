package com.deviantart.sdksample.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.deviantart.android.sdk.api.DVNTAsyncAPI;

/**
 * Base Activity handling the opening & closing of the OAuth Session.
 *
 * Created by drommk on 4/9/14.
 */
public class SampleBaseActivity  extends Activity{

    private static final String TAG = "SampleActivity";
    private static final String SAMPLE_SCOPE = "basic daprivate";
    public static final int SAMPLE_APP_DEVIATION_ID = 416026638;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DVNTAsyncAPI.start(this, getApiKey(), getApiSecret(), getSampleScope());
    }

    public String getApiKey() {
        Log.e(TAG, "Using SDK default API KEY : please override getApiKey() in this activity");
        return "637";
    }

    public String getApiSecret() {
        Log.e(TAG, "Using SDK default API SECRET : : please override getApiSecret() in this activity");
        return "80c93b54058da2a62433ce547f8b7563";
    }

    public String getSampleScope() {
        return SAMPLE_SCOPE;
    }

    @Override
    protected void onDestroy() {
        DVNTAsyncAPI.stop(this);
        super.onDestroy();
    }
}
