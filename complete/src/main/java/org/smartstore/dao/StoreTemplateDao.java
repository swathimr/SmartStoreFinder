package org.smartstore.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.smartstore.model.StoreTemplate;
import org.smartstore.model.Template;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface StoreTemplateDao extends CrudRepository<StoreTemplate, Long>{
	
//	@Query("from StoreTemplate st where st.templateId=:templateId")
//	public List<StoreTemplate> findStoreTemplates(@Param("templateId") long templateId);
	
	@Query("from StoreTemplate st where st.template=:template")
	public List<StoreTemplate> findStoreTemplates(@Param("template") Template template);
	
	
	
	
	
}
