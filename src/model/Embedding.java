package model;

public class Embedding extends Layer{
	int dictionarySize;
	int vectorSize;
	int inputLength;
	
	public Embedding(int dictionarySize, int vectorSize, int inputLength) {
		this.dictionarySize = dictionarySize;
		this.vectorSize = vectorSize;
		this.inputLength = inputLength;
	}
	
	public String toString() {
		String layer = "Layer=Embedding";
		String dictSize = "dict_size=" + dictionarySize;
		String vecSize = "vector_size=" + vectorSize;
		String inputLenght = "input_length=" + inputLength;
		return layer+", "+dictSize+", "+vecSize+", "+inputLenght;
	}
}
