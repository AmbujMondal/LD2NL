package org.aksw.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aksw.owl2nl.OWLAxiomConverter;
import org.aksw.owl2nl.OWLClassExpressionConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootApplication
@RestController
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/hello").allowedOrigins("*");
				registry.addMapping("/getOntology").allowedOrigins("*");
				registry.addMapping("/convert").allowedOrigins("*");
			}
		};
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "from OwlAPI") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/getOntology")
	public String GetOntology(@RequestParam(value = "path", defaultValue = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl") String path) throws Exception {
		String response;
		try {
			if (path == null || path.isEmpty())
				path = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl";
			path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
			OWLAxiomConverter converter = new OWLAxiomConverter();
			Map<String, String> data = converter.readOntology(path);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//JSONObject json = new JSONObject(data);
			Type gsonType = new TypeToken<Map>(){}.getType();
			response = gson.toJson(data, gsonType);
//			JsonElement je = JsonParser.parseString(gson.toJson(data, gsonType));
//			response = gson.toJson(je);
//
//			for(String key:je.getAsJsonObject().keySet()){
//				response = je[key]
//			}

			//response =
			System.out.println(String.format("reading from %s \n Ontologies :\n %s", path, response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format("Error! Please try again! \n \n %s", e.getMessage()));
			// return String.format("Internal Error. Please try later! \n \n %s", e.getMessage());
		}
	}

	@PostMapping("/convert")
	public Translate ConvertCEtoNL(@RequestBody Translate model) {
		Translate resp = new Translate(model.GetUrl(), model.GetCE());
		OWLClassExpressionConverter converter = new OWLClassExpressionConverter();


		return resp;
	}
}

