package com.EtiennePriou.go4launch.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.MainActivity;
import com.EtiennePriou.go4launch.ui.details.DetailPlaceActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class NotificationsService extends FirebaseMessagingService {

    private String placeRef,placeName,adresse,messageBody;
    QuerySnapshot listName;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        checkPlaceToGo();
    }

    private void checkPlaceToGo(){
        String uid = FirebaseAuth.getInstance().getUid();
        UserHelper.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Workmate workmate = documentSnapshot.toObject(Workmate.class);
                if (workmate.getPlaceToGo() !=null){
                    placeRef = workmate.getPlaceToGo().get("placeRef").toString();
                    adresse = workmate.getPlaceToGo().get("adresse").toString();
                    placeName = workmate.getPlaceToGo().get("placeName").toString();
                    getPlaceToGo(placeRef);
                }
            }
        });
    }

    private void getPlaceToGo(String placeRef){
        PlaceHelper.getWhoComing(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listName = queryDocumentSnapshots;
                makeMessage(queryDocumentSnapshots.isEmpty());
            }
        });
    }

    private void makeMessage(Boolean isEmp) {
        StringBuilder nameMessage = new StringBuilder();
        if (isEmp){
            nameMessage.append(", but no one is coming ;(");
        }else{
            nameMessage.append("with : \n");
            for (DocumentSnapshot data : listName){
                nameMessage.append("- " + data.get("name")+"\n");
            }
        }
        messageBody = "Dont forget, you're going to" + placeName + nameMessage;

        sendVisualNotification(messageBody);
    }

    private void sendVisualNotification(String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, DetailPlaceActivity.class);
        intent.putExtra("placeRef", placeRef);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notext_logo200x200)
                        .setContentTitle(getString(R.string.app_name))
                        .setTicker(getString(R.string.notification_title))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

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
