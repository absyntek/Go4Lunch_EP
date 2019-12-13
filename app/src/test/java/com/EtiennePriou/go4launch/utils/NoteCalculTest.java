package com.EtiennePriou.go4launch.utils;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class NoteCalculTest {

    private QueryDocumentSnapshot queryDocumentSnapshot;
    private QuerySnapshot queryDocumentSnapshots;
    private Iterator iterator;


    @Before
    public void setUp (){
        queryDocumentSnapshots = Mockito.mock(QuerySnapshot.class);
        queryDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot.class);
        iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true,true,true,false);
        Mockito.when(iterator.next()).thenReturn(queryDocumentSnapshot);
        Mockito.when(queryDocumentSnapshots.iterator()).thenReturn(iterator);
        Mockito.when(queryDocumentSnapshots.size()).thenReturn(3);
    }

    @Test
    public void calateNoteTest2() {

        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(2);

        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(2,result);
    }

    @Test
    public void calateNoteTest3() {

        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(2,3,4);

        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(3,result);
    }

    @Test
    public void calateNoteTest0() {

        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(0,1,0);


        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(0,result);
    }

    @Test
    public void calateNoteTestFull0() {

        Mockito.when(queryDocumentSnapshot.get("note", Integer.class)).thenReturn(0,0,0);


        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(0,result);
    }

    @Test
    public void calateNoteTestEmpty() {
        Mockito.when(queryDocumentSnapshots.size()).thenReturn(0);

        int result = NoteCalcul.calateNote(queryDocumentSnapshots);
        assertEquals(0,result);
    }
}