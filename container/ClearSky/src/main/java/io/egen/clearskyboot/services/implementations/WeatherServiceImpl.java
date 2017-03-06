package io.egen.clearskyboot.services.implementations;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
		return weatherRepository.findAll();
	}

	@Override
	public Reading create(Reading reading) {
		return weatherRepository.save(reading);
	}

	@Override
	public List<String> findDistinctCities() {
		return weatherRepository.findDistinctCities();
	}

	@Override
	public Reading findLatestWeatherByCity(String city) {
		Optional<Reading> optionalReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampDesc(city));
		if(optionalReading.isPresent()) return optionalReading.get();
		throw new NotFoundException("No latest data found for city : " + city);
	}

	@Override
	public JsonNode findLatestWeatherPropertyByCity(String city, String property) {
		
		// Create an object mapper, object node and put city name in the node
		ObjectMapper objectMapper = new ObjectMapper();
		
		Optional<Reading> optionalLatestReading = Optional.ofNullable(weatherRepository.findTop1ByCityOrderByTimestampDesc(city));
		if(optionalLatestReading.isPresent()) {
			
			Reading latestReading = optionalLatestReading.get();
			boolean propertyNotFound = true;
			BeanInfo beanInfo;
			
			Optional<BeanInfo> optionalBeanInfo;
			try {
				optionalBeanInfo = Optional.of(Introspector.getBeanInfo(Reading.class));
				if(optionalBeanInfo.isPresent()) beanInfo = optionalBeanInfo.get();
				else throw new NotFoundException("BeanInfo instance not found...");
			} catch (IntrospectionException e1) {
				throw new InternalServerError(e1.getMessage());
			}
			
			// Get attributes of class using getBeanInfo
			// Iterate through attributes to find specific property
			// Ex. temperature
			for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
				
				// If found, add property to objectNode and return objectNode (as JsonNode)
			    if(propertyDesc.getName().equalsIgnoreCase(property)) {
			    	
			    	propertyNotFound = false;
			    	
					ObjectNode resultNode = objectMapper.createObjectNode();
					resultNode.put("city", city);
					
					Optional<Method> optionalMethod = Optional.of(propertyDesc.getReadMethod());
					optionalMethod.ifPresent(m -> {
						Optional<String> optionalPropertyValue;
						try {
							optionalPropertyValue = Optional.of(String.valueOf(m.invoke(latestReading)));
							if (optionalPropertyValue.isPresent()) resultNode.put(property, optionalPropertyValue.get());
					    	else resultNode.put(property, "Value not found");
						} catch (Exception e) {
							throw new NotFoundException("Value for property: " + property + ", not found");
						}
					});
					
					return resultNode;
			    } // End of IF
			} // End of FOR
			
			// If the property we're searching is an invalid one, throw NotFoundException
			if(propertyNotFound) throw new NotFoundException("Property " + property + " does not exist!");
		} else throw new NotFoundException("No data found for city: " + city);
		return objectMapper.createObjectNode(); // Empty Node
	}
	
	@Override
	public JsonNode findAvgWeatherPropertyByCityAndGrain(String city, String property, String grain) {
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
		} else throw new NotFoundException("Invalid grain: Should be either <1h or hourly> or <1d or daily>");
	}
	
	private JsonNode calculateAvg(String city, boolean hourly, double quantity, String property) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode resultNode = objectMapper.createObjectNode();
		resultNode.put("city", city);
		
		String tag = hourly ? "hourly" : "daily";
		if(property.equalsIgnoreCase("ALL")) {
			
			double averageHumidity = weatherRepository.findAvgHumidityByCity(city);
			resultNode.put(tag + "-average-humidity", averageHumidity / quantity);
			
			double averageTemperature = weatherRepository.findAvgTemperatureByCity(city);
			resultNode.put(tag + "-average-temperature", averageTemperature / quantity);
			
			double averagePressure = weatherRepository.findAvgPressureByCity(city);
			resultNode.put(tag + "-average-Pressure", averagePressure / quantity);
			
			double averageWindSpeed = weatherRepository.findAvgWindSpeedByCity(city);
			resultNode.put(tag + "-average-wind-speed", averageWindSpeed / quantity);
			
			double averageWindDegree = weatherRepository.findAvgWindDegreeByCity(city);
			resultNode.put(tag + "-average-wind-degree", averageWindDegree / quantity);
			
		} else {
			switch(property) {
				case "humidity": 	double averageHumidity = weatherRepository.findAvgHumidityByCity(city);
									resultNode.put(tag + "-average-humidity", averageHumidity / quantity);
									break;
				case "temperature":	double averageTemperature = weatherRepository.findAvgTemperatureByCity(city);
									resultNode.put(tag + "-average-temperature", averageTemperature / quantity);
									break;
				case "pressure":	double averagePressure = weatherRepository.findAvgPressureByCity(city);
									resultNode.put(tag + "-average-Pressure", averagePressure / quantity);
									break;
				case "windSpeed":	double averageWindSpeed = weatherRepository.findAvgWindSpeedByCity(city);
									resultNode.put(tag + "-average-wind-speed", averageWindSpeed / quantity);
									break;
				case "windDegree":	double averageWindDegree = weatherRepository.findAvgWindDegreeByCity(city);
									resultNode.put(tag + "-average-wind-degree", averageWindDegree / quantity);
									break;
			}
		}		
		return resultNode;
	}
}
