package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.Date;
import java.util.List;


import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.TaskSearchCriteria;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

/**
 * Default TaskResource implementation.
 * 
 * @author Sergio Arroyo
 * 
 */
@Path("/vdc/{vdc}/task")
@Component
@Scope("request")
public class TaskResourceImpl implements TaskResource {

	@InjectParam("taskManager")
	private TaskManager taskManager;

	@Override
	public Task load(Long id) throws EntityNotFoundException {
		return taskManager.load(id);
	}

	@Override
	public List<Task> findAll(Integer page, Integer pageSize, String orderBy,
			String orderType, List<TaskStates> states, String resource,
			String owner, Date fromDate, Date toDate, String vdc) {
		TaskSearchCriteria criteria = new TaskSearchCriteria();
		criteria.setVdc(vdc);
		if (page != null && pageSize != null) {
			criteria.setPage(page);
			criteria.setPageSize(pageSize);
		}
		if (!StringUtils.isEmpty(orderBy)) {
			criteria.setOrderBy(orderBy);
		}
		if (!StringUtils.isEmpty(orderType)) {
			criteria.setOrderBy(orderType);
		}
		criteria.setStates(states);
		criteria.setResource(resource);
		criteria.setOwner(owner);
		criteria.setFromDate(fromDate);
		criteria.setToDate(toDate);
		return taskManager.findByCriteria(criteria);
	}

}
