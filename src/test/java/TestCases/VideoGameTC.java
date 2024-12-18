package TestCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

public class VideoGameTC {

	@Test(priority=1)
	public void test_getAllVideoGames() 
	{
	   given().log().all().contentType("application/json")
	   .when().get("http://localhost:8080/app/videogames")
	   .then().log().all()
	   .statusCode(200);
	}
	
	@Test(priority=2)
	public void text_addNewVideoGame()
	{
		Map<String, Object> data = new HashMap<>();
		data.put("id", 100);
		data.put("name", "GTA 5");
		data.put("releaseDate", "2024-12-18T11:34:31.082Z");
		data.put("reviewScore", "5");
		data.put("category", "Mission Based");
		data.put("rating", "Universal");
		
		Response res = given()
		.contentType("application/json").body(data)
		.when().post("http://localhost:8080/app/videogames")
		.then().log().body()
		 .statusCode(200).extract().response();
		
		String response = res.asString();
        
		Assert.assertTrue(response.contains("Record Added Successfully"));
	}
	@Test(priority=3)
	public void text_getNewVideoGame() 
	{
		 Response res =given()
		.when()
		.get("http://localhost:8080/app/videogames/100")
		.then()
		.statusCode(200).extract().response();
		
		 String response = res.asString();
		 XmlPath xmlPath = new XmlPath(response);
		 System.out.println(xmlPath.get("videogame.id")+" "+xmlPath.get("videogame.name"));
		 Assert.assertEquals(xmlPath.get("videogame.id"), "100");
		 Assert.assertEquals(xmlPath.get("videogame.name"), "GTA 5");
	}
	@Test(priority=4)
	public void test_UpdateVideoGame() 
	{
		Map<String, Object> data = new HashMap<>();
		data.put("id", 100);
		data.put("name", "GTA 5 stories");
		data.put("releaseDate", "2024-12-18T11:34:31.082Z");
		data.put("reviewScore", "4");
		data.put("category", "Mission Based");
		data.put("rating", "Universal");
		 Response res =given().contentType("application/json").body(data)
					.when()
					.put("http://localhost:8080/app/videogames/100")
					.then()
					.statusCode(200).log().all().extract().response();
		 String response = res.asString();
		 XmlPath xmlPath = new XmlPath(response);
		 System.out.println(xmlPath.get("videogame.name")+" "+xmlPath.get("videogame.reviewScore"));
		 Assert.assertEquals(xmlPath.get("videogame.name"), "GTA 5 stories");
		 Assert.assertEquals(xmlPath.get("videogame.reviewScore"), "4");
	}
	@Test(priority=5)
	public void test_DeleteVideoGame()
	{
		Response res =given()
				.when()
				.delete("http://localhost:8080/app/videogames/100")
				.then()
				.statusCode(200).extract().response();
		String response = res.asString();
		JsonPath js = new JsonPath(response);
		System.out.println(js.getString("status"));
		Assert.assertEquals(js.getString("status"),"Record Deleted Successfully");
		
	}

}
