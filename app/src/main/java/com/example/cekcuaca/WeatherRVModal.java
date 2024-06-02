package com.example.cekcuaca;

public class WeatherRVModal {

    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String description; // Add this field if needed

    public WeatherRVModal(String time, String temperature, String icon, String windSpeed, String description) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.description = description; // Initialize the new field
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
