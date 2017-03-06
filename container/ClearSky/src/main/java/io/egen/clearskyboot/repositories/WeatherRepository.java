package io.egen.clearskyboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import io.egen.clearskyboot.constants.Queries;
import io.egen.clearskyboot.entities.Reading;

public interface WeatherRepository extends Repository<Reading, String>{
	
	public List<Reading> findAll();
	public Reading save(Reading reading); //upsert
	
	@Query(Queries.FIND_DISTINCT_CITIES_QUERY)
	public List<String> findDistinctCities();
	
	public Reading findTop1ByCityOrderByTimestampAsc(String city);
	public Reading findTop1ByCityOrderByTimestampDesc(String city);
	
	@Query(Queries.FIND_HOURS_BETWEEN_TIMESTAMPS_QUERY)
	public double findHoursBetweenTimestamps(@Param("firstTimestamp") String firstTimestamp,
											 @Param("lastTimestamp") String lastTimestamp, 
											 @Param("cityName") String cityName);
	
	@Query(Queries.FIND_DAYS_BETWEEN_TIMESTAMPS_QUERY)
	public double findDaysBetweenTimestamps(@Param("firstTimestamp") String firstTimestamp,
											 @Param("lastTimestamp") String lastTimestamp, 
											 @Param("cityName") String cityName);
	
	@Query(Queries.FIND_AVG_HUMIDITY_QUERY)
	public double findAvgHumidityByCity(@Param("cityName") String cityName);
	
	@Query(Queries.FIND_AVG_PRESSURE_QUERY)
	public double findAvgPressureByCity(@Param("cityName") String cityName);
	
	@Query(Queries.FIND_AVG_TEMPERATURE_QUERY)
	public double findAvgTemperatureByCity(@Param("cityName") String cityName);
	
	@Query(Queries.FIND_AVG_WINDSPEED_QUERY)
	public double findAvgWindSpeedByCity(@Param("cityName") String cityName);
	
	@Query(Queries.FIND_AVG_WINDDEGREE_QUERY)
	public double findAvgWindDegreeByCity(@Param("cityName") String cityName);
}
