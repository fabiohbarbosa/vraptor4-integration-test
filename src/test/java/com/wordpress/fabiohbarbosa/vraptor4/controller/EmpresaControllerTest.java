package com.wordpress.fabiohbarbosa.vraptor4.controller;
 
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
 
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.mediatype.JsonMediaType;
 
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wordpress.fabiohbarbosa.vraptor4.model.entity.Empresa;
 
public class EmpresaControllerTest {
    private RestClient restfulie;
    private final Gson gson = new GsonBuilder().create();
 
    public static final String URL = "http://localhost:8080/vraptor4";
 
    public EmpresaControllerTest() {
        restfulie = Restfulie.custom();  
        restfulie.getMediaTypes().register(new JsonMediaType());
    }
 
    @Test
    public void deveListarEmpresasParaEndPointCorreto() {
        String endPoint = "/empresa/list";
        Response response = restfulie(endPoint).get();
 
        String json = response.getContent();
        List<Empresa> empresas = toObjectList(json, Empresa.class);
        Assert.assertTrue(empresas.size() > 0);
    }
 
    @Test
    public void naoDeveListarEmpresasParaEndPointIncorreto() {
        String endPoint = "/empresa/listAll";
        Response response = restfulie(endPoint).get();
 
        Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    }
 
    private Request restfulie(String path) {
        return restfulie.at(URL+path)
                .as("application/json")
                .accept("application/json");
    }
 
    private <T> T toObject(String json, Class<T> clazz) {
        if(json == null) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }
 
    private <T> List<T> toObjectList(String json, Class<T> clazz) {
        if(json == null){
            return null;
        }
 
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
 
        List<T> list = new ArrayList<T>();
        for (final JsonElement jsonElement : array) {
            T entity = gson.fromJson(jsonElement, clazz);
            list.add(entity);
        }
        return list;
    }
 
}