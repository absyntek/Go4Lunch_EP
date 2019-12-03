package com.EtiennePriou.go4launch.utils;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NoteCalculTest {

    private QueryDocumentSnapshot queryDocumentSnapshot;
    private QuerySnapshot queryDocumentSnapshots;


    @Before
    public void setUp (){
        queryDocumentSnapshots = Mockito.mock(QuerySnapshot.class);
        queryDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot.class);

        List<QueryDocumentSnapshot> mDocumentSnapshotList = new ArrayList<>();

        mDocumentSnapshotList.add(queryDocumentSnapshot);
        mDocumentSnapshotList.add(queryDocumentSnapshot);
        mDocumentSnapshotList.add(queryDocumentSnapshot);

        Mockito.when(queryDocumentSnapshots.iterator()).thenReturn(mDocumentSnapshotList.iterator());
    }

    @Test
    public void calateNoteTest2() {

        Mockito.when(queryDocumentSnapshots.size()).thenReturn(3);
        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(2);


        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(2,result);
    }

    @Test
    public void calateNoteTest3() {

        Mockito.when(queryDocumentSnapshots.size()).thenReturn(3);
        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(2,3,4);


        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(3,result);
    }

    @Test
    public void calateNoteTest0() {

        Mockito.when(queryDocumentSnapshots.size()).thenReturn(0,1,0);
        //Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(0);


        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(0,result);
    }
}