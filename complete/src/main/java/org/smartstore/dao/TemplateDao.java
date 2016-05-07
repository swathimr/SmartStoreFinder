package org.smartstore.dao;

import javax.transaction.Transactional;

import org.smartstore.model.Template;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TemplateDao extends CrudRepository<Template, Long>{
	
}
