package io.egen.clearsky.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.egen.clearsky.constants.HTTPStatus;
import io.egen.clearsky.constants.Tags;
import io.egen.clearsky.constants.URI;
import io.egen.clearsky.entities.Reading;
import io.egen.clearsky.exceptions.BadRequestException;
import io.egen.clearsky.services.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = URI.READINGS)
@Api(tags = Tags.READINGS)
public class WeatherController {
	
	@Autowired
	private WeatherService weatherService;
	
	/**
	 * Description: Get all readings
	 * @return List<Reading>
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = Tags.FIND_ALL_READINGS, notes = Tags.FIND_ALL_READINGS_DESC)
	@ApiResponses(value = { 
							@ApiResponse(code = HTTPStatus.SUCCESS_OK_CODE, message = HTTPStatus.SUCCESS_OK_MSG),
							@ApiResponse(code = HTTPStatus.SERVER_ERR_INTERNAL_CODE, message = HTTPStatus.SERVER_ERR_INTERNAL_MSG) 
						  })
	public List<Reading> findAll() {
		return weatherService.findAll();
	}
	
	/**
	 * Description: Create a new Reading
	 * @param reading
	 * @return Reading
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = Tags.CREATE_READING, notes = Tags.CREATE_READING_DESC)
	@ApiResponses(value = { 
							@ApiResponse(code = HTTPStatus.SUCCESS_OK_CODE, message = HTTPStatus.SUCCESS_OK_MSG),
							@ApiResponse(code = HTTPStatus.CLIENT_ERR_BAD_REQUEST_CODE, message = HTTPStatus.CLIENT_ERR_BAD_REQUEST_MSG),
							@ApiResponse(code = HTTPStatus.SERVER_ERR_INTERNAL_CODE, message = HTTPStatus.SERVER_ERR_INTERNAL_MSG) 
						  })
	public Reading create(@RequestBody Reading reading) {
		return weatherService.create(reading);
	}
	
	/**
	 * Description: Get list of distinct cities.
	 * @return List<Reading>
	 */
	@RequestMapping(method = RequestMethod.GET, value = URI.REPORTED_CITIES)
	@ApiOperation(value = Tags.FIND_DISTINCT_CITIES, notes = Tags.FIND_DISTINCT_CITIES_DESC)
	@ApiResponses(value = { 
							@ApiResponse(code = HTTPStatus.SUCCESS_OK_CODE, message = HTTPStatus.SUCCESS_OK_MSG),
							@ApiResponse(code = HTTPStatus.SERVER_ERR_INTERNAL_CODE, message = HTTPStatus.SERVER_ERR_INTERNAL_MSG) 
						  })
	public List<String> findDistinctCities() {
		return weatherService.findDistinctCities();
	}
	
	/**
	 * Description: Get the latest weather of a city. 
	 * Optional parameters: Property is used to get specific property of weather like temperature or humidity etc.. 
	 * 						Grain is used to get hourly or daily averaged data.
	 * @param city
	 * @param property (Optional)
	 * @param grain	(Optional)
	 * @return JsonNode
	 */
	@RequestMapping(method = RequestMethod.GET, value = URI.LATEST_WEATHER)
	@ApiOperation(value = Tags.FIND_LATEST_WEATHER, notes = Tags.FIND_LATEST_WEATHER_DESC)
	@ApiResponses(value = { 
							@ApiResponse(code = HTTPStatus.SUCCESS_OK_CODE, message = HTTPStatus.SUCCESS_OK_MSG),
							@ApiResponse(code = HTTPStatus.CLIENT_ERR_BAD_REQUEST_CODE, message = HTTPStatus.CLIENT_ERR_BAD_REQUEST_MSG),
							@ApiResponse(code = HTTPStatus.CLIENT_ERR_NOT_FOUND_CODE, message = HTTPStatus.CLIENT_ERR_NOT_FOUND_MSG),
							@ApiResponse(code = HTTPStatus.SERVER_ERR_INTERNAL_CODE, message = HTTPStatus.SERVER_ERR_INTERNAL_MSG) 
						  })
	public JsonNode findLatestWeather( @RequestParam(value = "city", required = true) String city,
								 @RequestParam(value = "property", required = false) String property,
								 @RequestParam(value = "grain", required = false) String grain) {
		
		// Check for Nullable
		Optional<String> optionalProperty = Optional.ofNullable(property);
		Optional<String> optionalGrain = Optional.ofNullable(grain);
		
		// If both property and grain are present, then get latest weather by city, property and gain
		// Else If only property is present, then get latest weather by city and property
		// Else If only grain is present, then 
		//		If grain is hourly, then get hourly average weather by city
		//		Else If grain is daily, then get daily average weather by city
		// Otherwise, Get all latest weather properties by city 
		if(optionalProperty.isPresent() && optionalGrain.isPresent()) 
			return weatherService.findLatestWeatherPropertyByCityAndGrain(city, property, grain); 
		else if(optionalProperty.isPresent()) return weatherService.findLatestWeatherPropertyByCity(city, property);
		else if(optionalGrain.isPresent()) {
			if(optionalGrain.get().equalsIgnoreCase("hourly") || optionalGrain.get().equalsIgnoreCase("1h")) return weatherService.findHourlyAvgWeatherByCity(city);
			else if(optionalGrain.get().equalsIgnoreCase("daily") || optionalGrain.get().equalsIgnoreCase("1d")) return weatherService.findDailyAvgWeatherByCity(city);
			else {
				throw new BadRequestException("Invalid grain value: Should be either <1d or daily> or <1h or hourly>");
			}
		} else return weatherService.findLatestWeatherByCity(city);
	}
}
