package sample.mybatis.xml.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sample.mybatis.xml.domain.City;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/city")
public class CityController {
	
	@GetMapping("/all")
	public List<City> all() {
		return new ArrayList<City>();
	}

}
