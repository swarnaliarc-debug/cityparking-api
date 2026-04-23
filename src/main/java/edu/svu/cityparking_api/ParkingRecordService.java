package edu.svu.cityparking_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingRecordService {

    @Autowired
    private ParkingRecordRepository parkingRecordRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // 1. Start a parking session
    public ParkingRecord startParking(Long vehicleId, String parkingId) {
        // Find the real vehicle from the database first
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        // Create the record and set the OBJECT
        ParkingRecord record = new ParkingRecord();
        record.setVehicle(vehicle); 
        record.setParkingId(parkingId);
        record.setEntryDateTime(LocalDateTime.now());

        return parkingRecordRepository.save(record);
    }

    // 2. Get all records to show in your React table
    public List<ParkingRecord> getAllRecords() {
        return parkingRecordRepository.findAll();
    }
}
