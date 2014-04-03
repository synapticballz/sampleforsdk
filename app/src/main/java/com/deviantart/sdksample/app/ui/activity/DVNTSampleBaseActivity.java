package com.deviantart.sdksample.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wonderlands.sdk.DVNTConsts;
import com.wonderlands.sdk.api.DVNTAsyncAPI;
import com.wonderlands.sdk.api.listener.DVNTAsyncRequestListener;
import com.wonderlands.sdk.api.model.DVNTGroupInfo;
import com.wonderlands.sdk.api.model.DVNTPlacebo;
import com.wonderlands.sdk.api.model.DVNTUserInfo;
import com.wonderlands.sdk.oauth.DVNTOAuth;

/**
 * Sample Activity for SDK.
 *
 * Shows standard OAuth session opening/closing + API calls.
 *
 * //TODO put this sample in its own module.
 *
 * Created by drommk on 11/21/13.
 */
public class DVNTSampleBaseActivity extends Activity {

    private static final String TAG = "DVNTSampleBaseActivity";
    private static final boolean mementoMode = false;
    private static final String SAMPLE_SCOPE = DVNTConsts.BASIC_SCOPE + " " + DVNTConsts.GROUP_SCOPE;

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
                if (placeboResult.wasSuccessful()) {
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
                Toast.makeText(DVNTSampleBaseActivity.this, "WHOAMI ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        final String groupName = "hq";

        DVNTAsyncAPI.with(this).getGroupInfo(groupName, new DVNTAsyncRequestListener<DVNTGroupInfo>() {
            @Override
            public void onSuccess(DVNTGroupInfo groupInfo) {
                StringBuilder builder = new StringBuilder("I am ");
                if (!groupInfo.getIsMember()) {
                    builder.append("not ");
                }
                builder.append("a member of " + groupName);
                Toast.makeText(DVNTSampleBaseActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
                if (mementoMode) {
                    DVNTOAuth.logout(DVNTSampleBaseActivity.this);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(DVNTSampleBaseActivity.this, "GROUPINFO ASYNC FAILURE : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        DVNTOAuth.closeSession(this);
        super.onDestroy();
    }
}
