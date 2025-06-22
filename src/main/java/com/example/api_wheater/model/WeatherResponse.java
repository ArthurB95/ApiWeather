package com.example.api_wheater.model;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    public String zipCode;
    public String location;
    public double latitude;
    public double longitude;
    public double currentTemperature;
    public double minTemperature;
    public double maxTemperature;
    public List<Forecast> forecast;
    public boolean fromCache;

    public static class Forecast {
        public String day;
        public double min;
        public double max;
    }
}
