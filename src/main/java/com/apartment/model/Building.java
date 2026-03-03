package com.apartment.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private int id;
    @Column(name = "building_name")
    private String name;
    private String address;
    @Column(name = "total_floors")
    private int totalFloor;
    @Column(name = "total_apartments")
    private int totalApartment;
}
