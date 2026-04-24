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

    @Column(name = "plate_number", unique = true, nullable = false)
    private String plateNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    @JsonBackReference
    private User user;

    /**
     * CUSTOM SETTER
     * We override the Lombok @Setter for plateNumber to force
     * every entry into UPPERCASE and remove accidental spaces.
     */
    public void setPlateNumber(String plateNumber) {
        if (plateNumber != null) {
            this.plateNumber = plateNumber.toUpperCase().trim();
        } else {
            this.plateNumber = null;
        }
    }
}
