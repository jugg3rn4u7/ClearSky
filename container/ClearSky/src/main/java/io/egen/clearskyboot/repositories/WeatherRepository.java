package io.egen.clearskyboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import io.egen.clearskyboot.entities.Reading;

public interface WeatherRepository extends Repository<Reading, String>{
	
	public List<Reading> findAll();
	public Reading save(Reading reading); //upsert
	
	@Query("SELECT DISTINCT r.city FROM Reading r")
	public List<String> findDistinctCities();
	
	public Reading findTop1ByCityOrderByTimestampAsc(String city);
	public Reading findTop1ByCityOrderByTimestampDesc(String city);
	
	@Query("SELECT ABS(HOUR(TIMEDIFF(:firstTimestamp, :lastTimestamp))) FROM Reading r WHERE r.city = :cityName")
	public double findHoursBetweenTimestamps(@Param("firstTimestamp") String firstTimestamp,
											 @Param("lastTimestamp") String lastTimestamp, 
											 @Param("cityName") String cityName);
	
	@Query("SELECT ABS(DATEDIFF(:firstTimestamp, :lastTimestamp)) FROM Reading r WHERE r.city = :cityName")
	public double findDaysBetweenTimestamps(@Param("firstTimestamp") String firstTimestamp,
											 @Param("lastTimestamp") String lastTimestamp, 
											 @Param("cityName") String cityName);
	
	@Query("SELECT AVG(r.humidity) FROM Reading r WHERE r.city = :cityName")
	public double findAvgHumidityByCity(@Param("cityName") String cityName);
	
	@Query("SELECT AVG(r.pressure) FROM Reading r WHERE r.city = :cityName")
	public double findAvgPressureByCity(@Param("cityName") String cityName);
	
	@Query("SELECT AVG(r.temperature) FROM Reading r WHERE r.city = :cityName")
	public double findAvgTemperatureByCity(@Param("cityName") String cityName);
	
	@Query("SELECT AVG(r.windSpeed) FROM Reading r WHERE r.city = :cityName")
	public double findAvgWindSpeedByCity(@Param("cityName") String cityName);
	
	@Query("SELECT AVG(r.windDegree) FROM Reading r WHERE r.city = :cityName")
	public double findAvgWindDegreeByCity(@Param("cityName") String cityName);
}
