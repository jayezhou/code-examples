package sample.mybatis.xml.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sample.mybatis.xml.domain.CityHotel;
import sample.mybatis.xml.service.HotelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hotel")
public class HotelController {
	
	@Autowired
	private HotelService hotelService;
	
	@GetMapping("/state/{state}")
	public List<CityHotel> findCityHotelsByState(@PathVariable String state) {
		return hotelService.findCityHotelsByState(state);
	}

}
