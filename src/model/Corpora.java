package model;
	import java.io.BufferedReader;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	
public class Corpora {

		public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
			String gikrya = "D:/MorphoRuEval/GICRYA_texts/gikrya_fixed.txt";
			String openCorpora = "D:/MorphoRuEval/OpenCorpora_Texts/unamb_sent_14_6.conllu";
			String rnc = "D:/MorphoRuEval/RNC_texts/RNCgoldInUD_Morpho.conll";
			String syntagrus = "D:/MorphoRuEval/SYNTAGRUS_texts/syntagrus_full_fixed.ud";
			List<Sentence> corpus = loadCorpus(rnc, false);
			Wordform[] context = corpus.get(1).contexts(5);
			System.out.println(corpus.size());
		}
		
		public static List<Sentence> loadCorpus(String path, boolean openCorpora) throws IOException {
			List<Sentence> corpus = new ArrayList<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = null;
			Sentence sentence = new Sentence();
			List<Wordform> words = new ArrayList<>();
			sentence.words = words;
			while ((line = br.readLine())!=null) {
				String[] l = line.split("\t");
				if (l.length<2) {
					corpus.add(sentence);
					sentence = new Sentence();
					List<Wordform> w = new ArrayList<>();
					sentence.words = w;
					continue;
				}
				Wordform wordform = new Wordform();
				if (!openCorpora) {
					if (l.length==4) {
						wordform.grammems = l[3];
					}
					else {
				wordform.grammems = l[4];
					}
				}
				else {
					wordform.grammems = l[5];
				}
				wordform.lemma = l[2];
				wordform.wordform = l[1];
				wordform.partOfSpeech = l[3];
				if (l.length>5) {
					wordform.additionalFeatures = l[5];
				}
				sentence.words.add(wordform);
			}
			return corpus;
		}
		
		public static List<String> partsOfSpeech() {
			List<String> pos = new ArrayList<>();
			pos.add("NOUN");
			pos.add("PROPN");
			pos.add("ADJ");
			pos.add("PRON");
			pos.add("NUM");
			pos.add("VERB");
			pos.add("ADV");
			pos.add("DET");
			pos.add("CONJ");
			pos.add("ADP");
			pos.add("PART");
			pos.add("H");
			pos.add("INTJ");
			pos.add("PUNCT");
			return pos;
		}

}
