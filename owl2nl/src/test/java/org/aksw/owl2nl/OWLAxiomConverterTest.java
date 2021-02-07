package org.aksw.owl2nl;

import org.aksw.owl2nl.exception.OWLAxiomConversionException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

public class OWLAxiomConverterTest {

    private static OWLAxiomConverter converter;
    private static OWLDataFactoryImpl df;

    private static OWLObjectProperty knows;
    private static OWLObjectProperty isFatherOf;
    private static OWLObjectProperty isInLawOf;
    private static OWLObjectProperty isUncleInLawOf;
    private static OWLObjectProperty hasSex;
    private static OWLObjectProperty hasMother;

    private static OWLDataProperty hasBirthYear;
    private static OWLDataRange dataRange;

    private static OWLClass man;
    private static OWLClass woman;
    private static OWLClass person;
    private static OWLClass male;
    private static OWLClass female;
    private static OWLClass sex;
    String text;
    private OWLAxiom axiom;

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        converter = new OWLAxiomConverter();

        df = new OWLDataFactoryImpl();
        PrefixManager pm = new DefaultPrefixManager("http://dbpedia.org/ontology/");

        knows = df.getOWLObjectProperty("knows", pm);
        isFatherOf = df.getOWLObjectProperty("isFatherOf", pm);
        isUncleInLawOf = df.getOWLObjectProperty("isUncleInLawOf", pm);
        isInLawOf = df.getOWLObjectProperty("isInLawOf", pm);
        hasSex = df.getOWLObjectProperty("hasSex", pm);
        hasMother = df.getOWLObjectProperty("hasMother", pm);

        hasBirthYear = df.getOWLDataProperty("hasBirthYear", pm);
//        dataRange = df.getOWLDatatype("integer", pm);

        man = df.getOWLClass("man", pm);
        woman = df.getOWLClass("woman", pm);
        person = df.getOWLClass("person", pm);
        male = df.getOWLClass("male", pm);
        female = df.getOWLClass("female", pm);
        sex = df.getOWLClass("sex", pm);

        ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
    }

    @Test
    public void test_sub_class() throws OWLAxiomConversionException {
        axiom = df.getOWLSubClassOfAxiom(male, sex);
        text = converter.convert(axiom);
        System.out.println(axiom + " = " + text);
    }

    @Test
    public void test_sub_object_property() throws OWLAxiomConversionException {
        axiom = df.getOWLSubObjectPropertyOfAxiom(isUncleInLawOf, isInLawOf);
        text = converter.convert(axiom);
        System.out.println(axiom + " = " + text);
    }
}
