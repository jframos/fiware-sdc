package com.telefonica.euro_iaas.sdc.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;




import com.telefonica.euro_iaas.sdc.manager.async.ArtifactAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.ProductInstanceAsyncManager;
import com.telefonica.euro_iaas.sdc.manager.async.TaskManager;
import com.telefonica.euro_iaas.sdc.model.Artifact;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;
import com.telefonica.euro_iaas.sdc.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ArtifactSearchCriteria;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

/**
 * Default ProductInstanceResource implementation.
 * 
 * @author Henar Mu�oz
 * 
 */
@Path("/vdc/{vdc}/productInstance/{productInstance}/ac")
@Component
@Scope("request")
public class ArtifactResourceImpl implements ArtifactResource {

	@InjectParam("artifactAsyncManager")
	private ArtifactAsyncManager artifactAsyncManager;
	@InjectParam("productInstanceAsyncManager")
	private ProductInstanceAsyncManager productInstanceAsyncManager;
	// @InjectParam("productManager")
	// private ProductManager productManager;
	@InjectParam("taskManager")
	private TaskManager taskManager;

	
	public Task install(String vdc, String productIntanceName,
			ArtifactDto artifactDto, String callback) {
		ProductInstance productInstance = getProductInstance(vdc,
				productIntanceName);

		Task task = createTask(MessageFormat.format(
				"Deploy artifact in  product {0} in  VM {1}{2}",
				productInstance.getProductRelease().getProduct().getName(),
				productInstance.getVm().getHostname(), productInstance.getVm()
						.getDomain()), vdc);
		Artifact artifact = new Artifact(artifactDto.getName(), productInstance
				.getVdc(), productInstance, artifactDto.getAttributes());
		artifactAsyncManager.deployArtifact(productInstance, artifact, task,
				callback);
		return task;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Task uninstall(String vdc, String productInstanceName,
			String artifactName, String callback) {

		ProductInstance productInstance = getProductInstance(vdc,
				productInstanceName);
		Task task = createTask(MessageFormat.format(
				"Undeploying artifact in  product {0} in  VM {1}{2}",
				productInstance.getProductRelease().getProduct().getName(),
				productInstance.getVm().getHostname(), productInstance.getVm()
						.getDomain()), vdc);
		artifactAsyncManager.undeployArtifact(productInstance, artifactName,
				task, callback);

		return task;
	}

	
	public Artifact load(String vdc, String productInstance, String name) {
		try {
			return artifactAsyncManager.load(vdc, productInstance, name);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	
	public List<ArtifactDto> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status, String vdc,
			String productInstance) {

		ArtifactSearchCriteria criteria = new ArtifactSearchCriteria();

		try {
			ProductInstance prodInstance = productInstanceAsyncManager.load(
					vdc, productInstance);
			criteria.setProductInstance(prodInstance);
			criteria.setVdc(vdc);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

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

		List<Artifact> artifact = artifactAsyncManager.findByCriteria(criteria);

		List<ArtifactDto> lArtifactDto = new ArrayList<ArtifactDto>();
		for (int i = 0; i < artifact.size(); i++) {
			ArtifactDto artifactDto = new ArtifactDto();

			if (artifact.get(i).getName() != null)
				artifactDto.setName(artifact.get(i).getName());

			if (artifact.get(i).getAttributes() != null)
				artifactDto.setAttributes(artifact.get(i).getAttributes());

			lArtifactDto.add(artifactDto);

		}
		return lArtifactDto;

	}

	private Task createTask(String description, String vdc) {
		Task task = new Task(TaskStates.RUNNING);
		task.setDescription(description);
		task.setVdc(vdc);
		return taskManager.createTask(task);
	}

	public ProductInstance getProductInstance(String vdc,
			String productInstanceName) {
		try {
			return productInstanceAsyncManager.load(vdc, productInstanceName);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}
	
	public void setTaskManager (TaskManager taskManager ){
		this.taskManager =taskManager ;
	}
	public void setProductInstanceAsyncManager (ProductInstanceAsyncManager productInstanceAsyncManager ){
		this.productInstanceAsyncManager =productInstanceAsyncManager ;
	}
	public void setArtifactAsyncManager (ArtifactAsyncManager artifactAsyncManager) {
		this.artifactAsyncManager=artifactAsyncManager;
	}


}