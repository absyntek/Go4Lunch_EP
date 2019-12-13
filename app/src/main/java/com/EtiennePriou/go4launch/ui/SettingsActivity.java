package com.EtiennePriou.go4launch.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.ActionBar;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.SplashActivity;
import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceToGo;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class SettingsActivity extends BaseActivity {

    TextView mDeleteAccount,mShowUserName;
    LinearLayout mUserName;

    FireBaseApi mFireBaseApi;

    AlertDialog alertDialogDelete;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.settings_activity;
    }

    @Override
    protected void setupUi() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDeleteAccount = findViewById(R.id.tvDeleteAccount);
        mShowUserName = findViewById(R.id.tv_showUserName);
        mUserName = findViewById(R.id.ll_userName);
    }

    @Override
    protected void withOnCreate() {

        mFireBaseApi = DI.getServiceFireBase();

        setUpUserNameButton();
        setUpDeleteButton();
    }

    private void setUpUserNameButton() {
        mShowUserName.setText(mFireBaseApi.getActualUser().getUsername());
        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserNameDialBox();
            }
        });
    }

    private void createUserNameDialBox(){
        final AlertDialog alertDialog;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_username, null));
        alertDialog = builder.create();
        alertDialog.show();


        //Cancel Button
        Button userNameCancel = alertDialog.findViewById(R.id.userNameBoxCancel);
        userNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //ValidButton
        Button userNameValid = alertDialog.findViewById(R.id.userNameBoxValid);
        final TextInputEditText userNameToUpdate = alertDialog.findViewById(R.id.tvUserNameToUpDate);
        userNameValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNameToUpdate.getText().toString().isEmpty()){
                    Toast.makeText(SettingsActivity.this, "Please give us a name", Toast.LENGTH_SHORT).show();
                }else if (userNameToUpdate.getText().toString().length() < 3 ){
                    Toast.makeText(SettingsActivity.this, "Sorry, your user name must be more than 3 characters", Toast.LENGTH_SHORT).show();
                }else {
                    UserHelper.updateUsername(userNameToUpdate.getText().toString(),mFireBaseApi.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Workmate toUpdate = mFireBaseApi.getActualUser();
                            toUpdate.setUsername(userNameToUpdate.getText().toString());
                            mFireBaseApi.setActualUser(toUpdate);
                            Toast.makeText(SettingsActivity.this, "Your Username is up to date", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            setUpUserNameButton();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsActivity.this, "Sorry, an error occurred", Toast.LENGTH_SHORT).show();
                            Log.i("Change UserName", "onFailure: "+ e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void setUpDeleteButton() {
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDeleteDialBox();
            }
        });
    }

    private void createDeleteDialBox(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_deleteaccount, null));
        alertDialogDelete = builder.create();
        alertDialogDelete.show();


        //Cancel Button
        Button deleteCancel = alertDialogDelete.findViewById(R.id.deleteBoxCancel);
        deleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogDelete.dismiss();
            }
        });

        //ValidButton
        Button deleteValid = alertDialogDelete.findViewById(R.id.deleteBoxValid);
        final TextInputEditText userNameToUpdate = alertDialogDelete.findViewById(R.id.tvUserNameTodelete);
        deleteValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNameToUpdate.getText().toString().isEmpty()){
                    Toast.makeText(SettingsActivity.this, "Type your user name for delete your account", Toast.LENGTH_SHORT).show();
                }else if (!userNameToUpdate.getText().toString().equals(mFireBaseApi.getActualUser().getUsername())){
                    Toast.makeText(SettingsActivity.this, "Sorry, this is not exactly correct", Toast.LENGTH_SHORT).show();
                }else if (userNameToUpdate.getText().toString().equals(mFireBaseApi.getActualUser().getUsername())){
                    deleteInformations();
                }
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        ListPreference languageList;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            languageList = findPreference("language");
        }
    }

    private void deleteInformations (){
        UserHelper.getUser(mFireBaseApi.getCurrentUser().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PlaceToGo placeToGo = documentSnapshot.get("placeToGo",PlaceToGo.class);
                if (placeToGo != null){
                    deleteMyPositionPlace(placeToGo.getPlaceRef());
                }else {
                    deleteUserFromFireStore();
                }
            }
        });
    }

    private void deleteMyPositionPlace(String placeRef) {
        PlaceHelper.deleteUserWhoComming(mFireBaseApi.getCurrentUser().getUid(),placeRef)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteUserFromFireStore();
            }
        });
    }

    private void deleteUserFromFireStore (){
        UserHelper.deleteUser(mFireBaseApi.getCurrentUser().getUid())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(SettingsActivity.this, "Your Account has been deleted", Toast.LENGTH_SHORT).show();
                                    finishOnDelete();
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Sorry, an error occurred", Toast.LENGTH_SHORT).show();
                Log.i("Delete account", "onFailure: "+ e.getMessage());
            }
        });
    }
    private void finishOnDelete (){
        FirebaseAuth.getInstance().signOut();
        alertDialogDelete.dismiss();
        Intent splash = new Intent(this, SplashActivity.class);
        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(splash);
    }
}