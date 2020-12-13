package com.orphynova.restshop.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orphynova.restshop.service.ProductService;
import org.testng.annotations.Test;



public class ShopTestsRefactored {
    private String baseuri = "http://localhost:8090";
    private String basepath = "/api/v1/products";
    private ObjectMapper mapper = new ObjectMapper();
    String file = "testdata/product.json";
    String upfile = "testdata/updateproduct.json";

    @Test
    public void saveANewProduct(){
        ProductService productservice = new ProductService();
        String id = productservice.saveRecord(file);
        productservice.findRecord(id,file);
    }

    @Test
    public void updateExistingProduct(){
      //  String upfile = "testdata/updateproduct.json";
        ProductService productservice = new ProductService();
        String id = productservice.saveRecord(file);
        productservice.updateRecord(id,upfile);
        productservice.findRecord(id,upfile);
    }
}
