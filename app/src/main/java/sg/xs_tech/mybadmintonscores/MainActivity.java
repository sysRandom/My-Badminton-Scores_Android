package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken accessToken;

    private Button btnAddMatch;
    private Button btnListMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        final AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(getApplicationContext());

        btnAddMatch = (Button) findViewById(R.id.add_match);
        btnListMatch = (Button) findViewById(R.id.view_match_history);
        btnAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.toString(), "Adding new match");
                Intent intent = new Intent(MainActivity.this, NewMatch.class);
                appEventsLogger.logEvent("START_NEW_MATCH");
                startActivity(intent);
            }
        });
        btnListMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.toString(), "Listing match history");
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    Log.i(this.toString(), "Current profile ID: " + currentProfile.getId());
                }
            }
        };
        profileTracker.startTracking();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    Log.i(this.toString(), "New access token: " + currentAccessToken.getToken());
                    mShowHideButtons(View.VISIBLE);
                }
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Log.i(this.toString(), "Access token: " + accessToken.getToken());
            mShowHideButtons(View.VISIBLE);
        }
        else {
            Log.i(this.toString(), "There is no access token");
            Intent intent = new Intent(MainActivity.this, Login.class);
            appEventsLogger.logEvent("START_LOGIN_PAGE");
            startActivity(intent);
        }
    }

    private void mShowHideButtons(int action) {
        Log.i(this.toString(), "Buttons action: " + action);
        switch (action) {
            case View.VISIBLE:
                if (btnAddMatch.getVisibility() == View.INVISIBLE) {
                    Log.i(this.toString(), "Setting buttons visible");
                    btnListMatch.setVisibility(View.VISIBLE);
                    btnAddMatch.setVisibility(View.VISIBLE);
                }
                break;
            default:
                if (btnAddMatch.getVisibility() == View.VISIBLE) {
                    Log.i(this.toString(), "Setting buttons invisible");
                    btnListMatch.setVisibility(View.INVISIBLE);
                    btnAddMatch.setVisibility(View.INVISIBLE);
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.toString(), "Resumed!");
        if (accessToken != null) {
            mShowHideButtons(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(this.toString(), "Destroyed!");
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
