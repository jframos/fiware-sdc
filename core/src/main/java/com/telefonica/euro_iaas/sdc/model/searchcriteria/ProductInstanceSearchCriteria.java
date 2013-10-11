/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Sergio Arroyo
 */
public class ProductInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The host where the product is installed (<i>this criteria return a list of entities<i>).
     */
    private VM vm;

    /**
     * The vdc where the product is installed.
     */
    private String vdc;
    /**
     * The status of the application (<i>this criteria return a list of entities<i>).
     */
    private Status status;

    /**
     * The product.
     */
    private ProductRelease productRelease;

    private String productName;

    /**
     * Default constructor
     */
    public ProductInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType, VM vm,
            Status status, ProductRelease productRelease, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(String orderBy, String orderType, VM vm, Status status,
            ProductRelease productRelease, String vdc) {
        super(orderBy, orderType);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param product
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize, VM vm, Status status,
            ProductRelease productRelease, String vdc) {
        super(page, pageSize);
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @param vm
     */
    public ProductInstanceSearchCriteria(VM vm, Status status, ProductRelease productRelease, String vdc) {
        this.vm = vm;
        this.status = status;
        this.productRelease = productRelease;
        this.vdc = vdc;
    }

    /**
     * @return the host
     */
    public VM getVM() {
        return vm;
    }

    /**
     * @param vm
     *            the host to set
     */
    public void setVM(VM vm) {
        this.vm = vm;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the vm
     */
    public VM getVm() {
        return vm;
    }

    /**
     * @return the product
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setVm(VM vm) {
        this.vm = vm;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductInstanceSearchCriteria [vm=" + vm + ", status=" + status + ", product=" + productRelease
                + ", productName=" + productName + "]";
    }

}
