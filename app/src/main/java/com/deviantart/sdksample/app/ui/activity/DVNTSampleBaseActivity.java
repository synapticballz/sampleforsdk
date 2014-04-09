package com.deviantart.sdksample.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wonderlands.sdk.api.DVNTAsyncAPI;
import com.wonderlands.sdk.api.DVNTBrowseMode;
import com.wonderlands.sdk.api.listener.DVNTAsyncRequestListener;
import com.wonderlands.sdk.api.model.DVNTDeviationCommentsThread;
import com.wonderlands.sdk.api.model.DVNTDeviationInfo;
import com.wonderlands.sdk.api.model.DVNTDeviationStats;
import com.wonderlands.sdk.api.model.DVNTMoreLikeThisResults;
import com.wonderlands.sdk.api.model.DVNTPlacebo;
import com.wonderlands.sdk.api.model.DVNTUserInfo;
import com.wonderlands.sdk.oauth.DVNTOAuth;

/**
 * Sample Activity for SDK.
 * <p/>
 * Shows standard OAuth session opening/closing + API calls.
 * <p/>
 * //TODO put this sample in its own module.
 * <p/>
 * Created by drommk on 11/21/13.
 */
public class DVNTSampleBaseActivity extends Activity {

    private static final String TAG = "DVNTSampleBaseActivity";
    private static final String SAMPLE_SCOPE = "basic daprivate";
    public static final int APP_DEVIATION_ID = 416026638;

    public String getApiKey() {
        Log.e(TAG, "Using SDK default API KEY : please override getApiKey() in this activity");
        return "637";
    }

    public String getApiSecret() {
        Log.e(TAG, "Using SDK default API SECRET : : please override getApiSecret() in this activity");
        return "80c93b54058da2a62433ce547f8b7563";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DVNTOAuth.getSession(this, getApiKey(), getApiSecret(), SAMPLE_SCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!DVNTOAuth.isReady()) {
            Log.d(TAG, "onResume : not ready");
            return;
        }

        DVNTAsyncAPI.with(this).placebo(new DVNTAsyncRequestListener<DVNTPlacebo>() {
            @Override
            public void onSuccess(DVNTPlacebo placeboResult) {
                String text = "access token";
                if ("success".equals(placeboResult.getStatus())) {
                    text += " is correct";
                } else {
                    text += " is incorrect";
                }

                Toast.makeText(DVNTSampleBaseActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "PLACEBO ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).whoAmI(new DVNTAsyncRequestListener<DVNTUserInfo>() {
            @Override
            public void onSuccess(DVNTUserInfo userInfo) {
                Toast.makeText(DVNTSampleBaseActivity.this, "I am " + userInfo.getUserName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "WhoAmI ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).browse(DVNTBrowseMode.POPULAR, null, null, null, null, null, null, new DVNTAsyncRequestListener<DVNTDeviationInfo.List>() {
            @Override
            public void onSuccess(DVNTDeviationInfo.List resultStream) {
                Toast.makeText(DVNTSampleBaseActivity.this, "Browsed " + resultStream.size() + " deviations ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "Browse ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).feedbackCounts(0, 10, new DVNTAsyncRequestListener<DVNTDeviationStats.List>() {
            @Override
            public void onSuccess(DVNTDeviationStats.List stats) {
                Toast.makeText(DVNTSampleBaseActivity.this, "Fetched " + stats.size() + " stats ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "DeviationStats ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).commentsForDeviation(APP_DEVIATION_ID, null, null, null, new DVNTAsyncRequestListener<DVNTDeviationCommentsThread>() {
            @Override
            public void onSuccess(DVNTDeviationCommentsThread dvntDeviationCommentsThread) {
                Toast.makeText(DVNTSampleBaseActivity.this, "Fetched " + dvntDeviationCommentsThread.getComments().size() + " comments ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "commentsForDeviation ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).moreLikeThis(APP_DEVIATION_ID, new DVNTAsyncRequestListener<DVNTMoreLikeThisResults>() {
            @Override
            public void onSuccess(DVNTMoreLikeThisResults dvntMoreLikeThisAndFromArtistResults) {
                Toast.makeText(DVNTSampleBaseActivity.this, "found " + dvntMoreLikeThisAndFromArtistResults.getMoreFromArtist().size() + " more deviations from this artist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "moreLikeThisAndFromArtist ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        DVNTOAuth.closeSession(this);
        super.onDestroy();
    }
}
