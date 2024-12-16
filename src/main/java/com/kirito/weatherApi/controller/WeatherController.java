package com.kirito.weatherApi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirito.weatherApi.common.pojos.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherController {

    private static final String baseUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s/%s/%s/?key=%s";

    @Value("${WEATHER_API_KEY}")
    private String API_KEY;

    @GetMapping("/{address}")
    @Cacheable(value = "weather_single", key = "#address")
    public Weather getWeather(@PathVariable String address) {
        String url = String.format(baseUrl,
                address, LocalDate.now(), "", API_KEY);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String weather = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(weather);

            JsonNode day = rootNode.path("days").get(0);
            DecimalFormat df = new DecimalFormat("#.##");

            String address1 = rootNode.get("address").asText();
            String datetime = day.get("datetime").asText();

            String tempmax = df.format(fahrenheitToCelsius(day.get("tempmax").asDouble()));
            String tempmin = df.format(fahrenheitToCelsius(day.get("tempmin").asDouble()));
            String temp = df.format(fahrenheitToCelsius(day.get("temp").asDouble()));
            String feelslikemax = df.format(fahrenheitToCelsius(day.get("feelslikemax").asDouble()));
            String feelslikemin = df.format(fahrenheitToCelsius(day.get("feelslikemin").asDouble()));
            String feelslike = df.format(fahrenheitToCelsius(day.get("feelslike").asDouble()));

            String description = day.get("description").asText();

            return new Weather(
                    address1,
                    datetime,
                    tempmax,
                    tempmin,
                    temp,
                    feelslikemax,
                    feelslikemin,
                    feelslike,
                    description);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{address}/{startTime}/{endTime}")
    @Cacheable(value = "weather", key = "#address+startTime+endTime")
    public List<Weather> getWeather(@PathVariable String address, @PathVariable String startTime, @PathVariable String endTime) {
        String url = String.format(baseUrl,
                address, startTime, endTime, API_KEY);
        try {
            return getWeathers(url);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{address}/all")
    @Cacheable(value = "weatherAll", key = "#address")
    public List<Weather> getWeatherAll(@PathVariable String address){
        String url = String.format(
                baseUrl,
                address,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(13),
                API_KEY);
        try {
            return getWeathers(url);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Weather> getWeathers(String url) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String weather = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(weather);

        List<Weather> result = new ArrayList<>();

        JsonNode days = rootNode.path("days");
        DecimalFormat df = new DecimalFormat("#.##");
        for (JsonNode day : days) {
            String address1 = rootNode.get("address").asText();
            String datetime = day.get("datetime").asText();

            String tempmax = df.format(fahrenheitToCelsius(day.get("tempmax").asDouble()));
            String tempmin = df.format(fahrenheitToCelsius(day.get("tempmin").asDouble()));
            String temp = df.format(fahrenheitToCelsius(day.get("temp").asDouble()));
            String feelslikemax = df.format(fahrenheitToCelsius(day.get("feelslikemax").asDouble()));
            String feelslikemin = df.format(fahrenheitToCelsius(day.get("feelslikemin").asDouble()));
            String feelslike = df.format(fahrenheitToCelsius(day.get("feelslike").asDouble()));
            String description = day.get("description").asText();

            result.add(new Weather(
                    address1,
                    datetime,
                    tempmax,
                    tempmin,
                    temp,
                    feelslikemax,
                    feelslikemin,
                    feelslike,
                    description));
        }
        return result;
    }

    // 将华氏度转换为摄氏度
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
}
