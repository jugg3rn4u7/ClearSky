package io.egen.clearskyboot.constants;

public final class Queries {
	
	public static final String FIND_DISTINCT_CITIES_QUERY = 
			"SELECT DISTINCT r.city FROM Reading r";
	
	public static final String FIND_HOURS_BETWEEN_TIMESTAMPS_QUERY = 
			"SELECT ABS(HOUR(TIMEDIFF(:firstTimestamp, :lastTimestamp))) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_DAYS_BETWEEN_TIMESTAMPS_QUERY = 
			"SELECT ABS(DATEDIFF(:firstTimestamp, :lastTimestamp)) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_AVG_HUMIDITY_QUERY = 
			"SELECT AVG(r.humidity) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_AVG_PRESSURE_QUERY = 
			"SELECT AVG(r.pressure) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_AVG_TEMPERATURE_QUERY = 
			"SELECT AVG(r.temperature) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_AVG_WINDSPEED_QUERY = 
			"SELECT AVG(r.windSpeed) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_AVG_WINDDEGREE_QUERY = 
			"SELECT AVG(r.windDegree) FROM Reading r WHERE r.city = :cityName";
}
