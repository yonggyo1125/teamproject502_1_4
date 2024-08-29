package org.g9project4.publicData.tour.controllers;

import lombok.Data;

@Data
public class CurrentLocation {
    private Double lat;
    private Double lon;
    private Integer radius = 1000;
}
