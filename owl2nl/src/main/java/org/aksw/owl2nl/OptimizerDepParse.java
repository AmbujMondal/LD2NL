package org.aksw.owl2nl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFormatter;

import java.util.ArrayList;
import java.util.List;


public class OptimizerDepParse {

    public String optimize(String text) {
        try {
            if (text == null || text == "") return text;

            text="something that a man that sings rock and that a man that sings jazz or a man that sings karaoke  or a man that sings metal";
            //text="something that a man that sings rock";
            StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
            CoreDocument coreDocument = new CoreDocument(text);
            SemanticGraphFormatter sgf = new SemanticGraphFormatter(1, 1, false, false, false, false, false);

            stanfordCoreNLP.annotate(coreDocument);
            CoreSentence sentence = coreDocument.sentences().get(0);
            SemanticGraph dependencyParse = sentence.dependencyParse();


            // Storing nodes of the parse tree in a list
            List<IndexedWord> nodeList = dependencyParse.vertexListSorted();

            //initializing

            // Complete sentence stored
            String completeSentence = dependencyParse.toRecoveredSentenceString();
            System.out.println("Text before using Dependency Parsing : " + text);

            List<IndexedWord> ccList = new ArrayList();
            List<Integer> ccIndex = new ArrayList();

            List<IndexedWord> commaList = new ArrayList();
            List<Integer> commaIndex = new ArrayList();

            List<IndexedWord> combinedCcCommaList = new ArrayList();
            List<Integer> combinedCcCommaIndex = new ArrayList();

            List<IndexedWord> verbList = new ArrayList();
            List<Integer> verbIndex = new ArrayList();

            List<IndexedWord> objectList = new ArrayList();
            List<Integer> objectIndex = new ArrayList();

            //object list
            int objectcounter=0;
            for (int i = 0; i < nodeList.size(); i++) {
                String object="";
                if ((nodeList.get(i).tag()).equals("VBZ")) {
                    objectcounter = i+1;
                    for(int j=i+1; j<nodeList.size(); j++){
                        if ((nodeList.get(j).tag()).equals("CC") || (nodeList.get(j).tag()).equals(".")){
                            i++;
                            break;
                        }
                        else{
                            object=object+nodeList.get(j)+" ";
                            i++;
                        }
                    };
                }
                if(object=="" || object=="."){

                }
                else{
                    objectList.add(new IndexedWord(CoreLabel.wordFromString(object)));
                    objectIndex.add(objectcounter);
                }
            }

            /*loading list*/
            for (int i = 0; i < nodeList.size(); i++) {
                if ((nodeList.get(i).tag()).equals("VBZ")) {
                    verbList.add(nodeList.get(i));
                    verbIndex.add(i);
                } else if ((nodeList.get(i).tag()).equals("CC")) {
                    ccList.add(nodeList.get(i));
                    ccIndex.add(i);
                    combinedCcCommaList.add(nodeList.get(i));
                    combinedCcCommaIndex.add(i);

                } else if ((nodeList.get(i).tag()).equals(",")) {
                    commaList.add(nodeList.get(i));
                    commaIndex.add(i);
                    combinedCcCommaList.add(nodeList.get(i));
                    combinedCcCommaIndex.add(i);

                }
            }
            boolean subjectsChecker = false;
            boolean verbsChecker = false;
            boolean objectChecker=false;
            boolean aggregated = false;
            StringBuffer finalText = new StringBuffer();
            List<IndexedWord> finalTextList = new ArrayList<>();
            if (combinedCcCommaList.size() >= 1) {
                subjectsChecker = checkAllSubjectsSame(nodeList, verbIndex, combinedCcCommaIndex);
                verbsChecker = checkAllVerbsSame(verbList);
                objectChecker=checkAllObjectsSame(objectList);
            }
            if (combinedCcCommaList.size() >= 1 && verbList.size() == combinedCcCommaList.size() + 1) {

                if (subjectsChecker) {
                    finalTextList = aggregateBySubjects(nodeList, verbIndex, combinedCcCommaIndex);
                    aggregated = true;
                    if (verbsChecker) {
                        finalTextList = aggregateByVerbs(nodeList, verbIndex, combinedCcCommaIndex);
                    }
                    //exp
                    for(int i=verbIndex.get(0)-1; i>=0; i--){

                        if(i-1>=0 && finalTextList.get(i).tag().equals("NN")){

                            for(int j=i-2;j>=0; j--){
                                finalTextList.remove(j);
                            }
                        }
                    }
                }

                else{
                    //for different subject same verbs and same objects
                    if(verbsChecker && objectChecker){
                        finalTextList=aggregateByObjects(nodeList,verbIndex,combinedCcCommaIndex);
                        aggregated=true;
                    }
                }
            }
            //exp
            for (int i=0;i<finalTextList.size();i++){
                if(finalTextList.get(i).tag().equals("WDT")){
                    finalTextList.remove(i);
                }
            }

            if (aggregated) {
                //removing WDT pos tag WDT stands for whdeterminer

//                for (int i=0;i<finalTextList.size();i++){
//                    if(finalTextList.get(i).tag().equals("WDT")){
//                        finalTextList.remove(i);
//                    }
//                }

                for (int i = 0; i < finalTextList.size(); i++) {
                    finalText.append(finalTextList.get(i).value());
                    if (i == finalTextList.size() - 1) {
                        continue;
                    } else {
                        String next = finalTextList.get(i + 1).value();
                        if (!next.equals(","))
                            finalText.append(" ");
                    }
                }
            } else {
                finalText.append(text);
            }
            System.out.println("Text after using Dependency Parsing : " + finalText);
            return finalText.toString();
        } catch (Exception e) {
            return text;
        }
    }

    //Method to check whether all verbs in the sentence are same or not
    public static boolean checkAllVerbsSame(List<IndexedWord> verbs) {
        for (int j = 1; j < verbs.size(); j++) {
            if (!((verbs.get(0).value()).equals(verbs.get(j).value()))) {
                return false;
            }
        }
        return true;
    }

    //Method to check whether all subjects in the sentence are same or not
    public static boolean checkAllSubjectsSame(List<IndexedWord> nodeList, List<Integer> verbIndex, List<Integer> combinedCcCommaIndex) {

        int numberOfSubjects = verbIndex.size();
        List<String> subjects = new ArrayList();
        //storing subjects
        for (int j = 0; j < numberOfSubjects; j++) {
            if (j == 0) {
                int pointer = 0;
                String s = ""; //temporary string
                for (; pointer < verbIndex.get(j); pointer++) {
                    s += nodeList.get(pointer).value();
                }
                if (!(s.equals(""))) {
                    subjects.add(s);
                }
            } else {
                int pointer = combinedCcCommaIndex.get(j - 1) + 1;
                String s = ""; //temporary string
                for (; pointer < verbIndex.get(j); pointer++) {
                    s += nodeList.get(pointer).value();
                }
                if (!(s.equals(""))) {
                    subjects.add(s);
                }
            }
        }
        if (subjects.size() == 1) {
            return false;
        }
        for (int i = 0; i < subjects.size() - 1; i++) {

            if (subjects.get(i).length() >= subjects.get(i + 1).length()) {
                if (!(subjects.get(i).contains(subjects.get(i + 1)))) {
                    return false;
                }
            } else if (!(subjects.get(i + 1).contains(subjects.get(i)))) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkAllObjectsSame(List<IndexedWord> objects){
        for(int j=1;j<objects.size();j++){
            if(!((objects.get(0).value()).equals(objects.get(j).value()))){
                //not same objects
                return false;
            }
        }
        return true;
    }

    //If the subject are same then this method will perform the aggregation on subjects
    public static List<IndexedWord> aggregateBySubjects(List<IndexedWord> nodeList, List<Integer> verbIndex, List<Integer> combinedCcCommaIndex) {
        List<IndexedWord> aggregatedList = new ArrayList();
        int pointer = 0;
        for (int j = 0; j < verbIndex.size(); j++) {
            if (j == 0) {
                for (; pointer <= combinedCcCommaIndex.get(j); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            } else if (j == verbIndex.size() - 1) {
                pointer = verbIndex.get(j);
                for (; pointer < nodeList.size(); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            } else {
                pointer = verbIndex.get(j);
                for (; pointer <= combinedCcCommaIndex.get(j); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            }

        }
        return aggregatedList;
    }

    //If the verbs are same then this method will perform the aggregation on verbs
    public static List<IndexedWord> aggregateByVerbs(List<IndexedWord> nodeList, List<Integer> verbIndex, List<Integer> combinedCcCommaIndex) {

        List<IndexedWord> aggregatedList = new ArrayList();
        int pointer = 0;
        for (int j = 0; j < verbIndex.size(); j++) {
            if (j == 0) {
                for (; pointer <= combinedCcCommaIndex.get(j); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            } else if (j == verbIndex.size() - 1) {
                pointer = verbIndex.get(j) + 1;
                for (; pointer < nodeList.size(); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            } else {
                pointer = verbIndex.get(j) + 1;
                for (; pointer <= combinedCcCommaIndex.get(j); pointer++) {
                    aggregatedList.add(nodeList.get(pointer));
                }
            }

        }
        return aggregatedList;
    }

    //object aggregation: different subject+same verbs+same object
    public static List<IndexedWord> aggregateByObjects(List<IndexedWord> nodeList, List<Integer> verbIndex, List<Integer> combinedCcCommaIndex) {
        List<IndexedWord> aggregatedList=new ArrayList();
        // removing verbs and object from the while list
        for(int i=0;i<verbIndex.size();i++){
            if(i==0){
                for(int j=0;j<verbIndex.get(i);j++){
                    aggregatedList.add(nodeList.get(j));
                }
            }
            else if(i==verbIndex.size()-1){
                for (int j=combinedCcCommaIndex.get(i-1);j<nodeList.size();j++){
                    aggregatedList.add(nodeList.get(j));
                }
            }
            else{
                for(int j=combinedCcCommaIndex.get(i-1); j<verbIndex.get(i); j++){
                    aggregatedList.add(nodeList.get(j));
                }
            }
        }

        return aggregatedList;
    }
}

