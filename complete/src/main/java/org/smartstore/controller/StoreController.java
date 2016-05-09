package org.smartstore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.smartstore.dao.AddressDao;
import org.smartstore.dao.StoreDao;
import org.smartstore.dao.TemplateDao;
import org.smartstore.model.Address;
import org.smartstore.model.Store;
import org.smartstore.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

	@Autowired
	private StoreDao storeDao;
	
	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private TemplateDao templateDao;

//	@Transactional
//	@RequestMapping(value = "/store/create", method = RequestMethod.POST)
//	public ResponseEntity<String> createStore(
//			@RequestParam(value = "name", required = true) String name,
//			@RequestParam(value = "desc", required=false) String desc,
//			@RequestParam(value = "lat", required=true) Double latitude,
//			@RequestParam(value = "long", required=true) Double longitude,
//			@RequestParam(value = "streetname",required=false) String streetName,
//			@RequestParam(value = "city", required=false) String city,
//			@RequestParam(value = "state", required=false) String state,
//			@RequestParam(value = "country", required=false) String country,
//			@RequestParam(value = "zip", required=false) String zipCode,
//			@RequestParam(value="category", required=true) String templateCategory
//			) {
//		Store store = null;
//		Address address = null;
//		try {
//			
//			address = new Address( latitude, longitude );
//			address.setStreetName(streetName);
//			address.setCity(city);
//			address.setState(state);
//			address.setCountry(country);
//			address.setZipCode(zipCode);
//			
//			addressDao.save(address);
//			
//			store = new Store(name, desc);
//			store.setAddress(address);
//			
//			List<Template> list = new ArrayList<>();
//			if( -1 != templateCategory.indexOf(",") ){
//				for( String id : templateCategory.split(",") ){
//					list.add( templateDao.findOne( Long.parseLong(id)) );
//				}
//			}
//			else
//				list.add( templateDao.findOne( Long.parseLong(templateCategory)) );
//			
//			store.setTemplates(list);
//			storeDao.save(store);
//			
//		} catch (Exception ex) {
//			System.out.println(ex.toString());
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
	

}
