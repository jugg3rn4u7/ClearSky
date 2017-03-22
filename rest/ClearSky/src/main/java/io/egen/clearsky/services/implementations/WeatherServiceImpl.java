package io.egen.clearsky.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;

import io.egen.clearsky.entities.Reading;
import io.egen.clearsky.repositories.WeatherRepository;
import io.egen.clearsky.services.WeatherService;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	private WeatherRepository weatherRepository;

	@Override
	public List<Reading> findAll() {
		return weatherRepository.findAll();
	}

	@Override
	public Reading create(Reading reading) {
		return weatherRepository.create(reading);
	}

	@Override
	public List<String> findDistinctCities() {
		return weatherRepository.findDistinctCities();
	}

	@Override
	public JsonNode findLatestWeatherByCity(String city) {
		return weatherRepository.findLatestWeatherByCity(city);
	}

	@Override
	public JsonNode findLatestWeatherPropertyByCity(String city, String property) {
		return weatherRepository.findLatestWeatherPropertyByCity(city, property);
	}

	@Override
	public JsonNode findHourlyAvgWeatherByCity(String city) {
		return weatherRepository.findHourlyAvgWeatherByCity(city);
	}

	@Override
	public JsonNode findDailyAvgWeatherByCity(String city) {
		return weatherRepository.findDailyAvgWeatherByCity(city);
	}

	@Override
	public JsonNode findLatestWeatherPropertyByCityAndGrain(String city, String property, String grain) {
		return weatherRepository.findLatestWeatherPropertyByCityAndGrain(city, property, grain);
	}

}
