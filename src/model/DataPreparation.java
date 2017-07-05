package model;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataPreparation {
	public static List<String> partsOfSpeech;
	public static List<String> fixedWords;
	public static int vectorSize;
	public static List<String> features;
	
	public DataPreparation() throws IOException {
		// TODO Auto-generated constructor stub
		features = loadFeatures("allFeaturesPOS.ser");
		partsOfSpeech = Corpora.partsOfSpeech();
		fixedWords = new ArrayList<>();		
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/ADP.txt"));
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/CONJ.txt"));
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/DET.txt"));
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/H.txt"));
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/PART.txt"));
//		fixedWords.addAll(FixedWords.loadWords("D:/MorphoRuEval/PRON.txt"));
	}
	
	public static List<String> loadFeatures(String path) {
		List<String> features = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(path);
			in = new ObjectInputStream(fis);
			features = (List<String>) in.readObject();
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return features;
	}
	
	public void print(List<model.Sentence> data, String path) throws IOException {
		int num = 0;
		int count = 0;
		PrintWriter ps = new PrintWriter(new BufferedWriter(new FileWriter(path + "learnSet" + count)));
		for (Sentence sentence : data) {
			for (int i = 0; i < sentence.words.size(); i++) {
//				int pos = sentence.posTaggedWords.get(i);
//				if (pos==0||pos==1||pos==2||pos==3||pos==4||pos==7||pos==5||pos==6) {
//						Wordform[] context = sentence.contexts(i);
//						printEntry(ps, sentence.words.get(i), context);
//						num++;
//						System.out.println("print " + num);
//						if (num % 500 == 0) {
//							ps.close();
//							count++;
//
//							ps = new PrintWriter(new BufferedWriter(new FileWriter(path + "learnSet" + count)));
//
//						}
//				}
				
			if (sentence.words.get(i).partOfSpeech.equals("NOUN")||sentence.words.get(i).partOfSpeech.equals("PRON")||sentence.words.get(i).partOfSpeech.equals("DET")||
				sentence.words.get(i).partOfSpeech.equals("ADJ")||sentence.words.get(i).partOfSpeech.equals("VERB")||sentence.words.get(i).partOfSpeech.equals("NUM")||sentence.words.get(i).partOfSpeech.equals("ADV")) {

//				if (sentence.words.get(i).partOfSpeech.equals("PUNCT")||sentence.words.get(i).partOfSpeech.equals("X")||fixedWords.contains(sentence.words.get(i).lemma.toLowerCase())) {
//					continue;
//				}
//				else {
				String f = sentence.words.get(i).partOfSpeech+":";
				if (sentence.words.get(i).grammems.equals("_")) {
					f += "_";
				}
				else {
					HashMap<String, String> grammems = new HashMap<>();
					String[] s = sentence.words.get(i).grammems.split("\\|");
					for (String st : s) {
						if (st.contains("Animacy")||st.contains("Voice")){
							continue;
						}
						String[] grams = st.split("=");
						grammems.put(grams[0], grams[1]);
					}
					for (String v : grammems.values()) {
						f+=v+":";
					}
				}
					Wordform[] context = sentence.contexts(i);
					printEntry(ps, sentence.words.get(i), context, f);
					num++;
					System.out.println("print " + num);
					if (num % 5000 == 0) {
						ps.close();
						count++;

						ps = new PrintWriter(new BufferedWriter(new FileWriter(path + "learnSet" + count)));
						if (num == 2000000) {
							break;
						}
					}
				}
			
			}
		}
		ps.close();
		System.out.println("Printed:" + num);
	}
	
	public void printEntry(PrintWriter ps, Wordform targetWord, Wordform[] context, String f) {
		double[] mm = new double[vectorSize];
		initInputs(targetWord, context, mm);
		if (features.indexOf(f)==-1) {
			System.out.println("!");
		}
		ps.print(features.indexOf(f) + ";");
		for (int i = 0; i < mm.length; i++) {
			ps.print(mm[i]);
			if (i != mm.length - 1) {
				ps.print(" ,");
			}			
		}
		ps.println();
	}
	
	public void printEntry(PrintWriter ps, Wordform targetWord, Wordform[] context) {
		double[] mm = new double[vectorSize];
		initInputs(targetWord, context, mm);
		if (partsOfSpeech.indexOf(targetWord.partOfSpeech)==-1) {
			System.out.println("!");
		}
		ps.print(partsOfSpeech.indexOf(targetWord.partOfSpeech) + ";");
		for (int i = 0; i < mm.length; i++) {
			ps.print(mm[i]);
			if (i != mm.length - 1) {
				ps.print(" ,");
			}			
		}
		ps.println();
	}
	
	public void initInputs(Wordform targetWord, Wordform[] context, double[] mm) {
		return;
	}
}

