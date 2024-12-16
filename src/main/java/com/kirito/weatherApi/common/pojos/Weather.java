package com.kirito.weatherApi.common.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class Weather implements Serializable {
    private String address;
    private String datetime;
    private String tempmax;
    private String tempmin;
    private String temp;
    private String feelslikemax;
    private String feelslikemin;
    private String feelslike;
    private String description;

    public Weather(String address, String datetime, String tempmax, String tempmin, String temp, String feelslikemax, String feelslikemin, String feelslike, String description) {
        String unit = "°C";
        this.address = address;
        this.datetime = datetime;
        this.tempmax = tempmax + unit;
        this.tempmin = tempmin + unit;
        this.temp = temp + unit;
        this.feelslikemax = feelslikemax + unit;
        this.feelslikemin = feelslikemin + unit;
        this.feelslike = feelslike + unit;
        this.description = description;
    }
}
