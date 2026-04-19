package edu.svu.cityparking_api;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private VehicleType type;

    @Column(name = "brand_name")
    private String brandname;
    
    @Column(name = "model")
    private String model;

    @Column(name = "color")
    private String color;

    @Column(name = "plate_number")
    private String plateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    @JsonBackReference
    private User user;
}
