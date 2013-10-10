/**
 *   (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights
 *   Reserved.
 * 
 *   The copyright to the software program(s) is property of Telefonica I+D.
 *   The program(s) may be used and or copied only with the express written
 *   consent of Telefonica I+D or in accordance with the terms and conditions
 *   stipulated in the agreement/contract under which the program(s) have
 *   been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_ID;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_CLIENT_PASS;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_NODES_PATH;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CHEF_SERVER_URL;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ChefNodeDao;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.SdcRuntimeException;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

/**
 * Default implementation of ChefNodeManager.
 * 
 * @author Sergio Arroyo
 */
public class ChefNodeDaoRestImpl implements ChefNodeDao {

    SystemPropertiesProvider propertiesProvider;
    MixlibAuthenticationDigester digester;
    Client client;
    
    private String NODE_NOT_FOUND_PATTERN ="404";
    private static final int MAX_TIME = 90000;
    
    public ChefNode loadNodeFromHostname(String hostname) throws EntityNotFoundException, 
        CanNotCallChefException {
        try {
            String path = "/nodes";

            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = client.resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            String stringNodes;
            stringNodes = IOUtils.toString(wr.get(InputStream.class));
            
            if (stringNodes == null) {
                throw new EntityNotFoundException(ChefNode.class, null, 
                    "The ChefServer is empty of ChefNodes");
            }
            
            ChefNode node = new ChefNode();
            String nodeName = node.getChefNodeName(stringNodes, hostname);
            return loadNode(nodeName);
         } catch (UniformInterfaceException e) {
             throw new CanNotCallChefException(e);
         } catch (IOException e) {
             throw new SdcRuntimeException(e);
         }
     }
    /**
     * {@inheritDoc}
     */
    @Override
    public ChefNode loadNode(String chefNodename) throws CanNotCallChefException {
        try {
           String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), chefNodename);

            Map<String, String> header = getHeaders("GET", path, "");
            WebResource webResource = client.resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
            Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }
            String stringNode;
            stringNode = IOUtils.toString(wr.get(InputStream.class));
            JSONObject jsonNode = JSONObject.fromObject(stringNode);
        
            ChefNode node = new ChefNode();
            node.fromJson(jsonNode);
            return node;
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChefNode updateNode(ChefNode node) throws CanNotCallChefException {
        try {
            String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), node.getName());
            String payload = node.toJson();
            Map<String, String> header = getHeaders("PUT", path, payload);

            WebResource webResource = client.resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                    .entity(payload);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            String stringNode;
            stringNode = IOUtils.toString(wr.put(InputStream.class));
            JSONObject jsonNode = JSONObject.fromObject(stringNode);

            ChefNode updatedNode = new ChefNode();
            updatedNode.fromJson(jsonNode);
            return updatedNode;
        } catch (IOException e) {
            throw new SdcRuntimeException(e);
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }

    public void deleteNode(ChefNode node) throws CanNotCallChefException {
        try {
            String path = MessageFormat.format(propertiesProvider.getProperty(CHEF_SERVER_NODES_PATH), node.getName());
            // String payload = node.toJson();
            Map<String, String> header = getHeaders("DELETE", path, "");

            WebResource webResource = client.resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);

            Builder wr = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

            for (String key : header.keySet()) {
                wr = wr.header(key, header.get(key));
            }

            wr.delete(InputStream.class);
            // ClientResponse respone = wr.delete(ClientResponse.class);

            /*
             * String stringNode; stringNode = IOUtils.toString(wr.delete(InputStream.class)); JSONObject jsonNode =
             * JSONObject.fromObject(stringNode); ChefNode deletedNode = new ChefNode(); deletedNode.fromJson(jsonNode);
             * return deletedNode;
             */
            /*
             * } catch (IOException e) { throw new SdcRuntimeException(e);
             */
        } catch (UniformInterfaceException e) {
            throw new CanNotCallChefException(e);
        }
    }
    
    /**
     * Checks if ChefNode is already registered in ChefServer.
     */
    public void isNodeRegistered (String hostname) throws CanNotCallChefException {
        String path = "/nodes";

        Map<String, String> header = getHeaders("GET", path, "");
        WebResource webResource = client.resource(propertiesProvider.getProperty(CHEF_SERVER_URL) + path);
        Builder wr = webResource.accept(MediaType.APPLICATION_JSON);
        for (String key : header.keySet()) {
            wr = wr.header(key, header.get(key));
        }
       
        String response = hostname;
        int time = 10000;
        while (response.contains(hostname)) {
            
            try {
                Thread.sleep(time);
                System.out.println("Checking node : " + hostname + " time:" + time);
                if (time > MAX_TIME) {
                    String errorMesg = "Node  " + hostname + " is not registered in ChefServer";
                    throw new CanNotCallChefException(errorMesg);
                }
                response = IOUtils.toString(wr.get(InputStream.class));
                time += time;
            } catch (UniformInterfaceException e) {
                throw new CanNotCallChefException(e);
            } catch (IOException e) {
                throw new CanNotCallChefException(e);
            } catch (InterruptedException e) {
                String errorMsg = e.getMessage();
                throw new CanNotCallChefException(errorMsg, e);
            }
        }
    }
    
    private Map<String, String> getHeaders(String method, String path, String payload) {

        return digester.digest(method, path, payload, new Date(), propertiesProvider.getProperty(CHEF_CLIENT_ID),
                propertiesProvider.getProperty(CHEF_CLIENT_PASS));
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param digester
     *            the digester to set
     */
    public void setDigester(MixlibAuthenticationDigester digester) {
        this.digester = digester;
    }

    /**
     * @param client
     *            the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

}
