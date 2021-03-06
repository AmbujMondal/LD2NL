package org.aksw.owl2nl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;


/**
 * @author KG2NL_WS20 Team
 */
public class OWLClassExpressionConverterTest_Extended {

	private static OWLClassExpressionConverter converter;

	private static OWLClass company;
	private static OWLClass person;
	private static OWLClass softwareCompany;
	private static OWLDataFactoryImpl df;
	private static OWLNamedIndividual paderborn;
	private static OWLNamedIndividual karaoke;
	private static OWLNamedIndividual Jazz;
	private static OWLNamedIndividual Cricket;
	private static OWLNamedIndividual football;
	private static OWLNamedIndividual hockey;
	private static OWLNamedIndividual tennis;
	private static OWLNamedIndividual golf;
	private static OWLNamedIndividual hiphop;
	private static OWLNamedIndividual rock;

	private static OWLObjectProperty worksFor;
	private static OWLObjectProperty ledBy;
	private static OWLDataProperty amountOfSalary;
	private static OWLObjectProperty sings;
	private static OWLObjectProperty plays;
	private static OWLObjectProperty workPlace;
	private static OWLObjectProperty birthPlace;
	private static OWLLiteral salary;
	private static OWLDataProperty nrOfInhabitants;
	private static OWLDataRange dataRange;

	OWLClassExpression ce;
	OWLClassExpression ce1;
	OWLClassExpression ce2;

	String text;

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		df = new OWLDataFactoryImpl();
		converter = new OWLClassExpressionConverter();

		PrefixManager pm = new DefaultPrefixManager("http://dbpedia.org/ontology/");

		worksFor = df.getOWLObjectProperty("worksFor", pm);
		ledBy = df.getOWLObjectProperty("isLedBy", pm);
		sings = df.getOWLObjectProperty("sing", pm);
		plays = df.getOWLObjectProperty("play", pm);
		company = df.getOWLClass("Company", pm);
		person = df.getOWLClass("Person", pm);
		softwareCompany = df.getOWLClass("SoftwareCompany", pm);
		salary = df.getOWLLiteral(40000);
		amountOfSalary = df.getOWLDataProperty("amountOfSalary", pm);
		birthPlace = df.getOWLObjectProperty("birthPlace", pm);
		worksFor = df.getOWLObjectProperty("worksFor", pm);
		ledBy = df.getOWLObjectProperty("isLedBy", pm);

		workPlace = df.getOWLObjectProperty("workPlace", pm);
		paderborn = df.getOWLNamedIndividual("Paderborn", pm);
		karaoke = df.getOWLNamedIndividual("karaoke", pm);
		Jazz = df.getOWLNamedIndividual("jazz", pm);
		football = df.getOWLNamedIndividual("football", pm);
		Cricket = df.getOWLNamedIndividual("cricket", pm);
		hockey = df.getOWLNamedIndividual("hockey", pm);
		tennis = df.getOWLNamedIndividual("tennis", pm);
		golf = df.getOWLNamedIndividual("golf", pm);
		hiphop = df.getOWLNamedIndividual("hiphop", pm);
		rock = df.getOWLNamedIndividual("rock", pm);

		nrOfInhabitants = df.getOWLDataProperty("nrOfInhabitants", pm);
		dataRange = df.getOWLDatatypeMinInclusiveRestriction(10000000);

		ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
	}

	@Test
	public void testhasValue() {
		// work place is a place and is named paderborn
		ce = df.getOWLObjectHasValue(workPlace, paderborn);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("∃ workPlace.{Paderborn}", ce.toString());
		Assert.assertEquals("something that works place Paderborn", text);
	}

	@Test
	public void testNested1() {
		/*someone who works for at least 5 companies that is ledby a company or a person*/
		ce = df.getOWLObjectMinCardinality(5, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 5 worksFor.(Company ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 5 that a company that is" +
				" led by a company or a person", text);
		//String expected = "someone who works for at least 5 companies that is ledby a company or a person";
		//Assert.assertEquals(expected, text);

		/*someone who works for at least one company which is led by a company or a person*/
		ce = df.getOWLObjectMinCardinality(1, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 1 worksFor.(Company ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 1 that a company that is" +
				" led by a company or a person", text);
	}

	@Test
	public void simpleNegation() {
		/*someone who does not work for a person or a company*/
		ce = df.getOWLObjectComplementOf(df.getOWLObjectSomeValuesFrom(worksFor,
				df.getOWLObjectUnionOf(person, company)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("¬(∃ worksFor.(Company ⊔ Person))", ce.toString());
		Assert.assertEquals("something that does not works not for a company or a person", text);
	}

	@Test
	public void testNested2() {

		ce = df.getOWLObjectMinCardinality(5, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 5 worksFor.(Company ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 5 that a company that is" +
				" led by a company or a person", text);

		ce = df.getOWLObjectMinCardinality(1, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person))));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 1 worksFor.(Company ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 1 that a company that is" +
				" led by a company or a person", text);
	}

	@Test
	public void complexNegationWithMaxCardinality() {
		/*someone who does not work for a person or a company and whose birthplace is paderborn that has not more than 50000 inhabitants */
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(birthPlace, paderborn),
				df.getOWLDataMaxCardinality(500000, nrOfInhabitants, dataRange));
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectComplementOf(
				df.getOWLObjectSomeValuesFrom(worksFor, df.getOWLObjectUnionOf(person, company)))), ce);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("((¬(∃ worksFor.(Company ⊔ Person)))) ⊓ ((∃ birthPlace.{Paderborn})" +
				" ⊓ (≤ 500000 nrOfInhabitants.()))", ce.toString());
		Assert.assertEquals("something that something that does not works not for a company" +
				" or a person and that something whose birth place is Paderborn and that has" +
				" at most 500000 nr of inhabitants that are greater than or equals to 10000000", text);
	}

	@Test
	public void testNested1WithNegation() {

		/*someone who works for at least 5 companies which are not software companies and are ledby a company or a person*/
		ce = df.getOWLObjectMinCardinality(5, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person)), df.getOWLObjectComplementOf(softwareCompany)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 5 worksFor.(Company ⊓ (¬SoftwareCompany)" +
				" ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 5 that a company that are" +
				" not a software company and that are led by a company or a person", text);
		/*someone who works for at least one company which is not a software company and led by a company or a person*/
		ce = df.getOWLObjectMinCardinality(1, worksFor,
				df.getOWLObjectIntersectionOf(company, df.getOWLObjectSomeValuesFrom(ledBy,
						df.getOWLObjectUnionOf(company, person)), df.getOWLObjectComplementOf(softwareCompany)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("≥ 1 worksFor.(Company ⊓ (¬SoftwareCompany)" +
				" ⊓ (∃ isLedBy.(Company ⊔ Person)))", ce.toString());
		Assert.assertEquals("something that works for at least 1 that a company that is" +
				" not a software company and that is led by a company or a person", text);
	}

	@Test
	public void testDataHasValueAndObjectHasValue() {
		// works for a company
		/*someone whose work place is paderborn and whose amount of salary is 40000*/
		ce = df.getOWLObjectIntersectionOf(df.getOWLDataHasValue(amountOfSalary, salary),
				df.getOWLObjectHasValue(workPlace, paderborn));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(∃ workPlace.{Paderborn}) ⊓ (∃ amountOfSalary.{40000})", ce.toString());
		Assert.assertEquals("something that works place Paderborn" +
				" and whose amount of salary is 40000", text);
	}

	@Test
	public void NestedtesthasValue() {
		// work place is a place and is named paderborn
		ce = df.getOWLObjectSomeValuesFrom(workPlace,
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(workPlace, paderborn)));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);

		String expected = "something that works place something that works place Paderborn";
		Assert.assertEquals(expected, text);
		Assert.assertEquals("∃ workPlace.((∃ workPlace.{Paderborn}))", ce.toString());
		Assert.assertEquals("something that works place something that works place Paderborn", text);
	}

	@Test
	public void testComplex() {
		// a person that sings karaoke
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), person);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("Person ⊓ (∃ sing.{karaoke})", ce.toString());
		Assert.assertEquals("a person that sings karaoke", text);
	}

	@Test
	public void testComplex8() {
		// a person that sings karaoke or a person that sings  jazz
		ce1 = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
				person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), person));
		text = converter.convert(ce1);
		System.out.println(ce1 + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ sing.{jazz})) ⊔ (Person ⊓ (∃ sing.{karaoke}))", ce1.toString());
		Assert.assertEquals("a person that sings jazz or karaoke", text);
	}

	@Test
	public void testComplex7() {
		// a person that sings karaoke or jazz
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
				df.getOWLObjectHasValue(sings, Jazz)), person);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("Person ⊓ ((∃ sing.{jazz}) ⊓ (∃ sing.{karaoke}))", ce.toString());
		Assert.assertEquals("that something that sings jazz and karaoke", text);
	}

	@Test
	public void testComplex6() {
		// a person that sings karaoke and a person that sings jazz
		ce2 = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,
				karaoke), person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), person));
		text = converter.convert(ce2);
		System.out.println(ce2 + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ sing.{jazz})) ⊓ (Person ⊓ (∃ sing.{karaoke}))",
				ce2.toString());
		Assert.assertEquals("a person that sings jazz and karaoke", text);
	}

	@Test
	public void testComplex5() {
		// a person that plays Cricket or a person that plays football or a person that plays hockey.
		ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, Cricket),
				person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, football), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, hockey), person));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ play.{cricket})) ⊔ (Person ⊓ (∃ play.{football}))" +
				" ⊔ (Person ⊓ (∃ play.{hockey}))", ce.toString());
		Assert.assertEquals("a person that plays cricket, football or hockey", text);
	}

	@Test
	public void testmultipleConnectors() {
		// sentences for multiple connectors
		ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, Cricket),
				person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, football), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, hockey), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, tennis), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, golf), person));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ play.{cricket})) ⊔ (Person ⊓ (∃ play.{football}))" +
				" ⊔ (Person ⊓ (∃ play.{golf})) ⊔ (Person ⊓ (∃ play.{hockey})) ⊔ (Person ⊓ (∃ play.{tennis}))",
				ce.toString());
		Assert.assertEquals("a person that plays cricket, football, golf, hockey or tennis", text);
	}

	@Test
	public void testComplex2() {
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
				person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), person),
				df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, hiphop), person));
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ sing.{hiphop})) ⊓ (Person ⊓ (∃ sing.{jazz}))" +
				" ⊓ (Person ⊓ (∃ sing.{karaoke})) ⊓ (Person ⊓ (∃ sing.{rock}))", ce.toString());
		Assert.assertEquals("a person that sings hiphop, jazz, karaoke and rock", text);
	}

	@Test
	public void testComplex3() {
		ce1 = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
				person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), person));
		ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock),
				person), ce1);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ sing.{rock})) ⊓ ((Person ⊓ (∃ sing.{jazz}))" +
				" ⊔ (Person ⊓ (∃ sing.{karaoke})))", ce.toString());
		Assert.assertEquals("a person that sings rock and jazz or karaoke", text);
	}

	@Test
	public void testComplex4() {
		ce2 = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,
				karaoke), person), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), person));
		ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, Cricket),
				person), ce2);
		text = converter.convert(ce);
		System.out.println(ce + " = " + text);
		Assert.assertEquals("(Person ⊓ (∃ play.{cricket})) ⊔ ((Person ⊓ (∃ sing.{jazz}))" +
				" ⊓ (Person ⊓ (∃ sing.{karaoke})))", ce.toString());
		Assert.assertEquals("a person that plays cricket or sings jazz and sings karaoke", text);

	}
}
