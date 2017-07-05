package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Wordform {
	public String wordform;
	public String lemma;
	public String partOfSpeech;
	public String grammems;
	public String additionalFeatures;
	public int number;
	
	public String toString(){
		return wordform;
	}
	
	public String getCase() {
		if (grammems.contains("Case=")) {
			String c = grammems.substring(grammems.indexOf("Case="), grammems.indexOf("Case=")+8);
			String caseName = c.substring(c.indexOf("=")+1, c.length());
			return caseName;
		}
		return null;
	}
	
	public List<String> getCaseGenderNumber() {
		if (!grammems.contains("Case=")) {
			return null;
		}
		List<String> grammem = new ArrayList<String>();
		HashMap<String, String> gr = new HashMap<>();
		String[] g = grammems.split("\\|");
		for (String s : g) {
			String[]ss = s.split("=");
			gr.put(ss[0], ss[1]);
		}
		if (gr.keySet().contains("Case")) {
			grammem.add(gr.get("Case"));
		}
		if (gr.keySet().contains("Gender")) {
			grammem.add(gr.get("Gender"));
		}
		if (gr.keySet().contains("Number")) {
			grammem.add(gr.get("Number"));
		}
		return grammem;
	}
}
