package sample.mybatis.xml.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sample.mybatis.xml.domain.CityHotel;
import sample.mybatis.xml.mapper.HotelMapper;

@Service
public class HotelService {
	
	@Autowired
	private HotelMapper hotelMapper;

	public List<CityHotel> findCityHotelsByState(String state) {
		return hotelMapper.findCityHotelsByState(state);
	}
	
}
