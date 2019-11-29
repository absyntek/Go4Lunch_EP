package com.EtiennePriou.go4launch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.MainActivity;
import com.EtiennePriou.go4launch.utils.InternetTest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AlertDialog alertDialog;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mProgressBar = findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);

        ImageView logo = findViewById(R.id.imgLogoSplash);

        startAsyncTask();

        Animation fadeAnim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(fadeAnim);
//        Button signIn = findViewById(R.id.btnSignInSplash);
//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createSignInIntent();
//            }
//        });
    }

    private void startAsyncTask(){
        new CheckIfInternet().execute(this);
    }

    public void createSignInIntent() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo_200x200)
                        .setTheme(R.style.connect)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            // Successfully signed in
            user = Objects.requireNonNull(mAuth.getCurrentUser());
            createUserInFirestore();
            startMainActivity();
        }
    }


    private void createUserInFirestore(){

        UserHelper.getUser(user.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("uid") == null || !Objects.equals(documentSnapshot.get("uid"), user.getUid())){
                    String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
                    String username = user.getDisplayName();
                    String uid = user.getUid();
                    UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(onFailureListener());
                }
            }
        });
    }

    private OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG).show();
            }
        };
    }

    private void showAlertDial (){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_no_internet, null));
        alertDialog = builder.create();
        alertDialog.show();

        Button valid = alertDialog.findViewById(R.id.ratingBoxValid);
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startAsyncTask();
            }
        });

        Button cancel = alertDialog.findViewById(R.id.ratingBoxCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
                System.exit(0);
            }
        });
    }

    private void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    @SuppressLint("StaticFieldLeak")
    class CheckIfInternet extends AsyncTask<Context,Void,Integer>{

        @Override
        protected Integer doInBackground(Context... contexts) {

            if (InternetTest.isNetworkConnected(contexts[0])){
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    return 1;
                }else {
                    return 2;
                }
            }else {
                return 3;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch (integer){
                case 1:
                    startMainActivity();
                    break;
                case 2:
                    createSignInIntent();
                    break;
                case 3:
                    showAlertDial();
                    break;
            }
        }
    }
}
