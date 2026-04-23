package edu.svu.cityparking_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // 1. Method for finding vehicles by User ID
    @Query("SELECT v FROM Vehicle v WHERE v.user.id = :userId")
    List<Vehicle> findByUserId(Long userId);

    // 2. Method for finding a specific vehicle by Plate Number
    Optional<Vehicle> findByPlateNumber(String plateNumber);
}
