package org.smartstore.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.smartstore.dao.StoreDao;
import org.smartstore.dao.StoreTemplateDao;
import org.smartstore.dao.TemplateDao;
import org.smartstore.helper.GoogleTravelTimeCalculator;
import org.smartstore.helper.TravelTimeCalculator;
import org.smartstore.model.Product;
import org.smartstore.model.StoreTemplate;
import org.smartstore.model.Template;
import org.smartstore.vo.ProductVo;
import org.smartstore.vo.StoreVo;
import org.smartstore.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@RestController
public class TemplateController {

	@Autowired
	private TemplateDao templateDao;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private StoreTemplateDao storeTemplateDao;

	@RequestMapping(value = "/template/getDefault", method = RequestMethod.GET)
	public ResponseEntity<List<Template>> getDefaultTemplate() {
		List<Template> list = null;
		try {
			list = (List<Template>) templateDao.findAll();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return new ResponseEntity<List<Template>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Template>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/template/create", method = RequestMethod.POST)
	public ResponseEntity<String> createTemplate(@RequestParam(value = "category", required = true) String category,
			@RequestParam(value = "name", required = true) String name) {
		Template template = null;
		try {
			template = new Template(category, name);
			templateDao.save(template);
		} catch (Exception ex) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/template/getStores", method = RequestMethod.GET)
	public ResponseEntity<List<TemplateVo>> getAvailableStores(
			@RequestParam(value = "lat", required = true) String latitude,
			@RequestParam(value = "long", required = true) String longitude,
			@RequestParam(value = "items", required = true) String templateItems) {

		System.out.println("items " + templateItems);
		System.out.println("lat " + latitude);
		System.out.println("lat " + longitude);
		List<TemplateVo> listTemplateVo = new ArrayList<>();
		
		TravelTimeCalculator travelTimeCalculator = new GoogleTravelTimeCalculator();
		
		try {
			listTemplateVo = new ArrayList<>();
			if (-1 != templateItems.indexOf(",")) {

				for (String templateId : templateItems.split(",")) {

					System.out.println("---------------");
					System.out.println("Template Id " + templateId);

					Template t = templateDao.findOne(Long.parseLong(templateId));

					List<StoreTemplate> lst = storeTemplateDao.findStoreTemplates(t);
					System.out.println("findStoreTemplates Size " + lst.size());

					TemplateVo templateVo = new TemplateVo();
					List<StoreVo> listStoresForStoreVo = new ArrayList<>();
					for (StoreTemplate st : lst) {

						StoreVo sVo = new StoreVo();
						sVo.setStoreId(st.getStore().getStoreId());
						sVo.setStoreName(st.getStore().getStoreName());
						sVo.setStoreDesc(st.getStore().getStoreDesc());
						sVo.setAddress(st.getStore().getAddress());
						sVo.setListIotDevices(st.getStore().getListIotDevices());
						sVo.setOrder(st.getStore().getOrder());
						sVo.setListProducts(this.getProductVoList(t.getName(), st.getStore().getListProducts()));
						sVo.setTravelTime(travelTimeCalculator.getTravelTime(latitude, longitude, sVo.getAddress().getLatitude(),
								sVo.getAddress().getLongitude()));

						listStoresForStoreVo.add(sVo);
					}
					System.out.println("Stores Added for this template id Size " + listStoresForStoreVo.size());
					templateVo.setTemplateId(t.getTemplateId());
					templateVo.setName(t.getName());

					// Sort
					templateVo.setStores(this.sortStoresOnTimeAndPrice(listStoresForStoreVo));
					listTemplateVo.add(templateVo);

					System.out.println("---------------");
				}
			} else {

				Template t = templateDao.findOne(Long.parseLong(templateItems));
				List<StoreTemplate> lst = storeTemplateDao.findStoreTemplates(t);

				TemplateVo templateVo = new TemplateVo();
				List<StoreVo> listStoresForStoreVo = new ArrayList<>();
				for (StoreTemplate st : lst) {

					StoreVo sVo = new StoreVo();
					sVo.setStoreId(st.getStore().getStoreId());
					sVo.setStoreName(st.getStore().getStoreName());
					sVo.setStoreDesc(st.getStore().getStoreDesc());
					sVo.setAddress(st.getStore().getAddress());
					sVo.setListIotDevices(st.getStore().getListIotDevices());
					sVo.setOrder(st.getStore().getOrder());
					sVo.setListProducts(this.getProductVoList(t.getName(), st.getStore().getListProducts()));
					sVo.setTravelTime(travelTimeCalculator.getTravelTime(latitude, longitude, sVo.getAddress().getLatitude(),
							sVo.getAddress().getLongitude()));

					listStoresForStoreVo.add(sVo);
				}

				templateVo.setTemplateId(t.getTemplateId());
				templateVo.setName(t.getName());

				// Sort
				templateVo.setStores(this.sortStoresOnTimeAndPrice(listStoresForStoreVo));

				listTemplateVo.add(templateVo);

			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return new ResponseEntity<List<TemplateVo>>(HttpStatus.BAD_REQUEST);
		}
		System.out.println(" At the end Size " + listTemplateVo.size());
		return new ResponseEntity<List<TemplateVo>>(listTemplateVo, HttpStatus.OK);
	}

	
	private List<StoreVo> sortStoresOnTimeAndPrice(List<StoreVo> list) {

		Collections.sort(list, new Comparator<StoreVo>() {

			@Override
			public int compare(StoreVo s1, StoreVo s2) {

				if (s1.getTravelTime() > s2.getTravelTime())
					return 1;
				else if (s1.getTravelTime() < s2.getTravelTime())
					return -1;
				return 0;
			}
		});

		return list;
	}

	private List<ProductVo> getProductVoList(String name, List<Product> list) {

		List<ProductVo> lst = new ArrayList<>();
		if (null != list && !list.isEmpty()) {
			for (Product p : list) {

				ProductVo pVo = new ProductVo();
				pVo.setProductId(p.getProductId());
				pVo.setProductName(p.getProductName());
				pVo.setProductDesc(p.getProductDesc());
				pVo.setCost(p.getCost());
				pVo.setQuantity(p.getQuantity());

				if (name.equalsIgnoreCase(pVo.getProductName()) && p.getQuantity() > 0)
					lst.add(pVo);
			}
		}
		return lst;
	}

	private Double getDistance(Double lat1, Double long1, Double lat2, Double long2) {
		Double dlon = long2 - long1;
		Double dlat = lat2 - lat1;
		Double a = (Math.pow(Math.sin(dlat / 2), 2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2));
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		// Miles 3961 // kms 6373
		return 3961 * c;
	}

}
