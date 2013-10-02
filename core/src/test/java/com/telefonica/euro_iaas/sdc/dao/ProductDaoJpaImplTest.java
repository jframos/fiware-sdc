/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc.dao;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.Product;

/**
 * Unit test for SODaoJpaImpl
 * 
 * @author Sergio Arroyo
 */
public class ProductDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductDao productDao;
    private OSDao soDao;

    public final static String PRODUCT_NAME = "productName";
    public final static String PRODUCT_VERSION = "productVersion";

    protected void createSO() throws Exception {
        SODaoJpaImplTest soDaoTest = new SODaoJpaImplTest();
        soDaoTest.setSoDao(soDao);
        soDaoTest.testCreate();
    }

    /**
     * Test the create and load method
     */
    public void testCreate() throws Exception {
        createSO();

        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.addAttribute(new Attribute("key", "value"));
        product.addMetadata(new Metadata("netkey", "metvalue"));

        assertEquals(0, productDao.findAll().size());
        product = productDao.create(product);
        assertEquals(product, productDao.load(PRODUCT_NAME));
        assertEquals(1, productDao.findAll().size());

    }

    /**
     * Test the update and load method
     */
    public void testUpdate() throws Exception {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.addAttribute(new Attribute("key", "value"));
        product.addMetadata(new Metadata("netkey", "metvalue"));

        assertEquals(0, productDao.findAll().size());
        product = productDao.create(product);

        Product loadedProduct = productDao.load(PRODUCT_NAME);
        assertEquals(loadedProduct.getName(), product.getName());

        loadedProduct.addAttribute(new Attribute("key", "value"));

        Product updatedProduct = productDao.update(loadedProduct);
        assertEquals(updatedProduct.getAttributes().size(), 2);

        loadedProduct.addMetadata(new Metadata("met_key2", "met_value2"));
        updatedProduct = productDao.update(loadedProduct);
        assertEquals(updatedProduct.getMetadatas().size(), 2);

    }

    /**
     * @param productDao
     *            the productDao to set
     */
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * @param soDao
     *            the soDao to set
     */
    public void setSoDao(OSDao soDao) {
        this.soDao = soDao;
    }

}
