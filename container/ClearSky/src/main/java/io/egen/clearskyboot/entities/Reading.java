package io.egen.clearskyboot.entities;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(Include.NON_DEFAULT)
public class Reading {
	
	@Id
	private String readingId;
	private String city;
	private String description;
	private int humidity;
	private int pressure;
	private int temperature;
	private double windSpeed;
	private int windDegree;
	private Timestamp timestamp;
	
	// Derived Properties
	@Transient
	private double hourly_average_humidity;
	@Transient
	private double hourly_average_temperature;
	@Transient
	private double hourly_average_pressure;
	@Transient
	private double hourly_average_wind_speed;
	@Transient
	private double hourly_average_wind_degree;
		
	@Transient
	private double daily_average_humidity;
	@Transient
	private double daily_average_temperature;
	@Transient
	private double daily_average_pressure;
	@Transient
	private double daily_average_wind_speed;
	@Transient
	private double daily_average_wind_degree;
	
	public Reading() {
		setReadingId(UUID.randomUUID().toString());
	}
	
	public String getReadingId() {
		return readingId;
	}
	public void setReadingId(String readingId) {
		this.readingId = readingId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public int getPressure() {
		return pressure;
	}
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public int getWindDegree() {
		return windDegree;
	}
	public void setWindDegree(int windDegree) {
		this.windDegree = windDegree;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public double getHourly_average_humidity() {
		return hourly_average_humidity;
	}
	public void setHourly_average_humidity(double hourly_average_humidity) {
		this.hourly_average_humidity = hourly_average_humidity;
	}
	public double getHourly_average_temperature() {
		return hourly_average_temperature;
	}
	public void setHourly_average_temperature(double hourly_average_temperature) {
		this.hourly_average_temperature = hourly_average_temperature;
	}
	public double getHourly_average_pressure() {
		return hourly_average_pressure;
	}
	public void setHourly_average_pressure(double hourly_average_pressure) {
		this.hourly_average_pressure = hourly_average_pressure;
	}
	public double getHourly_average_wind_speed() {
		return hourly_average_wind_speed;
	}
	public void setHourly_average_wind_speed(double hourly_average_wind_speed) {
		this.hourly_average_wind_speed = hourly_average_wind_speed;
	}
	public double getHourly_average_wind_degree() {
		return hourly_average_wind_degree;
	}
	public void setHourly_average_wind_degree(double hourly_average_wind_degree) {
		this.hourly_average_wind_degree = hourly_average_wind_degree;
	}
	public double getDaily_average_humidity() {
		return daily_average_humidity;
	}
	public void setDaily_average_humidity(double daily_average_humidity) {
		this.daily_average_humidity = daily_average_humidity;
	}
	public double getDaily_average_temperature() {
		return daily_average_temperature;
	}
	public void setDaily_average_temperature(double daily_average_temperature) {
		this.daily_average_temperature = daily_average_temperature;
	}
	public double getDaily_average_pressure() {
		return daily_average_pressure;
	}
	public void setDaily_average_pressure(double daily_average_pressure) {
		this.daily_average_pressure = daily_average_pressure;
	}
	public double getDaily_average_wind_speed() {
		return daily_average_wind_speed;
	}
	public void setDaily_average_wind_speed(double daily_average_wind_speed) {
		this.daily_average_wind_speed = daily_average_wind_speed;
	}
	public double getDaily_average_wind_degree() {
		return daily_average_wind_degree;
	}
	public void setDaily_average_wind_degree(double daily_average_wind_degree) {
		this.daily_average_wind_degree = daily_average_wind_degree;
	}
	
	@Override
	public String toString() {
		return String.format("{ \"readingId\":\"%s\", \"city\":\"%s\", \"description\":\"%s\", \"humidity\":%d, \"temperature\":%d, \"pressure\":%d, \"windSpeed\":%f, \"windDegree\":%d }", 
				readingId, city, description, humidity, temperature, pressure, windSpeed, windDegree);
	}
	
	@JsonProperty("wind")
	private void unpackNameFromNestedObject(Map<String, String> wind) {
	    setWindSpeed(Double.parseDouble(wind.get("speed")));
	    setWindDegree(Integer.parseInt(wind.get("degree")));
	}
}
