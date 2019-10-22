package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.EtiennePriou.go4launch.models.Message;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class MessageHelper {

    private static final String COLLECTION_NAME = "messages";

    // --- GET ---

    public static Query getAllMessageForChat(String token){
        return ChatHelper.getChatCollection()
                .document(token)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated", Query.Direction.ASCENDING)
                .limit(50);
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, String token, Workmate userSender){

        // 1 - Create the Message object
        Message message = new Message(textMessage, userSender);

        // 2 - Store Message to Firestore
        return ChatHelper.getChatCollection()
                .document(token)
                .collection(COLLECTION_NAME)
                .add(message);
    }
}