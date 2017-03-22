package io.egen.clearsky.repositories.implementations;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.egen.clearsky.constants.Queries;
import io.egen.clearsky.entities.Reading;
import io.egen.clearsky.exceptions.InternalServerError;
import io.egen.clearsky.exceptions.NotFoundException;
import io.egen.clearsky.repositories.WeatherRepository;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Description: Finds all the readings stored in database. 
	 * 				Returns a list of Reading.class instances. 
	 * @param none
	 * @return List<Reading>
	 */
	@Override
	public List<Reading> findAll() {
		return entityManager.createNamedQuery(
				Queries.FIND_ALL_READINGS, Reading.class)
				.getResultList();
	}
	
	/**
	 * Description: Creates a Reading.class record in Database.
	 * 				Returns the create Reading.class instance.
	 * @param Reading reading
	 * @return Reading
	 */
	@Override
	public Reading create(Reading reading) {
		entityManager.persist(reading);
		return reading;
	}
	
	/**
	 * Description: Finds a unique list of cities in Database.
	 * 				Returns a list of String objects.
	 * @param none
	 * @return List<String>
	 */
	@Override
	public List<String> findDistinctCities() {
		return entityManager.createNamedQuery(
                 Queries.FIND_DISTINCT_CITIES, String.class)
                .getResultList();
	}
	
	/**
	 * Description: Finds the latest weather reading for a city by timestamp.
	 * 				Returns a JsonNode object containing latest Reading.
	 * @param String city
	 * @return JsonNode
	 */
	@Override
	public JsonNode findLatestWeatherByCity(String city) {
		// Create an object mapper
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Create Named Query
		TypedQuery<Reading> query = 
				entityManager.createNamedQuery(Queries.FIND_LATEST_WEATHER_BY_CITY, Reading.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		
		// If not data for city is found then throw NotFoundException
		// Otherwise, return data as JsonNode
		if(query.getResultList().isEmpty()) {
			throw new NotFoundException("Data not found for city: " + city);
		} else {
			Reading latestReading = query.getResultList().get(0);
			return objectMapper.convertValue(latestReading, JsonNode.class);
		}
	}
	
	/**
	 * Description: Find the latest weather property of a city.
	 * 				Returns a JsonNode object containing city and property values.
	 * @param String city
	 * @param String property
	 * @return JsonNode
	 */
	@Override
	public JsonNode findLatestWeatherPropertyByCity(String city, String property) {
		
		// Create an object mapper, object node and put city name in the node
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode resultNode = objectMapper.createObjectNode();
		resultNode.put("city", city);
		
		// Create Named Query
		TypedQuery<Reading> query = 
				entityManager.createNamedQuery(Queries.FIND_LATEST_WEATHER_BY_CITY, Reading.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		
		// If not data for city is found then throw NotFoundException
		// Otherwise, return specific property data as JsonNode
		if(query.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for property : " + property);
		}
		else {
			// Get latest Reading
			Reading latestWeatherReading = query.getResultList().get(0);
			BeanInfo beanInfo;
			try {
				// propertyNotFound flag
				boolean propertyNotFound = true;
				
				// Get attributes of class using getBeanInfo
				beanInfo = Introspector.getBeanInfo(Reading.class);
				
				// Iterate through attributes to find specific property
				// Ex. temperature
				for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
					
					// If found, add property to objectNode and return objectNode (as JsonNode)
				    if(propertyDesc.getName().equalsIgnoreCase(property)) {
				    	propertyNotFound = false;
				    	resultNode.put(property, String.valueOf(propertyDesc.getReadMethod().invoke(latestWeatherReading)));
				    	return resultNode;
				    }
				} 
				
				// If the property we're searching is an invalid one, throw NotFoundException
				if(propertyNotFound) throw new NotFoundException("Property " + property + " does not exist!");
				
			} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				
				// Internal Server Error occured, throw InternalServerError
				throw new InternalServerError("Something went wrong ! Try again...", e);
			}
			return null;
		}
	}
	
	/**
	 * Description: Calculates hourly averages for the properties humidity, pressure, temperature, windSpeed and windDegree, for a given city.
	 * 				Returns the result as a JsonNode object. 
	 * @param String city
	 * @return JsonNode
	 */
	@Override
	public JsonNode findHourlyAvgWeatherByCity(String city) {
		/**
		 * Create a JSON object to return
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode resultNode = objectMapper.createObjectNode();
		resultNode.put("city", city);
		
		/**
		 * Get the timestamp of the first reading recorded for a city 
		 */
		TypedQuery<Date> query1 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_FIRST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * If there is no first reading for the city, it means data hasn't been recorded for this city yet !
		 * Return JSON object with this information
		 */
		if(query1.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date firstWeatherTimestamp = query1.getResultList().get(0);
		
		/**
		 * Get the timestamp of the last reading recorded for a city 
		 */
		TypedQuery<Date> query2 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_LAST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * Fail case. If result list is empty, return JSON info object
		 */
		if(query2.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date lastWeatherTimestamp = query2.getResultList().get(0);
		
		/**
		 * Get the number of hours elapsed between the first reading and last reading for a city
		 */
		TypedQuery<Integer> query3 = 
				entityManager.createNamedQuery(Queries.FIND_HOURS_BETWEEN_TIMESTAMPS, Integer.class)
							 .setParameter("firstTimestamp", firstWeatherTimestamp.toString())
							 .setParameter("lastTimestamp", lastWeatherTimestamp.toString())
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * Fail case. If result list is empty, return JSON info object
		 */
		if(query3.getResultList().isEmpty()) {
			throw new NotFoundException("We need more data to calculate hourly averaged weather for city " + city);
		} 
		
		Integer numberOfHours = query3.getResultList().get(0);
		
		/**
		 * If the number of hours elapsed between the first reading and last reading for a city is zero,
		 * hourly average cannot be calculated, since we need readings for atleast an hour
		 */
		if(numberOfHours == 0) {
			throw new NotFoundException("We need more data to calculate hourly averaged weather for city " + city);
		} 
		
		/**
		 * Calculate hourly average for properties: humidity, pressure, temperature, windSpeed and windDegree.
		 * Formula 1: hourly average for a property = 
		 * 					( simple average for a property ) / number of hours between first and last reading
		 * Formula 2: simple average for a property =
		 * 					sum of values of the property / number of values
		 */
		Arrays.asList( "humidity", "pressure", "temperature", "windSpeed", "windDegree" )
			  .stream()
			  .forEach((property) -> {
				  resultNode.put("hourly-average-" + property, (Double) getAvgByPropertyAndCity(property, city) / numberOfHours);   
			  });
		
		/**
		 * Return a JSON object with hourly averages for properties: humidity, pressure, temperature, windSpeed and windDegree.
		 */
		return resultNode;
	}
	
	/**
	 * Description: Calculates and returns a simple average of a property for a given city. 
	 * @param String property
	 * @param String city
	 * @return Double
	 */
	private Double getAvgByPropertyAndCity(String property, String city) {
		try {
			String jpqlQuery = "SELECT AVG(r." + property + ") FROM Reading r WHERE r.city = '" + city + "'";
			return entityManager.createQuery(jpqlQuery, Double.class).getResultList().get(0);
		} catch(Exception e) {
			
			// Internal Server Error occured, throw InternalServerError
			throw new InternalServerError("Cannot calculate average for property: " + property + " for city: " + city, e);
		}
	}
	
	/**
	 * Description: Calculates daily averages for the properties humidity, pressure, temperature, windSpeed and windDegree,
	 * 				for a given city.
	 * 				Returns the result as a JsonNode object. 
	 * @param String city
	 * @return JsonNode
	 */
	@Override
	public JsonNode findDailyAvgWeatherByCity(String city) {
		/**
		 * Create a JSON object to return
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode resultNode = objectMapper.createObjectNode();
		resultNode.put("city", city);
		
		/**
		 * Get the timestamp of the first reading recorded for a city 
		 */
		TypedQuery<Date> query1 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_FIRST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * If there is no first reading for the city, it means data hasn't been recorded for this city yet !
		 * Return JSON object with this information
		 */
		if(query1.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date firstWeatherTimestamp = query1.getResultList().get(0);
		
		/**
		 * Get the timestamp of the last reading recorded for a city 
		 */
		TypedQuery<Date> query2 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_LAST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * Fail case. If result list is empty, return JSON info object
		 */
		if(query2.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date lastWeatherTimestamp = query2.getResultList().get(0);
		
		/**
		 * Get the number of DAYS elapsed between the first reading and last reading for a city
		 */
		TypedQuery<Integer> query3 = 
				entityManager.createNamedQuery(Queries.FIND_DAYS_BETWEEN_TIMESTAMPS, Integer.class)
							 .setParameter("firstTimestamp", firstWeatherTimestamp.toString())
							 .setParameter("lastTimestamp", lastWeatherTimestamp.toString())
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * Fail case. If result list is empty, return JSON info object
		 */
		if(query3.getResultList().isEmpty()) {
			throw new NotFoundException("We need more data to calculate daily averaged weather for city " + city);
		} 
		
		Integer numberOfDays = query3.getResultList().get(0);
		
		/**
		 * If the number of days elapsed between the first reading and last reading for a city is zero,
		 * daily average cannot be calculated, since we need readings for atleast a day
		 */
		if(numberOfDays == 0) {
			throw new NotFoundException("We need more data to calculate daily averaged weather for city " + city);
		} 
		
		/**
		 * Calculate daily average for properties: humidity, pressure, temperature, windSpeed and windDegree.
		 * Formula 1: daily average for a property = 
		 * 					( simple average for a property ) / number of days between first and last reading
		 * Formula 2: simple average for a property =
		 * 					sum of values of the property / number of values
		 */
		Arrays.asList( "humidity", "pressure", "temperature", "windSpeed", "windDegree" )
			  .stream()
			  .forEach((property) -> {
				  resultNode.put("daily-average-" + property, (Double) getAvgByPropertyAndCity(property, city) / numberOfDays);   
			  });
		
		/**
		 * Return a JSON object with daily averages for properties: humidity, pressure, temperature, windSpeed and windDegree.
		 */
		return resultNode;
	}
	
	/**
	 * Description: Calculates daily averages for one of the properties like humidity, pressure, temperature, windSpeed and windDegree, 
	 * 				for a given city and a grain (hourly or daily).
	 * 				Returns the result as a JsonNode object. 
	 * @param String city
	 * @param String property
	 * @param String grain
	 * @return JsonNode
	 */
	@Override
	public JsonNode findLatestWeatherPropertyByCityAndGrain(String city, String property, String grain) {
		/**
		 * Create a JSON object to return
		 */
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode resultNode = objectMapper.createObjectNode();
		resultNode.put("city", city);
		
		/**
		 * Get the timestamp of the first reading recorded for a city 
		 */
		TypedQuery<Date> query1 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_FIRST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * If there is no first reading for the city, it means data hasn't been recorded for this city yet !
		 * Return JSON object with this information
		 */
		if(query1.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date firstWeatherTimestamp = query1.getResultList().get(0);
		
		/**
		 * Get the timestamp of the last reading recorded for a city 
		 */
		TypedQuery<Date> query2 = 
				entityManager.createNamedQuery(Queries.FIND_TIMESTAMP_OF_LAST_READING_BY_CITY, Date.class)
							 .setParameter("cityName", city)
							 .setMaxResults(1);
		/**
		 * Fail case. If result list is empty, return JSON info object
		 */
		if(query2.getResultList().isEmpty()) {
			throw new NotFoundException("We don't have data for city : " + city);
		} 
		
		Date lastWeatherTimestamp = query2.getResultList().get(0);
		
		/**
		 * Calculate hourly average or daily average
		 */
		if(grain.equalsIgnoreCase("hourly")) {
			
			/**
			 * Get the number of hours elapsed between the first reading and last reading for a city
			 */
			TypedQuery<Integer> query3 = 
					entityManager.createNamedQuery(Queries.FIND_HOURS_BETWEEN_TIMESTAMPS, Integer.class)
								 .setParameter("firstTimestamp", firstWeatherTimestamp.toString())
								 .setParameter("lastTimestamp", lastWeatherTimestamp.toString())
								 .setParameter("cityName", city)
								 .setMaxResults(1);
			/**
			 * Fail case. If result list is empty, return JSON info object
			 */
			if(query3.getResultList().isEmpty()) {
				throw new NotFoundException("We need more data to calculate hourly averaged weather for city " + city);
			} 
			
			Integer numberOfHours = query3.getResultList().get(0);
			
			/**
			 * If the number of hours elapsed between the first reading and last reading for a city is zero,
			 * hourly average cannot be calculated, since we need readings for atleast an hour
			 */
			if(numberOfHours == 0) {
				throw new NotFoundException("We need more data to calculate hourly averaged weather for city " + city);
			} 
			
			/**
			 * Calculate hourly average for any of the properties: humidity, pressure, temperature, windSpeed and windDegree.
			 * Formula 1: hourly average for a property = 
			 * 					( simple average for a property ) / number of hours between first and last reading
			 * Formula 2: simple average for a property =
			 * 					sum of values of the property / number of values
			 */
			resultNode.put("hourly-average-" + property, (Double) getAvgByPropertyAndCity(property, city) / numberOfHours);
			
			/**
			 * Return a JSON object with hourly averages for properties: humidity, pressure, temperature, windSpeed and windDegree.
			 */
			return resultNode;
			
		
		} else if(grain.equalsIgnoreCase("daily")) {
			
			/**
			 * Get the number of DAYS elapsed between the first reading and last reading for a city
			 */
			TypedQuery<Integer> query3 = 
					entityManager.createNamedQuery(Queries.FIND_DAYS_BETWEEN_TIMESTAMPS, Integer.class)
								 .setParameter("firstTimestamp", firstWeatherTimestamp.toString())
								 .setParameter("lastTimestamp", lastWeatherTimestamp.toString())
								 .setParameter("cityName", city)
								 .setMaxResults(1);
			/**
			 * Fail case. If result list is empty, return JSON info object
			 */
			if(query3.getResultList().isEmpty()) {
				throw new NotFoundException("We need more data to calculate daily averaged weather for city " + city);
			} 
			
			Integer numberOfDays = query3.getResultList().get(0);
			
			/**
			 * If the number of days elapsed between the first reading and last reading for a city is zero,
			 * daily average cannot be calculated, since we need readings for atleast a day
			 */
			if(numberOfDays == 0) {
				throw new NotFoundException("We need more data to calculate daily averaged weather for city " + city);
			} 
			
			/**
			 * Calculate daily average for any of the properties: humidity, pressure, temperature, windSpeed and windDegree.
			 * Formula 1: daily average for a property = 
			 * 					( simple average for a property ) / number of days between first and last reading
			 * Formula 2: simple average for a property =
			 * 					sum of values of the property / number of values
			 */
			resultNode.put("daily-average-" + property, (Double) getAvgByPropertyAndCity(property, city) / numberOfDays); 
			
			/**
			 * Return a JSON object with daily averages for any of the properties: humidity, pressure, temperature, windSpeed and windDegree.
			 */
			return resultNode;		
		}
		return null;
	}
}
