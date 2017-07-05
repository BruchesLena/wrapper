package model;
import java.util.HashMap;
import java.util.List;

public class Sentence {
	public List<Wordform> words; 
	public HashMap<Integer, String> wordsForTagging; //key - position in sentence; value - wordform
	public HashMap<Integer, HashMap<String, String>> taggedWords; //key - position in sentence; value - grammem and its value
	public HashMap<Integer, Integer> posTaggedWords; // key - position in sentence; value - pos
	
	public String toString(){
		String sentence = "";
		for (Wordform word : words) {
			sentence += word.wordform+" ";
		}
		return sentence;
	}
	
	public Wordform[] contexts(int i) {
		Wordform[] contexts = new Wordform[6];
		int t = 0;
		for (int z = i-3; z<i+4; z++) {
			if (z==i) {
				continue;
			}
			if (z>=0 && z < words.size()) {
				contexts[t] = words.get(z);
			}
			t++;
		}
		return contexts;
	}
}
