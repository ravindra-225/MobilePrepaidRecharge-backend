package com.techm.mobicom.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer validity;
    private String data;
    private String category; // e.g., POPULAR, VALIDITY, DATA, UNLIMITED_DATA
}