package io.egen.clearskyboot.services;

import java.util.List;

import io.egen.clearskyboot.entities.Reading;

public interface WeatherService {
	public List<Reading> findAll();
	public Reading create(Reading reading);
	public List<String> findDistinctCities();
	public Reading findLatestWeatherByCity(String city);
	public Reading findLatestWeatherPropertyByCity(String city, String property);	
	public Reading findAvgWeatherPropertyByCityAndGrain(String city, String property, String grain);
}
