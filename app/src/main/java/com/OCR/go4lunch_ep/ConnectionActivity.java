package com.OCR.go4lunch_ep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.OCR.go4lunch_ep.ui.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class ConnectionActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_connection);
        setUpVars();
        setUpFaceboonkLogin();
    }

    private void setUpVars (){
        mCallbackManager = CallbackManager.Factory.create();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void setUpFaceboonkLogin (){
        checkLastConnexion();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getApplicationContext().startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // TODO code this
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // TODO code this
                    }
                });
    }

    private void checkLastConnexion () {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            getApplicationContext().startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
