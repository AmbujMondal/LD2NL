package org.aksw.api;

import org.aksw.owl2nl.OWLAxiomConverter;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
			}
		};
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "from OwlAPI") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/getOntology")
	public String GetOntology(@RequestParam(value = "path", defaultValue = "") String path) {
		String response;
		try {
			if (path == null || path.isEmpty())
				path = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl";
			path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
			OWLAxiomConverter converter = new OWLAxiomConverter();
			Map<String, String> data = converter.readOntology(path);

			JSONObject json = new JSONObject(data);

			System.out.println(String.format("reading from %s \n Ontologies :\n %s", path, json));
			return json.toString(2);
		} catch (Exception e) {
			e.printStackTrace();
			return String.format("Internal Error. Please try later! \n \n %s", e.getMessage());
		}
	}
}

