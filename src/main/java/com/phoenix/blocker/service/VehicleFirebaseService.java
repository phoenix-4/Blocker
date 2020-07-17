package com.phoenix.blocker.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.phoenix.blocker.dao.User;
import com.phoenix.blocker.dao.Vehicle;
import org.springframework.stereotype.Service;

import java.security.acl.Owner;
import java.util.concurrent.ExecutionException;

@Service
public class VehicleFirebaseService {

    public String saveVehicle(Vehicle vehicle){

        if (getVehicle(vehicle.getRegistrationNo()) == null) {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> future = db.collection("vehicles").document(vehicle.getRegistrationNo()).set(vehicle);
            return "Successfully Registered";
        }
        else
            return "Vehicle number Already registered";
    }

    public Vehicle getVehicle(String registrationNO) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("vehicles").document(registrationNO);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // block on response
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Vehicle vehicle = null;
        if (document.exists()) {
            // convert document to POJO
            Iterable<CollectionReference> collections =
                    db.collection("vehicles").document(registrationNO).listCollections();

//            for (CollectionReference collRef : collections) {
//                System.out.println("Found subcollection with id: " + collRef.getId());
//            }

            vehicle = document.toObject(Vehicle.class);
            System.out.println(vehicle.getCurrentOwner().getOwner());
            return vehicle;
        } else {
            System.out.println("No such vehicle!");
            return null;
        }
    }

    public String updateVehicle(Vehicle newVehicle){

        Vehicle existingVehicle = getVehicle(newVehicle.getRegistrationNo());

        if (existingVehicle != null){
            if (!(existingVehicle.getCurrentOwner().getNic().equalsIgnoreCase( newVehicle.getCurrentOwner().getNic()))){
                existingVehicle.getPreviousOwners().add(existingVehicle.getCurrentOwner());
                existingVehicle.setCurrentOwner(newVehicle.getCurrentOwner());
            }
            existingVehicle.setChassisNo(newVehicle.getChassisNo());
            existingVehicle.setEngineNo(newVehicle.getEngineNo());
            existingVehicle.setColour(newVehicle.getColour());
            existingVehicle.setHash(newVehicle.getHash());

            Firestore db = FirestoreClient.getFirestore();
            DocumentReference docRef = db.collection("vehicles").document(newVehicle.getRegistrationNo());
            // (async) Update
            ApiFuture<WriteResult> future = docRef.set(existingVehicle);

            try {
                WriteResult result = future.get();
                return "Success";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Fail";
            } catch (ExecutionException e) {
                e.printStackTrace();
                return "Fail";
            }
        } else {
            return "Error while updating vehicle info";
        }


    }


}
