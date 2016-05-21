package sg.xs_tech.mybadmintonscores;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private CallbackManager callbackManager;
//    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        FacebookSdk.sdkInitialize(getApplicationContext());
        final AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(getApplicationContext());
//        final Profile profile = Profile.getCurrentProfile();
        final JSONObject queryData = new JSONObject();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login);

        callbackManager = CallbackManager.Factory.create();

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            try {
                Log.i(this.toString(), "Already has token: " + accessToken.getToken());
                appEventsLogger.logEvent("FACEBOOK_ALREADY_LOGGED_IN");
                queryData.put("fb_app_id", getResources().getString(R.string.facebook_app_id));
                queryData.put("fb_id", accessToken.getUserId());
                queryData.put("fb_ct", accessToken.getToken());
//                if (profile != null) {
//                    queryData.put("fb_name", profile.getName());
//                    queryData.put("fb_fname", profile.getFirstName());
//                    queryData.put("fb_mname", profile.getMiddleName());
//                    queryData.put("fb_lname", profile.getLastName());
//                    queryData.put("fb_uri", profile.getLinkUri());
//                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.member_login_api_url),
                        queryData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(this.toString(), "Login Response: " + response.toString());
                        finish();
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
            loginButton.setReadPermissions("user_friends");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    try {
                        Log.i(this.toString(), "Facebook login successful!");
                        appEventsLogger.logEvent("FACEBOOK_LOGIN_SUCCESS");
                        queryData.put("fb_app_id", getResources().getString(R.string.facebook_app_id));
                        queryData.put("fb_id", accessToken.getUserId());
                        queryData.put("fb_ct", accessToken.getToken());
//                        if (profile != null) {
//                            queryData.put("fb_name", profile.getName());
//                            queryData.put("fb_fname", profile.getFirstName());
//                            queryData.put("fb_mname", profile.getMiddleName());
//                            queryData.put("fb_lname", profile.getLastName());
//                            queryData.put("fb_uri", profile.getLinkUri());
//                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                getResources().getString(R.string.member_login_api_url),
                                queryData, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(this.toString(), "Login Response: " + response.toString());
                                finish();
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

                @Override
                public void onCancel() {
                    Log.i(this.toString(), "Facebook login cancelled!");
                    appEventsLogger.logEvent("FACEBOOK_LOGIN_CANCELLED");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(this.toString(), "Facebook login error: " + error.toString());
                    appEventsLogger.logEvent("FACEBOOK_LOGIN_ERROR");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
