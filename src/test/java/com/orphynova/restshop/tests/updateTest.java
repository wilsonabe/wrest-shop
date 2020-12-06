package com.orphynova.restshop.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orphynova.restshop.models.Products;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class updateTest {
    private String baseuri = "http://localhost:8090";
    private String basepath = "/api/v1/products";
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void updateRecord(){
        String file = "testdata/product.json";
        Products product = null;
        URL url = getClass().getClassLoader().getResource(file);

        try {
            product = mapper.readValue(url,Products.class);
        } catch (IOException e) {
            System.out.println("file doesnt exist");
            e.printStackTrace();
        }
        //System.out.println("File exists");

        ValidatableResponse upResp =
        given()
                .baseUri(baseuri)
                .basePath(basepath)
                .contentType(ContentType.JSON)
                .body(product)
                .log().all()
         .when()
                .post("/")
         .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .assertThat().header("Location",containsString(basepath)); // checking if header exists
         String upLoc = upResp.extract().header("Location");
         String id = upLoc.substring(basepath.length()+1);
      //  System.out.println(id);

        ValidatableResponse updateRec =
                given()
                        .baseUri(baseuri)
                        .basePath(basepath)
                        .contentType(ContentType.JSON)
                        .body(product)
                        .log().all()
                .when()
                        .put("/"+id)
                .then()
                        .log().all()
                        .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

}
