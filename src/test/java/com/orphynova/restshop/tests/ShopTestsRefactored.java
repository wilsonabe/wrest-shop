package com.orphynova.restshop.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orphynova.restshop.service.ProductService;
import org.testng.annotations.Test;



public class ShopTestsRefactored {
    private String baseuri = "http://localhost:8090";
    private String basepath = "/api/v1/products";
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void saveANewProduct(){
        ProductService productservice = new ProductService();
        String id = productservice.saveRecord();
        productservice.findRecord(id);
    }

    @Test
    public void updateExistingProduct(){
        ProductService productservice = new ProductService();
        String id = productservice.saveRecord();
        productservice.updateRecord(id);
    }
}
