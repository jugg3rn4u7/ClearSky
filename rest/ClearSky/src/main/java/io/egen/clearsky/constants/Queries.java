package io.egen.clearsky.constants;

public final class Queries {
	
	public static final String FIND_ALL_READINGS = "Reading.findAll";
	public static final String FIND_ALL_READINGS_QUERY = "SELECT r FROM Reading r ORDER BY r.timestamp DESC";
	
	public static final String FIND_DISTINCT_CITIES = "Reading.findDistinctCities";
	public static final String FIND_DISTINCT_CITIES_QUERY = "SELECT DISTINCT r.city FROM Reading r";
	
	public static final String FIND_LATEST_WEATHER_BY_CITY = "Reading.findLatestWeatherByCity";
	public static final String FIND_LATEST_WEATHER_BY_CITY_QUERY = "SELECT r FROM Reading r WHERE r.city = :cityName ORDER BY r.timestamp DESC";
	
	public static final String FIND_TIMESTAMP_OF_FIRST_READING_BY_CITY = "Reading.findFirstTimestampByCity";
	public static final String FIND_TIMESTAMP_OF_FIRST_READING_BY_CITY_QUERY = "SELECT r.timestamp FROM Reading r WHERE r.city = :cityName ORDER BY r.timestamp ASC";
	
	public static final String FIND_TIMESTAMP_OF_LAST_READING_BY_CITY = "Reading.findLastTimestampByCity";
	public static final String FIND_TIMESTAMP_OF_LAST_READING_BY_CITY_QUERY = "SELECT r.timestamp FROM Reading r WHERE r.city = :cityName ORDER BY r.timestamp DESC";
	
	public static final String FIND_HOURS_BETWEEN_TIMESTAMPS = "Reading.findHoursBetweenTimestamps";
	public static final String FIND_HOURS_BETWEEN_TIMESTAMPS_QUERY = "SELECT ABS(HOUR(TIMEDIFF(:firstTimestamp, :lastTimestamp))) FROM Reading r WHERE r.city = :cityName";
	
	public static final String FIND_DAYS_BETWEEN_TIMESTAMPS = "Reading.findDaysBetweenTimestamps";
	public static final String FIND_DAYS_BETWEEN_TIMESTAMPS_QUERY = "SELECT ABS(DATEDIFF(:firstTimestamp, :lastTimestamp)) FROM Reading r WHERE r.city = :cityName";
}
