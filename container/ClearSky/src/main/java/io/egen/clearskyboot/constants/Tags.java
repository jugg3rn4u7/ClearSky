package io.egen.clearskyboot.constants;

public final class Tags {
	
	public static final String READINGS = "readings";
	
	public static final String FIND_ALL_READINGS = "Find all readings";
	public static final String FIND_ALL_READINGS_DESC = "Returns a list of all weather readings";
	
	public static final String CREATE_READING = "Create a weather reading";
	public static final String CREATE_READING_DESC = "Creates a weather reading in the app with unique reading ID";
	
	public static final String FIND_DISTINCT_CITIES = "Find all cities with a reported reading";
	public static final String FIND_DISTINCT_CITIES_DESC = "Returns a unique list of cities from all readings";
	
	public static final String FIND_LATEST_WEATHER = "Find the latest weather for a city based on optional properties";
	public static final String FIND_LATEST_WEATHER_DESC = "Returns a JSON object of latest weather properties for a city. If optional property parameter is specified, then specific property will be shown. If optional grain parameter is specified, then based on hourly and daily values, averaged values will be shown.";
}
