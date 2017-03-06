package io.egen.clearskyboot.services;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.egen.clearskyboot.entities.Reading;

public interface WeatherService {
	public List<Reading> findAll();
	public Reading create(Reading reading);
	public List<String> findDistinctCities();
	public Reading findLatestWeatherByCity(String city);
	public JsonNode findLatestWeatherPropertyByCity(String city, String property);	
	public JsonNode findAvgWeatherPropertyByCityAndGrain(String city, String property, String grain);
}
