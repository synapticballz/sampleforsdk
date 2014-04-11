package com.deviantart.sdksample.app.ui.activity;

import android.util.Log;
import android.widget.Toast;

import com.deviantart.android.sdk.api.DVNTAsyncAPI;
import com.deviantart.android.sdk.api.DVNTBrowseMode;
import com.deviantart.android.sdk.api.listener.DVNTAsyncRequestListener;
import com.deviantart.android.sdk.api.model.DVNTDeviationCommentsThread;
import com.deviantart.android.sdk.api.model.DVNTDeviationInfo;
import com.deviantart.android.sdk.api.model.DVNTDeviationStats;
import com.deviantart.android.sdk.api.model.DVNTMoreLikeThisResults;
import com.deviantart.android.sdk.api.model.DVNTPlacebo;
import com.deviantart.android.sdk.api.model.DVNTUserInfo;


/**
 * Sample Activity for SDK.
 *
 * Shows standard OAuth session opening/closing + API calls.
 *
 * Created by drommk on 11/21/13.
 */
public class SampleActivity extends SampleBaseActivity {
    @Override
    protected void onResume() {
        super.onResume();
        if (!DVNTAsyncAPI.isReady()) {
            Log.d("SampleActivity", "API is not ready");
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

                Toast.makeText(SampleActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "PLACEBO ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).whoAmI(new DVNTAsyncRequestListener<DVNTUserInfo>() {
            @Override
            public void onSuccess(DVNTUserInfo userInfo) {
                Toast.makeText(SampleActivity.this, "I am " + userInfo.getUserName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "WhoAmI ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).browse(DVNTBrowseMode.POPULAR, null, null, null, null, null, null, new DVNTAsyncRequestListener<DVNTDeviationInfo.List>() {
            @Override
            public void onSuccess(DVNTDeviationInfo.List resultStream) {
                Toast.makeText(SampleActivity.this, "Browsed " + resultStream.size() + " deviations ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "Browse ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).feedbackCounts(0, 10, new DVNTAsyncRequestListener<DVNTDeviationStats.List>() {
            @Override
            public void onSuccess(DVNTDeviationStats.List stats) {
                Toast.makeText(SampleActivity.this, "Fetched " + stats.size() + " stats ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "DeviationStats ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).commentsForDeviation(SAMPLE_APP_DEVIATION_ID, null, null, null, new DVNTAsyncRequestListener<DVNTDeviationCommentsThread>() {
            @Override
            public void onSuccess(DVNTDeviationCommentsThread dvntDeviationCommentsThread) {
                Toast.makeText(SampleActivity.this, "Fetched " + dvntDeviationCommentsThread.getComments().size() + " comments ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "commentsForDeviation ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DVNTAsyncAPI.with(this).moreLikeThis(SAMPLE_APP_DEVIATION_ID, new DVNTAsyncRequestListener<DVNTMoreLikeThisResults>() {
            @Override
            public void onSuccess(DVNTMoreLikeThisResults dvntMoreLikeThisAndFromArtistResults) {
                Toast.makeText(SampleActivity.this, "found " + dvntMoreLikeThisAndFromArtistResults.getMoreFromArtist().size() + " more deviations from this artist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(SampleActivity.this, "moreLikeThisAndFromArtist ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
