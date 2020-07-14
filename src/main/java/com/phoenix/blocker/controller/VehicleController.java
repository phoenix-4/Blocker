package com.phoenix.blocker.controller;

import com.phoenix.blocker.dao.Person;
import com.phoenix.blocker.dao.Vehicle;
import com.phoenix.blocker.service.VehicleFirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleFirebaseService vehicleFirebaseService;

    @GetMapping("/getVehicle")
    public Vehicle getVehicleDetails(@RequestHeader String registrationNo) {
        System.out.println("Getting Vehicle......");
        return vehicleFirebaseService.getVehicle(registrationNo);
    }

    @PostMapping("/createVehicle")
    public String createNewVehicle(@RequestBody Vehicle vehicle) {
        System.out.println("Creating Vehicle.....");
        return vehicleFirebaseService.saveVehicle(vehicle);
    }

    @PutMapping("/updateVehicle")
    public String updateVehicle(@RequestBody Vehicle vehicle) {
        return vehicleFirebaseService.updateVehicle(vehicle);
    }

//    @DeleteMapping("/deleteUser")
//    public String deleteUser(@RequestHeader String name) throws InterruptedException, ExecutionException {
//        return firebaseServices.deleteUser(name);
//    }
}
