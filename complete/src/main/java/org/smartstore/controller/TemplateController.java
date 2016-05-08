package org.smartstore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.smartstore.dao.StoreDao;
import org.smartstore.dao.StoreTemplateDao;
import org.smartstore.dao.TemplateDao;
import org.smartstore.model.Product;
import org.smartstore.model.Store;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



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
			@RequestParam(value = "lat", required = true) Double latitude,
			@RequestParam(value = "long", required = true) Double longitude,
			@RequestParam(value = "items", required = true) String templateItems
			) {
		
		System.out.println( "items " + templateItems );
		List<TemplateVo> listTemplateVo = new ArrayList<>();
		try {
			listTemplateVo = new ArrayList<>();
			if( -1 != templateItems.indexOf(",") ){
				
				for( String templateId : templateItems.split(",") ){
					
					System.out.println("---------------");
					System.out.println("Template Id " + templateId );
					
					Template t = templateDao.findOne( Long.parseLong(templateId));
					
					List<StoreTemplate> lst = storeTemplateDao.findStoreTemplates(t);
					System.out.println("findStoreTemplates Size " + lst.size() );
					
					TemplateVo templateVo = new TemplateVo();
					List<StoreVo> listStoresForStoreVo = new ArrayList<>();
					for( StoreTemplate st : lst ){
						
						StoreVo sVo = new StoreVo();
						sVo.setStoreId(st.getStore().getStoreId());
						sVo.setStoreName(st.getStore().getStoreName());
						sVo.setStoreDesc(st.getStore().getStoreDesc());
						sVo.setAddress(st.getStore().getAddress());
						sVo.setListIotDevices(st.getStore().getListIotDevices());
						sVo.setOrder(st.getStore().getOrder());
						sVo.setListProducts( this.getProductVoList( t.getName(), st.getStore().getListProducts() ));
						
						listStoresForStoreVo.add(sVo);
					}
					System.out.println("Stores Added for this template id Size " + listStoresForStoreVo.size() );
					templateVo.setTemplateId(t.getTemplateId());
					templateVo.setName(t.getName());
					templateVo.setStores(listStoresForStoreVo);
					listTemplateVo.add(templateVo);
					
					System.out.println("---------------");
				}
			}else{
				
				Template t = templateDao.findOne( Long.parseLong(templateItems));
				List<StoreTemplate> lst = storeTemplateDao.findStoreTemplates(t);
				
				TemplateVo templateVo = new TemplateVo();
				List<StoreVo> listStoresForStoreVo = new ArrayList<>();
				for( StoreTemplate st : lst ){
					
					StoreVo sVo = new StoreVo();
					sVo.setStoreId(st.getStore().getStoreId());
					sVo.setStoreName(st.getStore().getStoreName());
					sVo.setStoreDesc(st.getStore().getStoreDesc());
					sVo.setAddress(st.getStore().getAddress());
					sVo.setListIotDevices(st.getStore().getListIotDevices());
					sVo.setOrder(st.getStore().getOrder());
					sVo.setListProducts( this.getProductVoList( t.getName(), st.getStore().getListProducts() ));
					
					listStoresForStoreVo.add(sVo);
				}
				
				templateVo.setTemplateId(t.getTemplateId());
				templateVo.setName(t.getName());
				templateVo.setStores(listStoresForStoreVo);
				listTemplateVo.add(templateVo);
				
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return new ResponseEntity<List<TemplateVo>>(HttpStatus.BAD_REQUEST);
		}
		System.out.println( " At the end Size " + listTemplateVo.size() );
		return new ResponseEntity<List<TemplateVo>>(listTemplateVo, HttpStatus.OK);
	}
	
	/*private List<Store> getListOfStores( Template template, Double lat, Double longi){
		
		
		
		System.out.println( " Template id " + template.getTemplateId() + " Template Store size " + template.getStores().size());
		
		List<Store> stores = new ArrayList<>();
		
		//System.out.println(" Lat " + lat + " longi " + longi );
		
		ListIterator<Store> itrS = template.getStores().listIterator();
		while( itrS.hasNext() ){
			
			Store s = itrS.next();
			System.out.println( " Store got in itrs " + s.getStoreName() );
			System.out.println( " Store Products Size " + s.getListProducts().size() );
			if( null != s.getListProducts() ){
				
				ListIterator<Product> itr = s.getListProducts().listIterator();
				
				while( itr.hasNext() ){
					Product p = itr.next();
					if( !p.getProductName().equalsIgnoreCase(template.getName()) ){
						itr.remove();
					}
				}
			}
			
			if( s.getListProducts() == null || s.getListProducts().isEmpty() ){
				System.out.println( " Removing whole storee " + s.getStoreName() );
				itrS.remove();
			}else{
				System.out.println( " Adding storee " + s.getStoreName() );
				stores.add(s);
			}
		}
		
		System.out.println( "--------------------Stores List Size getListOfStores " + stores.size() );
		return stores;
	}
*/	
	private Double getDistance( Double lat1, Double long1, Double lat2, Double long2 ){
		Double dlon = long2 - long1; 
		Double dlat = lat2 - lat1;
		Double a = ( Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2) ); 
		Double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) ); 
		// Miles 3961 // kms 6373 
		return 3961 * c;
	}
	
	private List<ProductVo> getProductVoList( String name, List<Product> list ){
		
		List<ProductVo> lst = new ArrayList<>();
		if( null != list && !list.isEmpty() ){
			for( Product p : list ){
				
				ProductVo pVo = new ProductVo();
				pVo.setProductId(p.getProductId());
				pVo.setProductName(p.getProductName());
				pVo.setProductDesc(p.getProductDesc());
				pVo.setCost(p.getCost());
				pVo.setQuantity(p.getQuantity());
				
				if( name.equalsIgnoreCase( pVo.getProductName() ))
					lst.add(pVo);
			}
		}
		return lst;
	}
}
