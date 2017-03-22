package io.egen.clearsky.services;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.egen.clearsky.entities.Reading;

public interface WeatherService {
	public List<Reading> findAll();
	public Reading create(Reading reading);
	public List<String> findDistinctCities();
	public JsonNode findLatestWeatherByCity(String city);
	public JsonNode findLatestWeatherPropertyByCity(String city, String property);
	public JsonNode findHourlyAvgWeatherByCity(String city);
	public JsonNode findDailyAvgWeatherByCity(String city);
	public JsonNode findLatestWeatherPropertyByCityAndGrain(String city, String property, String grain);
}
