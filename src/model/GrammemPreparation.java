package model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.onpositive.semantic.wordnet.AbstractWordNet;
import com.onpositive.semantic.wordnet.GrammarRelation;
import com.onpositive.semantic.wordnet.Grammem;
import com.onpositive.semantic.wordnet.WordNetProvider;

public class GrammemPreparation extends DataPreparation {
	public static List<String> grammems;

	public GrammemPreparation() throws IOException {
		super();
		DataPreparation.vectorSize = 735;
		grammems = getAllGrammems();
		// TODO Auto-generated constructor stub
	}
	
	public static List<String> getAllGrammems() {
		List<String> grammems = new ArrayList<String>();
		HashSet<Grammem> gr = Grammem.all;
		for (Grammem g : gr) {
			grammems.add(g.id);
		}
		return grammems;
	}
	
	public void initInputs(Wordform targetWord, Wordform[] context, double[] mm) {
		//return;
		int i = 0;
		for (Wordform wordform : context) {
			if (wordform == null) {
				i++;
				continue;
			}
//			if (wordform.partOfSpeech.equals("PUNCT")) {
//				mm[105*i+105] = 1;
//				i++;
//				continue;
//			}
			List<String> grammem = getAllGrammems(wordform.wordform.toLowerCase());
			if (grammem!=null) {
				for (String gr : grammem) {
					int pos = grammems.indexOf(gr);
					mm[105*i+pos] = 1;
				}
			}
			i++;
		}
		List<String> grammem = getAllGrammems(targetWord.wordform.toLowerCase());
		if (grammem!=null) {
			for (String g : grammem) {
				int pos = grammems.indexOf(g);
				mm[105*i+pos] = 1;
			}
		}
	}
	
	public static List<String> getAllGrammems(String word) {
		HashSet<Grammem> grammems = new HashSet<>();
		AbstractWordNet instance = WordNetProvider.getInstance();
		GrammarRelation[] possibleGrammarForms = instance.getPossibleGrammarForms(word);
		if (possibleGrammarForms == null) {
			return null;
		}
		for (GrammarRelation gr : possibleGrammarForms) {
			grammems = gr.getGrammems();
			grammems.addAll(gr.getWord().allGrammems());
		}
		List<String> gramems = new ArrayList<>();
		for (Grammem grammem : grammems) {
			gramems.add(grammem.id);
		}
		return gramems;
	}

}
