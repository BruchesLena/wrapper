package model;

public class Dense extends Layer {

	public Dense(int neurons, String function) {
		this.function = function;
		this.neurons = neurons;
	}
	
	public Dense(int neurons, String function, int inputDim) {
		this.function = function;
		this.inputDim = inputDim;
		this.neurons = neurons;
	}
	
	public String toString() {
		String layer = "Layer=Dense";
		String func = "function="+function;
		String neur = "neurons="+neurons;
		if (inputDim != 0) {
			String input_dim = "input_dim="+inputDim;
			return layer+", "+func+", "+neur+", "+input_dim;
		}
		else {
			return layer+", "+func+", "+neur;
		}
	}
}
