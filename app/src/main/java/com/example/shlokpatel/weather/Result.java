package com.example.shlokpatel.weather;

public class Result {
    String city,country,condition,imageUrl;
    Double temp,humidity;

    public Result(String city, String country, String condition, String imageUrl, Double temp, Double humidity) {
        this.city = city;
        this.country = country;
        this.condition = condition;
        this.imageUrl = imageUrl;
        this.temp = temp;
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCondition() {
        return condition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getTemp() {
        return temp;
    }

    public Double getHumidity() {
        return humidity;
    }
}
