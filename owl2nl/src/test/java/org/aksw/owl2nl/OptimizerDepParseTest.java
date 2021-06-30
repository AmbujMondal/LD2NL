package org.aksw.owl2nl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class OptimizerDepParseTest {
    private static OWLClassExpressionConverter converter;
    private static OWLDataFactoryImpl df;

    private static OWLClass company;
    private static OWLClass man;
    private static OWLClass boy;
    private static OWLClass girl;
    private static OWLClass softwareCompany;
    private static OWLClass woman;
    private static OWLClass student;
    private static OWLClass teacher;
    private static OWLClass employee;

    private static OWLNamedIndividual HTML;
    private static OWLNamedIndividual CSS;
    private static OWLNamedIndividual ANGULAR;
    private static OWLNamedIndividual German;
    private static OWLNamedIndividual French;
    private static OWLNamedIndividual English;

    private static OWLObjectProperty speaks;
    private static OWLObjectProperty knows;
    private static OWLObjectProperty teaches;
    private static OWLObjectProperty wears;




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
    private static OWLNamedIndividual top;
    private static OWLNamedIndividual skirt;
    private static OWLNamedIndividual shirt;
    private static OWLNamedIndividual jeans;



    private static OWLObjectProperty worksFor;
    private static OWLObjectProperty ledBy;
    private static OWLDataProperty amountOfSalary;
    private static OWLObjectProperty sings;
    private static OWLObjectProperty plays;
    private static OWLObjectProperty workPlace;
    private static OWLObjectProperty birthPlace;
    private static OWLLiteral salary;
    private static OWLDataProperty nrOfInhabitants;
    private static OWLDataProperty nrOfSons;
    private static OWLDataProperty nrOfDaughters;
    private static OWLDataRange dataRange;
    private static OWLLiteral sons;
    private static OWLLiteral daughters;
    OWLClassExpression ce;

    String text;
    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        converter = new OWLClassExpressionConverter();

        df = new OWLDataFactoryImpl();
        PrefixManager pm = new DefaultPrefixManager("http://dbpedia.org/ontology/");

        worksFor = df.getOWLObjectProperty("worksFor", pm);
        ledBy = df.getOWLObjectProperty("isLedBy", pm);
        sings = df.getOWLObjectProperty("sing", pm);
        plays = df.getOWLObjectProperty("play", pm);
        company = df.getOWLClass("Company", pm);
        man = df.getOWLClass("Man", pm);
        softwareCompany = df.getOWLClass("SoftwareCompany", pm);
        salary = df.getOWLLiteral(40000);
        amountOfSalary = df.getOWLDataProperty("amountOfSalary", pm);
        birthPlace = df.getOWLObjectProperty("birthPlace", pm);
        worksFor = df.getOWLObjectProperty("worksFor", pm);
        ledBy = df.getOWLObjectProperty("isLedBy", pm);
        knows = df.getOWLObjectProperty("knows", pm);
        speaks=df.getOWLObjectProperty("speaks", pm);

        employee = df.getOWLClass("employee", pm);
        company = df.getOWLClass("Company", pm);
        student = df.getOWLClass("student", pm);
        woman = df.getOWLClass("woman", pm);
        man = df.getOWLClass("man", pm);
        boy = df.getOWLClass("boy", pm);
        girl = df.getOWLClass("girl", pm);
        teacher = df.getOWLClass("teacher", pm);

        workPlace = df.getOWLObjectProperty("workPlace", pm);
        teaches = df.getOWLObjectProperty("teaches", pm);
        wears= df.getOWLObjectProperty("wears", pm);
        paderborn = df.getOWLNamedIndividual("Paderborn", pm);
        karaoke = df.getOWLNamedIndividual("karaoke", pm);
        Jazz = df.getOWLNamedIndividual("jazz", pm);
        football = df.getOWLNamedIndividual("football", pm);
        Cricket = df.getOWLNamedIndividual("cricket", pm);
        hockey = df.getOWLNamedIndividual("hockey", pm);
        tennis= df.getOWLNamedIndividual("tennis", pm);
        golf= df.getOWLNamedIndividual("golf", pm);
        hiphop= df.getOWLNamedIndividual("hiphop", pm);
        rock=df.getOWLNamedIndividual("rock", pm);
        HTML = df.getOWLNamedIndividual("HTML", pm);
        CSS = df.getOWLNamedIndividual("CSS", pm);
        ANGULAR = df.getOWLNamedIndividual("ANGULAR", pm);
        German = df.getOWLNamedIndividual("German", pm);
        French = df.getOWLNamedIndividual("French", pm);
        English = df.getOWLNamedIndividual("English", pm);
        top = df.getOWLNamedIndividual("top", pm);
        skirt= df.getOWLNamedIndividual("skirt", pm);
        shirt = df.getOWLNamedIndividual("shirt", pm);
        jeans = df.getOWLNamedIndividual("jeans", pm);



        nrOfInhabitants = df.getOWLDataProperty("nrOfInhabitants", pm);
        nrOfSons= df.getOWLDataProperty("nrOfSons",pm);
        nrOfDaughters= df.getOWLDataProperty("nrOfDaughters",pm);
        dataRange = df.getOWLDatatypeMinInclusiveRestriction(10000000);
        sons = df.getOWLLiteral(3);
        daughters = df.getOWLLiteral(2);
        ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
    }

    @Test
    public void testSubjectAndVerbAggregation() {

        // a person that sings karaoke or a person that sings  jazz

        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, Jazz), man));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, ANGULAR), student),(df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), student), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), student)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);


        ce = df.getOWLObjectUnionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), woman), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), woman)) ), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, ANGULAR), man));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, ANGULAR), woman), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, ANGULAR), man));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);


        ce = df.getOWLObjectUnionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), woman), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), woman)) ), (df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), man)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(speaks, German), employee),(df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(speaks, French), employee), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(speaks, English), employee)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLDataHasValue(nrOfSons, sons), man),(df.getOWLObjectIntersectionOf(df.getOWLDataHasValue(nrOfDaughters, daughters), man) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectIntersectionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), woman), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), woman)) ), (df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, HTML), man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(knows, CSS), man)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectIntersectionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, top), girl), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, skirt), girl)) ), (df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, shirt), boy), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, jeans), boy)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectUnionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, top), girl), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, skirt), girl)) ), (df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, shirt), boy), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(wears, jeans), boy)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(teaches, German), teacher),(df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(teaches, French), teacher), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(teaches, English), teacher)) ));
        text = converter.convert(ce);
        System.out.println(ce + "=" + text);

    }
}
