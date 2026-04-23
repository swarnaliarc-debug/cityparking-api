package edu.svu.cityparking_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    // You can add custom search methods here later
}
