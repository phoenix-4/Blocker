package com.phoenix.blocker.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.phoenix.blocker.dao.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserFirebaseService {

    public User saveUserWithCollectionId(String collectionId, User user) throws InterruptedException, ExecutionException {

        if (getUser(collectionId) == null) {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> future = db.collection("users").document(collectionId).set(user);
            return user;
        }
        else
            return null;
    }

    public User getUser(String username) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(username);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = future.get();
        User user = null;
        if (document.exists()) {
            // convert document to POJO
            user = document.toObject(User.class);
            System.out.println(user);
            return user;
        } else {
            System.out.println("No such user!");
            return null;
        }
    }



}
