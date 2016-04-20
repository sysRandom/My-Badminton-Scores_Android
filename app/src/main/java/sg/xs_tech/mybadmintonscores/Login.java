package sg.xs_tech.mybadmintonscores;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private JSONObject queryData = new JSONObject();
    private JSONObject queryResult = new JSONObject();

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fb_login);

        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Log.i(this.toString(), "Already has token: " + accessToken.getToken());
            this.finish();
        }
        else {
            loginButton.setReadPermissions("user_friends");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.i(this.toString(), "Facebook login successful!");
                    finish();
                }

                @Override
                public void onCancel() {
                    Log.i(this.toString(), "Facebook login cancelled!");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(this.toString(), "Facebook login error: " + error.toString());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void finishLogin() {
        this.finish();
    }
}
