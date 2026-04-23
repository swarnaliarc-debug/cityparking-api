package edu.svu.cityparking_api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class BasicOpsController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private ParkingRecordRepository parkingRecordRepository;

    // --- User Endpoints ---
    @GetMapping("/user/{userid}")
    public User user(@PathVariable String userid){
        return userRepository.findById(Long.parseLong(userid)).orElse(null);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }
    
    @PostMapping("/login")
    public User login(@RequestBody LoginForm loginForm){
        return userRepository.findByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword()).orElse(null);
    }

    // --- Vehicle Endpoints ---
    @GetMapping("/vehicle/{vehicleid}")
    public Vehicle vehicle(@PathVariable String vehicleid){
        return vehicleRepository.findById(Long.parseLong(vehicleid)).orElse(null);
    }

    @GetMapping("/uservehicles/{userid}")
    public List<Vehicle> userVehicles(@PathVariable String userid){
        return vehicleRepository.findByUserId(Long.parseLong(userid));
    }

    @PostMapping("/vehicle")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle, @RequestParam String userid){
        log.info("Creating vehicle: " + vehicle.getPlateNumber());
        User vehicleOwner = userRepository.findById(Long.parseLong(userid)).get();
        vehicle.setUser(vehicleOwner);
        return vehicleRepository.save(vehicle);
    }
 
    @DeleteMapping("/vehicle/{id}")
    public void deleteVehicle(@PathVariable String id){
        vehicleRepository.deleteById(Long.parseLong(id));
    }

    // --- Parking Records Endpoints ---

    @GetMapping("/parking-history")
    public List<ParkingRecord> getParkingHistory() {
        return parkingRecordRepository.findAll();
    }

    @PostMapping("/test-entry")
    public String addTestEntry() {
        // Find vehicle ID 5
        Vehicle realVehicle = vehicleRepository.findById(5L).orElse(null);

        if (realVehicle == null) {
            return "Error: Vehicle ID 5 not found in database!";
        }

        try {
            // Check if already parked to prevent double entry
            List<ParkingRecord> allRecords = parkingRecordRepository.findAll();
            boolean isAlreadyParked = allRecords.stream()
                .anyMatch(r -> r.getVehicle().getId().equals(5L) && r.getExitDateTime() == null);
            
            if (isAlreadyParked) {
                return "Error: Vehicle is already parked (Status: IN). Use test-exit first.";
            }

            ParkingRecord test = new ParkingRecord();
            test.setVehicle(realVehicle); 
            test.setParkingId("SPOT-01");
            test.setEntryDateTime(LocalDateTime.now());
            
            parkingRecordRepository.save(test);
            return "Success! Entry recorded for plate: " + realVehicle.getPlateNumber(); 
        } catch (Exception e) {
            log.error("Save failed: " + e.getMessage());
            return "Error saving record: " + e.getMessage();
        }
    }

    @PostMapping("/test-exit")
    public String addTestExit() {
        Long targetVehicleId = 5L; 
        
        List<ParkingRecord> allRecords = parkingRecordRepository.findAll();
        ParkingRecord activeRecord = allRecords.stream()
            .filter(r -> r.getVehicle().getId().equals(targetVehicleId) && r.getExitDateTime() == null)
            .findFirst()
            .orElse(null);

        if (activeRecord == null) {
            return "Error: This vehicle is not currently parked (Status: OUT). Use test-entry first!";
        }

        activeRecord.setExitDateTime(LocalDateTime.now());
        parkingRecordRepository.save(activeRecord);

        return "Success! Exit recorded. Vehicle status updated to OUT.";
    }
}
