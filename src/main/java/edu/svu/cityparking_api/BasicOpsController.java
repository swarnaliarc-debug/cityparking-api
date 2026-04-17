package edu.svu.cityparking_api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
 

@RestController
@Slf4j
public class BasicOpsController {
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userid}")
    public User user(@PathVariable String userid){
        return userRepository.findById(Long.parseLong(userid)).get();
    }

    @GetMapping("/vehicle/{vehicleid}")
    public Vehicle vehicle(@PathVariable String vehicleid){
        Vehicle vehicle= vehicleRepository.findById(Long.parseLong(vehicleid)).get();
        return vehicle;
    }

    @GetMapping("/uservehicles/{userid}")
    public List<Vehicle> userVehicles(@PathVariable String userid){
        List<Vehicle> vehicles= vehicleRepository.findByUserId(Long.parseLong(userid));
        return vehicles;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user){

        User newUser = userRepository.save(user);
        return newUser;

    }

    @PostMapping("/vehicle")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle, @RequestParam String userid){

        User vehicleOwner = userRepository.findById(Long.parseLong(userid)).get();
        vehicle.setUser(vehicleOwner);
        Vehicle newVehicle = vehicleRepository.save(vehicle);
        return newVehicle;
        
    }
 

}
