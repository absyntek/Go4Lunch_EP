package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.EtiennePriou.go4launch.models.Message;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageHelper {

    private static final String COLLECTION_NAME = "chats";
    private static final String SUBCOLLECTION_NAME = "messages";

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- GET ---

    public static Query getAllMessageForChat(String token){
        return getChatCollection()
                .document(token)
                .collection(SUBCOLLECTION_NAME)
                .orderBy("dateCreated", Query.Direction.ASCENDING)
                .limit(50);
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, String token, Workmate userSender){

        Message message = new Message(textMessage, userSender);

        return getChatCollection()
                .document(token)
                .collection(SUBCOLLECTION_NAME)
                .add(message);
    }
}