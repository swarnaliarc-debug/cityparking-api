package edu.svu.cityparking_api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/vehicle/by-plate/{plateNumber}")
    public Vehicle getVehicleByPlate(@PathVariable String plateNumber) {
        log.info("AI Query for plate: " + plateNumber);
        return vehicleRepository.findByPlateNumber(plateNumber).orElse(null);
    }

    @PostMapping("/vehicle")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle, @RequestParam String userid) {
        log.info("Processing vehicle: " + vehicle.getPlateNumber());

        Optional<Vehicle> existingVehicle = vehicleRepository.findByPlateNumber(vehicle.getPlateNumber());

        if (existingVehicle.isPresent()) {
            if (vehicle.getId() != null) {
                if (!existingVehicle.get().getId().equals(vehicle.getId())) {
                    log.error("Plate " + vehicle.getPlateNumber() + " is already taken by another vehicle!");
                    return null;
                }
            } else {
                log.error("Plate number already exists for a new registration!");
                return null;
            }
        }

        User vehicleOwner = userRepository.findById(Long.parseLong(userid))
                .orElseThrow(() -> new RuntimeException("User not found"));

        vehicle.setUser(vehicleOwner);
        return vehicleRepository.save(vehicle);
    }

    @DeleteMapping("/vehicle/{id}")
    public void deleteVehicle(@PathVariable String id){
        vehicleRepository.deleteById(Long.parseLong(id));
    }

    // --- Parking Records Endpoints ---

    @GetMapping("/parking-history/{userid}")
    public List<ParkingRecord> getParkingHistory(@PathVariable String userid) {
        log.info("Fetching history for user: " + userid);
        return parkingRecordRepository.findByVehicleUserId(Long.parseLong(userid));
    }


       @PostMapping("/parking/toggle")
    public String toggleParking(@RequestParam String plateNumber) {
        log.info("Parking toggle requested for plate: " + plateNumber);

        // 1. Find the vehicle by plate
        Vehicle vehicle = vehicleRepository.findByPlateNumberIgnoringSpacesAndCase(plateNumber).orElse(null);
        if (vehicle == null) {
            return "Error: Vehicle with plate [" + plateNumber + "] not found!";
        }

        // 2. Check if currently parked (active record has no exit time)
        List<ParkingRecord> allRecords = parkingRecordRepository.findAll();
        ParkingRecord activeRecord = allRecords.stream()
            .filter(r -> r.getVehicle() != null && 
                        r.getVehicle().getPlateNumber() != null && // Added null check for safety
                        r.getVehicle().getPlateNumber().replaceAll("\\s+", "")
                        .equalsIgnoreCase(plateNumber.replaceAll("\\s+", "")) && 
                        r.getExitDateTime() == null)
            .findFirst()      // Streams usually require a terminal operation
            .orElse(null);

        try {
            if (activeRecord != null) {
                // EXIT
                activeRecord.setExitDateTime(LocalDateTime.now());
                parkingRecordRepository.save(activeRecord);
                return "SUCCESS: Exit recorded for " + plateNumber + ". Status: OUT";
            } else {
                // ENTRY
                ParkingRecord newEntry = new ParkingRecord();
                newEntry.setVehicle(vehicle);
                newEntry.setParkingId("SPOT-01");
                newEntry.setEntryDateTime(LocalDateTime.now());
                parkingRecordRepository.save(newEntry);
                return "SUCCESS: Entry recorded for " + plateNumber + ". Status: IN";
            }
        } catch (Exception e) {
            log.error("Parking toggle failed: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

}
