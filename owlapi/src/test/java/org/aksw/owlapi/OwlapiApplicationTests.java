package org.aksw.owlapi;

import org.aksw.owl2nl.OWLAxiomConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OwlapiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void Check() throws Exception {
		OWLAxiomConverter converter = new OWLAxiomConverter();
		String response = converter.GetOntology("");
		System.out.println(response);
	}

}
