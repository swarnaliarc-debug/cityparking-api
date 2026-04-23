package edu.svu.cityparking_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "http://localhost:3000") // Allows React to talk to Java
public class ParkingRecordController {

    @Autowired
    private ParkingRecordService service;

    // POST: http://localhost:8080/api/parking/start
    @PostMapping("/start")
    public ParkingRecord start(@RequestParam Long vehicleId, @RequestParam String parkingId) {
        return service.startParking(vehicleId, parkingId);
    }

    // GET: http://localhost:8080/api/parking/history
    @getMapping("/history")
    public List<ParkingRecord> getHistory() {
        return service.getAllRecords();
    }
}
