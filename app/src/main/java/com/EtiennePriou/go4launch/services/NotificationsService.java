package com.EtiennePriou.go4launch.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.EtiennePriou.go4launch.utils.CheckDate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsService extends FirebaseMessagingService {

    private String placeRef, placeName, adresse, uid;
    private Date mDate;
    private Workmate workmate;
    List<Workmate> listName;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        checkPlaceToGo();
    }

    private void checkPlaceToGo(){
        uid = FirebaseAuth.getInstance().getUid();
        UserHelper.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                workmate = documentSnapshot.toObject(Workmate.class);
                if (workmate.getPlaceToGo() !=null && !CheckDate.isDatePast(workmate.getPlaceToGo().getDateCreated())){
                    placeRef = workmate.getPlaceToGo().getPlaceRef();
                    adresse = workmate.getPlaceToGo().getAdresse();
                    placeName = workmate.getPlaceToGo().getPlaceName();
                    mDate = workmate.getPlaceToGo().getDateCreated();
                    getPlaceToGo(placeRef);
                }
            }
        });
    }

    private void getPlaceToGo(String placeRef){
        PlaceHelper.getWhoComing(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Workmate> tmp = queryDocumentSnapshots.toObjects(Workmate.class);
                for (Workmate workmate : tmp){
                    if (!CheckDate.isDatePast(workmate.getPlaceToGo().getDateCreated())){
                        tmp.add(workmate);
                    }
                }
                listName = tmp;
                makeMessage();
            }
        });
    }

    private void makeMessage() {
        StringBuilder nameMessage = new StringBuilder();
        if (listName.size() == 1){
            nameMessage.append(Resources.getSystem().getString(R.string.butYouAreTheOnlyOne));
        }else{
            nameMessage.append(getString(R.string.with));
            for (Workmate data : listName){
                if (!data.getUid().equals(uid))
                nameMessage.append("- " + data.getUsername()+"\n");
            }
        }
        String messageBody = getString(R.string.dontForget) + placeName + nameMessage + "\n" + adresse;

        sendVisualNotification(messageBody);
    }

    private void sendVisualNotification(String messageBody) {

        Intent intent = new Intent(this, DetailPlaceActivity.class);
        intent.putExtra("placeReference",placeRef);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notext_logo200x200)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "default";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        String NOTIFICATION_TAG = "FIREBASE";
        int NOTIFICATION_ID = 007;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
