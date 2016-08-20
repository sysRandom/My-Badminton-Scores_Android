package sg.xs_tech.mybadmintonscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        final AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(getApplicationContext());
        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login);

        callbackManager = CallbackManager.Factory.create();

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
//            Log.i(this.toString(), "Already has token: " + accessToken.getToken());
            appEventsLogger.logEvent("FACEBOOK_ALREADY_LOGGED_IN");
            finish();
        }
        else {
            loginButton.setReadPermissions("user_friends");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
//                    Log.i(this.toString(), "Facebook login successful!");
                    appEventsLogger.logEvent("FACEBOOK_LOGIN_SUCCESS");
                    finish();
                }

                @Override
                public void onCancel() {
//                    Log.i(this.toString(), "Facebook login cancelled!");
                    appEventsLogger.logEvent("FACEBOOK_LOGIN_CANCELLED");
                }

                @Override
                public void onError(FacebookException error) {
//                    Log.e(this.toString(), "Facebook login error: " + error.toString());
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
