package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;

/**
 * Provides the methods to work with Chef Nodes.
 * 
 * @author Sergio Arroyo
 * 
 */
public interface ChefNodeDao {

	/**
	 * Retrieve the node information form Chef server given a VM (containing
	 * hostname and domain).
	 * 
	 * @param vm
	 *            the VM
	 * @return the ChefNode.
	 * @throws CanNotCallChefException
	 *             if Chef Server returns an unexpected error code
	 */
	//ChefNode loadNode(VM vm) throws CanNotCallChefException;
	ChefNode loadNode(String chefNodeName) throws CanNotCallChefException;

	/**
	 * Update the ChefNode with the actual values.
	 * 
	 * @param node
	 *            the node
	 * @return the updated node.
	 * @throws CanNotCallChefException
	 *             if Chef Server returns an unexpected error code
	 */
	ChefNode updateNode(ChefNode node) throws CanNotCallChefException;
	
	/**
	 * Delete the ChefNode 
	 * @param node the node
	 * @throws CanNotCallChefException
	 *             if Chef Server returns an unexpected error code
	 */
	void deleteNode(ChefNode node) throws CanNotCallChefException;
}
