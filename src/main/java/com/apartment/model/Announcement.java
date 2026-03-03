package com.apartment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Annoucement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annoucement_id")
    private Long id;

    private String title;
    private String content;
    private int priority;

    @Column(name = "create_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;


}
