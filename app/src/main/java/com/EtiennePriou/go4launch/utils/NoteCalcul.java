package com.EtiennePriou.go4launch.utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NoteCalcul {

    public static Integer calateNote(@NonNull QuerySnapshot queryDocumentSnapshots){

        if (queryDocumentSnapshots.size() > 0){

            int divisor = queryDocumentSnapshots.size();
            int noteToDivise = 0;

            for (DocumentSnapshot note : queryDocumentSnapshots){
                int noteTmp = note.get("note",Integer.class);
                noteToDivise += noteTmp;
            }
            if (noteToDivise < 1){
                return 0; }
            else{
                return Math.round(noteToDivise/divisor); }
        }else return 0;
    }
}

