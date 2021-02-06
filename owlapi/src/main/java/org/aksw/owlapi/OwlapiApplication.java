package org.aksw.owlapi;

import org.aksw.owl2nl.OWLAxiomConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@RestController
public class OwlapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwlapiApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello from owlApi %s!", name);
	}

	@GetMapping("/getOntology")
	public String GetOntology(@RequestParam(value = "path", defaultValue = "") String path) {
		String response = "";
		try {
			if (!path.isEmpty())
				path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
			else path = "http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl";
//			path = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
			OWLAxiomConverter converter = new OWLAxiomConverter();

			response = converter.GetOntology(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(response);
		return String.format("reading from %s /n Ontologies :/n %s", path, response);
	}
}
