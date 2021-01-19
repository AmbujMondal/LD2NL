package org.aksw.owl2nl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Optimizer {

    public String Optimise(String text) {
        try {
            StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
            if (text == null || text == "") return text;// = "something that plays jazz and something that plays karyoke";

            CoreDocument coreDocument = new CoreDocument(text);

            stanfordCoreNLP.annotate(coreDocument);
            List<CoreLabel> coreLabelList = coreDocument.tokens();
            List<String> posList = new ArrayList<>();
            List<Dict> list = new ArrayList<Dict>();

            for (CoreLabel coreLabel : coreLabelList) {
                //System.out.println(coreLabel.originalText());
                String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                //System.out.println(coreLabel.originalText() + "=" + pos);
                posList.add(pos);
                list.add(new Dict(pos, coreLabel.originalText()));
            }

            System.out.println("**************************************************");
            System.out.println("Before optimize : " + text);

            List<Integer> ccList = new ArrayList();
            List<Dict> verbList = new ArrayList();

            List<String> finalText = new ArrayList();

            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i)).Key.equals("VBZ")) {
                    verbList.add(new Dict(i,list.get(i).Value));
                }
                else if ((list.get(i)).Key.equals("CC")) {
                    ccList.add(i);
                }
            }
            boolean sameVerb = false;
            if(ccList.size()!=1)
                return text;
            if(verbList.size()!=2) return text;

            if (verbList.get(0).Value.equals(verbList.get(1).Value)) {

                for (int i = 0; i <= ccList.get(0); i++) {
                    finalText.add(list.get(i).Value);
                }
                for (int i = Integer.parseInt(verbList.get(1).Key) + 1; i < list.size(); i++) {
                    finalText.add(list.get(i).Value);
                }
            }
            text = String.join(" ", finalText);
            System.out.println("After Optimize : " + text);
            return text;
        }
        catch (Exception ex){
            return text;
        }
    }
}
class Pipeline {
    private static Properties properties;
    private static String propertiesName="tokenize, ssplit, pos";
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline(){

    }
    static {
        properties=new Properties();
        properties.setProperty("annotators",propertiesName);
    }
    public static StanfordCoreNLP getPipeline(){
        if(stanfordCoreNLP==null){
            stanfordCoreNLP=new StanfordCoreNLP(properties);

        }
        return stanfordCoreNLP;

    }
}

class Dict {
    public String Key;

    public String Value;

    public Dict(String key, String value) {
        Key = key;
        Value = value;
    }
    public Dict(int index, String value) {
        Key = String.valueOf(index);
        Value = value;
    }
}