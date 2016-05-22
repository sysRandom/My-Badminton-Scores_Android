package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private AccessTokenTracker accessTokenTracker;
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
        final JSONObject queryData = new JSONObject();

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

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    try {
                        Log.i(this.toString(), "New access token: " + currentAccessToken.getToken());
                        mShowHideButtons(View.VISIBLE);
                        queryData.put("fb_app_id", getResources().getString(R.string.facebook_app_id));
                        queryData.put("fb_id", currentAccessToken.getUserId());
                        queryData.put("fb_ct", currentAccessToken.getToken());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                getResources().getString(R.string.member_login_api_url),
                                queryData, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(this.toString(), "Login Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                                    Log.i(this.toString(), "Response error message: " + volleyError.getMessage());
                                }
                            }
                        });
                        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            try {
                Log.i(this.toString(), "Access token: " + accessToken.getToken());
                mShowHideButtons(View.VISIBLE);
                queryData.put("fb_app_id", getResources().getString(R.string.facebook_app_id));
                queryData.put("fb_id", accessToken.getUserId());
                queryData.put("fb_ct", accessToken.getToken());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.member_login_api_url),
                        queryData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(this.toString(), "Login Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                            Log.i(this.toString(), "Response error message: " + volleyError.getMessage());
                        }
                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i(this.toString(), "There is no access token. Calling Login.");
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
    }
}
