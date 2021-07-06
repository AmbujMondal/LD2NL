package org.aksw.owl2nl;

import org.junit.Assert;
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

    private static OWLClass man;
    private static OWLClass woman;

    private static OWLNamedIndividual karaoke;
    private static OWLNamedIndividual jazz;
    private static OWLNamedIndividual rock;
    private static OWLNamedIndividual cricket;
    private static OWLNamedIndividual HTML;
    private static OWLNamedIndividual CSS;
    private static OWLNamedIndividual ANGULAR;
    private static OWLNamedIndividual upb;

    private static OWLObjectProperty worksFor;
    private static OWLObjectProperty know;
    private static OWLObjectProperty sings;
    private static OWLObjectProperty plays;

    OWLClassExpression ce, ce1;

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
        know = df.getOWLObjectProperty("know", pm);
        sings = df.getOWLObjectProperty("sing", pm);
        plays = df.getOWLObjectProperty("play", pm);

        man = df.getOWLClass("Man", pm);
        woman = df.getOWLClass("Woman", pm);

        karaoke = df.getOWLNamedIndividual("karaoke", pm);
        jazz = df.getOWLNamedIndividual("jazz", pm);
        cricket = df.getOWLNamedIndividual("cricket", pm);
        rock=df.getOWLNamedIndividual("rock", pm);
        upb=df.getOWLNamedIndividual("UPB", pm);
        HTML = df.getOWLNamedIndividual("HTML", pm);
        CSS = df.getOWLNamedIndividual("CSS", pm);
        ANGULAR = df.getOWLNamedIndividual("ANGULAR", pm);

        ToStringRenderer.getInstance().setRenderer(new DLSyntaxObjectRenderer());
    }

    @Test
    public void testDisjunctionSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊔ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz or karaoke", text);
    }

    @Test
    public void testConjunctionSSSVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                df.getOWLObjectHasValue(sings, jazz), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{karaoke})", ce.toString());
        Assert.assertEquals("a man that sings jazz and karaoke", text);
    }

    @Test
    public void testConjunctionSSSVDO2() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊓ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz and karaoke", text);
    }

    @Test
    public void testConjunctionMultipleSSSVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                df.getOWLObjectHasValue(sings, jazz),
                df.getOWLObjectHasValue(sings, rock), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{karaoke}) ⊓ (∃ sing.{rock})", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke and rock", text);
    }

    @Test
    public void testConjunctionMultipleSSSVDO2() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊓ (Man ⊓ (∃ sing.{karaoke}))" +
                " ⊓ (Man ⊓ (∃ sing.{rock}))", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke and rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz})) ⊔ (Man ⊓ (∃ sing.{karaoke}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock}))", ce.toString());
        Assert.assertEquals("a man that sings jazz, karaoke or rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSSVDO2() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                        df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man)));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{karaoke})) ⊔ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock})))", ce.toString());
        Assert.assertEquals("a man that sings karaoke or jazz or rock", text);
    }

    @Test
    public void testCombinationMultipleSSSVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz),
                        df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{jazz}) ⊓ (∃ sing.{rock}))" +
                " ⊔ (Man ⊓ (∃ sing.{karaoke}))", ce.toString());
        Assert.assertEquals("a man that sings jazz and rock or karaoke", text);
    }

    @Test
    public void testCombinationMultipleSSSVDO2() {
        ce1 = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, karaoke),
                man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock),
                man), ce1);
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ sing.{rock})) ⊓ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{karaoke})))", ce.toString());
        Assert.assertEquals("a man that sings rock and jazz or karaoke", text);
    }

    @Test
    public void testConjunctionMultipleSSDVDO() {
        ce = df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket),
                df.getOWLObjectHasValue(sings, jazz),
                df.getOWLObjectHasValue(sings, rock), man);
        text = converter.convert(ce);
        Assert.assertEquals("Man ⊓ (∃ play.{cricket}) ⊓ (∃ sing.{jazz})" +
                " ⊓ (∃ sing.{rock})", ce.toString());
        Assert.assertEquals("a man that plays cricket, sings jazz and sings rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSDVDO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ play.{cricket})) ⊔ (Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock}))", ce.toString());
        Assert.assertEquals("a man that plays cricket, sings jazz or sings rock", text);
    }

    @Test
    public void testDisjunctionMultipleSSDVDO2() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket), man),
                df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man),
                        df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, rock), man)));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ play.{cricket})) ⊔ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊔ (Man ⊓ (∃ sing.{rock})))", ce.toString());
        Assert.assertEquals("a man that plays cricket or sings jazz or sings rock", text);
    }

    @Test
    public void testCombinationMultipleSSDVDO() {
        ce1 = df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings,
                karaoke), man), df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(sings, jazz), man));
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(plays, cricket),
                man), ce1);
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ play.{cricket})) ⊔ ((Man ⊓ (∃ sing.{jazz}))" +
                " ⊓ (Man ⊓ (∃ sing.{karaoke})))", ce.toString());
        Assert.assertEquals("a man that plays cricket or sings jazz and sings karaoke", text);

    }

    @Test
    public void testDisjunctionDSSVSO() {
        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(worksFor, upb), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(worksFor, upb), woman));
        text = converter.convert(ce);
        Assert.assertEquals("(Man ⊓ (∃ worksFor.{UPB})) ⊔ (Woman ⊓ (∃ worksFor.{UPB}))", ce.toString());
        Assert.assertEquals("a man that or a woman that works for UPB", text);
    }

    @Test
    public void testCombinationMultipleDSSVDO() {
        ce = df.getOWLObjectUnionOf((df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(
                df.getOWLObjectHasValue(know, HTML), woman),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(know, CSS), woman))),
                (df.getOWLObjectIntersectionOf(df.getOWLObjectIntersectionOf(
                        df.getOWLObjectHasValue(know, HTML), man),
                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(know, CSS), man)) ));
        text = converter.convert(ce);
        Assert.assertEquals("((Man ⊓ (∃ know.{CSS})) ⊓ (Man ⊓ (∃ know.{HTML})))" +
                " ⊔ ((Woman ⊓ (∃ know.{CSS})) ⊓ (Woman ⊓ (∃ know.{HTML})))", ce.toString());
        Assert.assertEquals("something that a man that knows CSS and that a man that knows HTML or something" +
                " that a woman that knows CSS and that a woman that knows HTML", text);
    }

//    @Test
//    public void testcombination() {
//        ce = df.getOWLObjectUnionOf(df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(worksFor, upb),
//                df.getOWLObjectHasValue(plays, cricket) , man),
//                df.getOWLObjectIntersectionOf(df.getOWLObjectHasValue(worksFor, upb), woman));
//        text = converter.convert(ce);
//        Assert.assertEquals("(Man ⊓ (∃ worksFor.{UPB})) ⊔ (Woman ⊓ (∃ worksFor.{UPB}))", ce.toString());
//        Assert.assertEquals("a man that or a woman that works for UPB", text);
//    }
}
