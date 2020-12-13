package com.orphynova.restshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orphynova.restshop.models.Products;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class ProductService {
    private String baseuri = "http://localhost:8090";
    private String basepath = "/api/v1/products";
    private ObjectMapper mapper = new ObjectMapper();
   // String file = "testdata/product.json";
    Products product = null;

    public Products readFile (String file){
        URL url = getClass().getClassLoader().getResource(file);
        try {
            product = mapper.readValue(url, Products.class);
        } catch (IOException e) {
            System.out.println("File read error");
            e.printStackTrace();
        }
        return product;
    }

    public String saveRecord(String file) {

        readFile(file);
        String Id = null;
        ValidatableResponse resp = given()
                .baseUri(baseuri)
                .basePath(basepath)
                .contentType(ContentType.JSON)
                .body(product)
                .log().all()
                .when()
                .post("/")     // POST - creating new data

                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                // checking 2nd part of test , if the header location has the entry listed
                .assertThat().header("Location", containsString("/api/v1/products/"));
        String Locn = resp.extract().header("Location");
        Id = Locn.substring(basepath.length() + 1);
        return Id;
    }


    public void findRecord(String id, String file) {

        readFile(file);
     //checking if products exists in DB using GET operator
        ValidatableResponse getResp =
                given()
                        .baseUri(baseuri)
                        .basePath(basepath)
                        .log().all()
                .when()
                        .get("/"+id)         // GET - retreiving created data
                .then()
                        .log().all()
                        .assertThat().statusCode(HttpStatus.SC_OK);
        // Getting Body from the response
        Products respBody = getResp.extract().body().as(Products.class);
       // Set ID in json file before comparison
        product.setId(respBody.getId());
        Assert.assertEquals(respBody,product,"Entry doesn't exist in DB");
    }

    public void updateRecord(String id, String file) {
       // String upfile = "testdata/updateproduct.json";
        readFile(file);
        product.setId(id);
        ValidatableResponse getResp =
                given()
                        .baseUri(baseuri)
                        .basePath(basepath)
                        .contentType(ContentType.JSON)
                        .body(product)
                        .log().all()
                .when()
                        .put("/"+id)         // PUT - updating existing data
                .then()
                        .log().all()
                        .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
