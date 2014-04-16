package com.deviantart.sdksample.app.ui.activity;

import android.util.Log;
import android.widget.Toast;

import com.deviantart.android.sdk.api.DVNTAsyncAPI;
import com.deviantart.android.sdk.api.DVNTBrowseMode;
import com.deviantart.android.sdk.api.listener.DVNTAsyncRequestListener;
import com.deviantart.android.sdk.api.model.DVNTDamnToken;
import com.deviantart.android.sdk.api.model.DVNTDeviationCommentsThread;
import com.deviantart.android.sdk.api.model.DVNTDeviationInfo;
import com.deviantart.android.sdk.api.model.DVNTDeviationStats;
import com.deviantart.android.sdk.api.model.DVNTEndpointError;
import com.deviantart.android.sdk.api.model.DVNTMoreLikeThisResults;
import com.deviantart.android.sdk.api.model.DVNTPlacebo;
import com.deviantart.android.sdk.api.model.DVNTStashSubmitResponse;
import com.deviantart.android.sdk.api.model.DVNTUserInfo;
import com.deviantart.sdksample.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


/**
 * Sample Activity for SDK.
 * <p/>
 * Shows standard OAuth session opening/closing + API calls.
 * <p/>
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

        asyncPlacebo();
        asyncWhoAmI();
        asyncBrowsePopular();
        asyncFeedbackCounts();
        asyncCommentsForDeviation();
        asyncMoreLikeThis();
        asyncDamnToken();
        asyncStashSubmitTextFile();
        asyncStashSubmitImage();
    }

    private void asyncStashSubmitImage() {
        InputStream imageIS = getResources().openRawResource(R.drawable.ic_launcher);
        File outputDir = getCacheDir(); // context being the Activity pointer
//        File outputFile = File.createTempFile("prefix", "txt", outputDir);
    }

    private void asyncStashSubmitTextFile() {
        try {
            File outputDir = getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile("Lorem Ipsum", "txt", outputDir);
            FileWriter writer = new FileWriter(outputFile);
            writer.write("Lorem Ipsum");
            writer.close();

            DVNTAsyncAPI.with(this).stashSubmitFile(outputFile, "text/html", "testTitle", new SampleAsyncListener<DVNTStashSubmitResponse>() {
                @Override
                public void onSuccess(DVNTStashSubmitResponse submitResponse) {
                    Toast.makeText(SampleActivity.this, "submitted file in stashid = " + submitResponse.getStashId(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void asyncDamnToken() {
        DVNTAsyncAPI.with(this).damnToken(new SampleAsyncListener<DVNTDamnToken>() {
            @Override
            public void onSuccess(DVNTDamnToken tokenResult) {
                Toast.makeText(SampleActivity.this, "damnToken = " + tokenResult.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncMoreLikeThis() {
        DVNTAsyncAPI.with(this).moreLikeThis(SAMPLE_APP_DEVIATION_ID, new SampleAsyncListener<DVNTMoreLikeThisResults>() {
            @Override
            public void onSuccess(DVNTMoreLikeThisResults dvntMoreLikeThisAndFromArtistResults) {
                Toast.makeText(SampleActivity.this, "found " + dvntMoreLikeThisAndFromArtistResults.getMoreFromArtist().size() + " more deviations from this artist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncCommentsForDeviation() {
        DVNTAsyncAPI.with(this).commentsForDeviation(SAMPLE_APP_DEVIATION_ID, null, null, null, new SampleAsyncListener<DVNTDeviationCommentsThread>() {
            @Override
            public void onSuccess(DVNTDeviationCommentsThread dvntDeviationCommentsThread) {
                Toast.makeText(SampleActivity.this, "Fetched " + dvntDeviationCommentsThread.getComments().size() + " comments ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncFeedbackCounts() {
        DVNTAsyncAPI.with(this).feedbackCounts(0, 10, new SampleAsyncListener<DVNTDeviationStats.List>() {
            @Override
            public void onSuccess(DVNTDeviationStats.List stats) {
                Toast.makeText(SampleActivity.this, "Fetched " + stats.size() + " stats ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncBrowsePopular() {
        DVNTAsyncAPI.with(this).browse(DVNTBrowseMode.POPULAR, null, null, null, null, null, null, new SampleAsyncListener<DVNTDeviationInfo.List>() {
            @Override
            public void onSuccess(DVNTDeviationInfo.List resultStream) {
                Toast.makeText(SampleActivity.this, "Browsed " + resultStream.size() + " deviations ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncWhoAmI() {
        DVNTAsyncAPI.with(this).whoAmI(new SampleAsyncListener<DVNTUserInfo>() {
            @Override
            public void onSuccess(DVNTUserInfo userInfo) {
                Toast.makeText(SampleActivity.this, "I am " + userInfo.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asyncPlacebo() {
        DVNTAsyncAPI.with(this).placebo(new SampleAsyncListener<DVNTPlacebo>() {
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
        });
    }

    private abstract class SampleAsyncListener<T> implements DVNTAsyncRequestListener<T> {
        public void onException(Exception e) {
            Toast.makeText(SampleActivity.this, " Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        public void onFailure(DVNTEndpointError dvntEndpointError) {
            Toast.makeText(SampleActivity.this, " Endpoint error : " + dvntEndpointError.getError() + " / " + dvntEndpointError.getErrorDetails(), Toast.LENGTH_LONG).show();
        }
    }
}
