package edu.svu.cityparking_api;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_records")
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This creates the actual database link to the Vehicle table
    @ManyToOne
    @JoinColumn(name = "vehicle_id") 
    private Vehicle vehicle; 

    private String parkingId; 
    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;

    // --- GETTERS AND SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // FIXED: The parameter type must be 'Vehicle', not 'Long'
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public LocalDateTime getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(LocalDateTime entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public LocalDateTime getExitDateTime() {
        return exitDateTime;
    }

    public void setExitDateTime(LocalDateTime exitDateTime) {
        this.exitDateTime = exitDateTime;
    }
}
