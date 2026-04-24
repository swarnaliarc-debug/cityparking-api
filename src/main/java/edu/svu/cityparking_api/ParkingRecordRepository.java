package edu.svu.cityparking_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    
    /**
     * Finds parking history only for a specific user.
     * It looks through the 'vehicle' relationship to find the 'user' ID.
     */
    List<ParkingRecord> findByVehicleUserId(Long userId);
}

