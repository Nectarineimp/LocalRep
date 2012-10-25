import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.semgraph.*;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;

public class StanfordMain {
	public Properties props;
	StanfordCoreNLP pipeline;
	String text;
	 Annotation document;
	 List<CoreMap> sentences;
	
	public StanfordMain(String[] args) {
		props = new Properties();
		if (args.equals(null)) {
			System.err.print("Can't run without arguments. Usage: StanfordMain Text [annotator list]\n");
			System.exit(-1);
		} else if (args[1].equals(null)) {
			props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		} else {
			props.put("annotators", args[1]);
		}
		
		String text = args[0];
		pipeline = new StanfordCoreNLP(props);
	    document = new Annotation(text);
	    pipeline.annotate(document);
	    sentences = document.get(SentencesAnnotation.class);
	}
	
	public void PrintAnnotation() {
		for(CoreMap sentence: sentences) {
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class) ) {
	    		String word = token.get(TextAnnotation.class);
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		String ne = token.get(NamedEntityTagAnnotation.class);
	    		System.out.println(word + " " + pos + " " + ne);
	    	}
		}
	}
	
	public void PrintTree() {
		for(CoreMap sentence: sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			tree.pennPrint();
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			dependencies.prettyPrint();
		}
	}
	
	public void PrintDependencies() {
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
		System.out.println(graph.toString());
	}
	
}
