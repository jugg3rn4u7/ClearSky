package io.egen.clearskyboot.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.egen.clearskyboot.entities.Reading;
import io.egen.clearskyboot.exceptions.InternalServerError;
import io.egen.clearskyboot.exceptions.NotFoundException;
import io.egen.clearskyboot.repositories.WeatherRepository;
import io.egen.clearskyboot.services.WeatherService;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	private WeatherRepository weatherRepository;

	@Override
	public List<Reading> findAll() {
		Optional<List<Reading>> optionalData = Optional.ofNullable(weatherRepository.findAll());
		if(optionalData.isPresent()) {
			List<Reading> readings = optionalData.get();
			if(readings.isEmpty()) throw new NotFoundException("No weather readings found !");
			else return readings;
		}
		throw new NotFoundException("No weather readings found !");
	}

	@Override
	public Reading create(Reading reading) {
		try{
			return weatherRepository.save(reading);
		} catch(Exception e) {
			throw new InternalServerError(e.getLocalizedMessage(), e);
		}	
	}

	@Override
	public List<String> findDistinctCities() {
		Optional<List<String>> optionalCities = Optional.ofNullable(weatherRepository.findDistinctCities());
		if(optionalCities.isPresent()) {
			List<String> cityList = optionalCities.get();
			if(cityList.isEmpty()) throw new NotFoundException("No data for cities found !");
			else return cityList;
		}
		throw new NotFoundException("No data for cities found !");
	}

	@Override
	public Reading findLatestWeatherByCity(String city) {
		Optional<Reading> optionalReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampDesc(city));
		if(optionalReading.isPresent()) return optionalReading.get();
		throw new NotFoundException("No data found for city : " + city);
	}

	@Override
	public Reading findLatestWeatherPropertyByCity(String city, String property) {
		Reading weatherPropertyObject = new Reading();
		weatherPropertyObject.setCity(city);
			
		Optional<Reading> optionalLatestReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampDesc(city));
		if(optionalLatestReading.isPresent()) {	
			Reading latestReading = optionalLatestReading.get();
				
			if(property.equalsIgnoreCase("description")) weatherPropertyObject.setDescription(latestReading.getDescription());
			else if(property.equalsIgnoreCase("humidity")) weatherPropertyObject.setHumidity(latestReading.getHumidity());
			else if(property.equalsIgnoreCase("pressure")) weatherPropertyObject.setPressure(latestReading.getPressure());
			else if(property.equalsIgnoreCase("temperature")) weatherPropertyObject.setTemperature(latestReading.getTemperature());
			else if(property.equalsIgnoreCase("windSpeed")) weatherPropertyObject.setWindSpeed(latestReading.getWindSpeed());
			else if(property.equalsIgnoreCase("windDegree")) weatherPropertyObject.setWindDegree(latestReading.getWindDegree());
			else throw new NotFoundException("Property " + property + " does not exist!");
		
		} else throw new NotFoundException("No data found for city: " + city);
		return weatherPropertyObject;
	}
	
	@Override
	public Reading findAvgWeatherPropertyByCityAndGrain(String city, String property, String grain) {
		Optional<Reading> firstReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampAsc(city));
		Optional<Reading> lastReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampDesc(city));
			
		if(!firstReading.isPresent() || !lastReading.isPresent()) throw new NotFoundException("No data found for city : " + city);
			
		if(grain.equalsIgnoreCase("1h") || grain.equalsIgnoreCase("hourly")) {
			double noOfHours = weatherRepository.findHoursBetweenTimestamps(firstReading.get().getTimestamp().toString(), 
																				lastReading.get().getTimestamp().toString(), city); 
			if(noOfHours == 0) throw new NotFoundException("Need atleast an hour's data to calculate hourly average weather for city : " + city);
			return calculateAvg(city, true, noOfHours, property);
		} else if(grain.equalsIgnoreCase("1d") || grain.equalsIgnoreCase("daily")) {
			double noOfDays = weatherRepository.findDaysBetweenTimestamps(firstReading.get().getTimestamp().toString(), 
																		  lastReading.get().getTimestamp().toString(), city); 
			if(noOfDays == 0) throw new NotFoundException("Need atleast day's data to calculate daily average weather for city : " + city);
			return calculateAvg(city, false, noOfDays, property);
		} else throw new InternalServerError("Invalid grain: Should be either <1h or hourly> or <1d or daily>");
	}
	
	private Reading calculateAvg(String city, boolean hourly, double quantity, String property) {
		try{
			Reading derivedWeatherPropertiesObject = new Reading();
			derivedWeatherPropertiesObject.setCity(city);
			
			if(property.equalsIgnoreCase("ALL")) {
				
				double averageHumidity = weatherRepository.findAvgHumidityByCity(city);
				double averageTemperature = weatherRepository.findAvgTemperatureByCity(city);
				double averagePressure = weatherRepository.findAvgPressureByCity(city);
				double averageWindSpeed = weatherRepository.findAvgWindSpeedByCity(city);
				double averageWindDegree = weatherRepository.findAvgWindDegreeByCity(city);
				
				if(hourly) {
					derivedWeatherPropertiesObject.setHourly_average_humidity(averageHumidity / quantity);
					derivedWeatherPropertiesObject.setHourly_average_temperature(averageTemperature / quantity);
					derivedWeatherPropertiesObject.setHourly_average_pressure(averagePressure / quantity);
					derivedWeatherPropertiesObject.setHourly_average_wind_speed(averageWindSpeed / quantity);
					derivedWeatherPropertiesObject.setHourly_average_wind_degree(averageWindDegree / quantity);
				} else {
					derivedWeatherPropertiesObject.setDaily_average_humidity(averageHumidity / quantity);
					derivedWeatherPropertiesObject.setDaily_average_temperature(averageTemperature / quantity);
					derivedWeatherPropertiesObject.setDaily_average_pressure(averagePressure / quantity);
					derivedWeatherPropertiesObject.setDaily_average_wind_speed(averageWindSpeed / quantity);
					derivedWeatherPropertiesObject.setDaily_average_wind_degree(averageWindDegree / quantity);
				}
				
			} else {
				switch(property) {
					case "humidity": 	double averageHumidity = weatherRepository.findAvgHumidityByCity(city);
										if(hourly) derivedWeatherPropertiesObject.setHourly_average_humidity(averageHumidity / quantity);
										else derivedWeatherPropertiesObject.setDaily_average_humidity(averageHumidity / quantity);
										break;
					case "temperature":	double averageTemperature = weatherRepository.findAvgTemperatureByCity(city);
										if(hourly) derivedWeatherPropertiesObject.setHourly_average_temperature(averageTemperature / quantity);
										else derivedWeatherPropertiesObject.setDaily_average_temperature(averageTemperature / quantity);
										break;
					case "pressure":	double averagePressure = weatherRepository.findAvgPressureByCity(city);
										if(hourly) derivedWeatherPropertiesObject.setHourly_average_pressure(averagePressure / quantity);
										else derivedWeatherPropertiesObject.setDaily_average_pressure(averagePressure / quantity);
										break;
					case "windSpeed":	double averageWindSpeed = weatherRepository.findAvgWindSpeedByCity(city);
										if(hourly) derivedWeatherPropertiesObject.setHourly_average_wind_speed(averageWindSpeed / quantity);
										else derivedWeatherPropertiesObject.setDaily_average_wind_speed(averageWindSpeed / quantity);
										break;
					case "windDegree":	double averageWindDegree = weatherRepository.findAvgWindDegreeByCity(city);
										if(hourly) derivedWeatherPropertiesObject.setHourly_average_wind_degree(averageWindDegree / quantity);
										else derivedWeatherPropertiesObject.setDaily_average_wind_degree(averageWindDegree / quantity);
										break;
				}
			}		
			return derivedWeatherPropertiesObject;
		} catch(Exception e) {
			throw new InternalServerError(e.getLocalizedMessage(), e);
		}
	}
}
