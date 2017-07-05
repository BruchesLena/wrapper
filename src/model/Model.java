package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.MyExperiment.Printer;

public class Model {
	String name;
	List<Layer> layers;
	String path;
	String pythonFile = "C:/Users/onpositive/PycharmProjects/untitled1/Experiments/Prediction.py";
	
	public Model() {
		layers = new ArrayList<>();
	}
	
	public String toString() {
		String model = name+"\n";
		for (Layer layer : layers) {
			model += layer.toString()+"\n";
		}
		return model;
	}
	
	public void writeInFile() throws IOException {
		PrintWriter ps = new PrintWriter(new BufferedWriter(new FileWriter(path)));
		String mod = toString();
		ps.print(mod);
		ps.close();
	}
	
	public void add(Layer layer) {
		layers.add(layer);
	}
	
	public void setPythonFile(String pythonFile) {
		this.pythonFile = pythonFile;
	}
	
	/**
	 * Create a name for the model.
	 * @param name new name for the model
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Provide a path to the directory where the model will be saved.
	 * @param path 
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Predict a class for your data using trained model.
	 * Note that weights for the model and the data should be stored in the same directory.
	 * @param dataPath the directory where the data and weights are stored
	 * @param numberOfData the general number of batches (files) you want to get prediction for
	 * @param samplesInFile the general number of samples in a batch (file)
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void predict(String dataPath, int numberOfData, int samplesInFile) throws IOException, InterruptedException {
		writeInFile();
		String[] arg = new String[3];
		arg[0] = "PATH="+System.getenv("PATH");	
		arg[1] = "OMP_NUM_THREADS=4";
		arg[2] = "USERPROFILE=4";
		ProcessBuilder processBuilder = new ProcessBuilder();
		Map<String, String> environment = processBuilder.environment();
		ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(new String[]{pythonFile,path,dataPath}));
		arrayList.add(String.valueOf(numberOfData));
		arrayList.add(String.valueOf(samplesInFile));
		arrayList.add(0,"python.exe");
		processBuilder.command( arrayList);
		System.out.println("Started");
		Process process = processBuilder.start();
		InputStream stdout = process.getInputStream();
		InputStreamReader isrStdout = new InputStreamReader(stdout);
		BufferedReader brStdout = new BufferedReader(isrStdout);
		
		InputStream error = process.getErrorStream();
		InputStreamReader isrError = new InputStreamReader(error);
		BufferedReader brError = new BufferedReader(isrError);
		
		String line = null;
		while((line = brStdout.readLine())!=null) {
			System.out.println(line);
		}
		int exitValue = process.waitFor();
		System.out.println(exitValue);
	}
}
