package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyExperiment {
	
	Model model; 
	
	float learningRate = 0.01f;
	String algorithm = "SGD";
	int batchSize = 1000;
	
	model.DataPreparation dataPreparation;
	
	int numberOfClasses;
	int trainingSamples;
	int testSamples;
	int samplesInFile;
	
	String pythonFile = "C:/Users/onpositive/PycharmProjects/untitled1/Experiments/SimpleTest.py";
	
	public MyExperiment(Model model, model.DataPreparation dataPreparation) {
		super();
		this.model = model;
		this.dataPreparation = dataPreparation;
	}
	
	public void setPythonFile(String pythonFile) {
		this.pythonFile = pythonFile;
	}
	
	public void setNumberOfClasses(int n) {
		this.numberOfClasses = n;
	}
	
	public void setTrainingSamples(int n) {
		this.trainingSamples = n;	
	}
	
	public void setTestSamples(int n) {
		this.testSamples = n;
	}
	
	public void setSamplesInFile(int n) {
		this.samplesInFile = n;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public float getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(float learningRate) {
		this.learningRate = learningRate;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	class Printer extends Thread{
	
		private BufferedReader reader;

		Printer(BufferedReader r){
			this.reader=r;
		}
		
		public void run(){
			String errorLine = null;
			try {
				while ((errorLine = reader.readLine())!=null) {
					System.out.println(errorLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Run the experiment with given parameters.
	 * Note that by default learning rate = 0.01, algorithm = "SGD", batchSize = 1000. 
	 * After the model trained, the weights and the statistics for this experiment will be saved in the directory with the data for this experiment.
	 * @param data the corpus which will be prepared as training and validation data according to the chosen way of preparation
	 * @param dataPath the directory where the data are stored
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public void run(List<model.Sentence> data, String dataPath) throws IOException, InterruptedException {
		model.writeInFile();
		
		File folder = new File(dataPath);
		File[] files = folder.listFiles();
		if (files.length<2) {
			dataPreparation.print(data, dataPath);
		}
		
		
		String[] arg = new String[3];
		arg[0] = "PATH="+System.getenv("PATH");	
		arg[1] = "OMP_NUM_THREADS=4";
		arg[2] = "USERPROFILE=4";
		ProcessBuilder processBuilder = new ProcessBuilder();
		Map<String, String> environment = processBuilder.environment();
		ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(new String[]{pythonFile,model.path,dataPath}));
		for (String s:experimentSettings().split(" ")){
			arrayList.add(s);
		};
		for (String s:samplesSettings().split(" ")){
			arrayList.add(s);
		};
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
		
		new Printer(brError).start();
		//new Printer(brStdout).start();
		List<String> counter = new ArrayList<>();
		List<String> losses = new ArrayList<>();
		String line = null;
		String bufLine = null;
		while ((line=brStdout.readLine())!=null) {
			System.out.println(line);
			
			if (!line.startsWith("Epoch")&&!line.contains("Epoch 1/")) {
				bufLine = line;				
			}
			else if(!line.contains("Epoch 1/")) {
				losses.add(bufLine);
			}
			if (line.startsWith("Samples")) {
				counter.add(line);
			}
		}
		losses.add(bufLine);
		int exitVal = process.waitFor();
		System.out.println(exitVal);
		
		PrintWriter ps = new PrintWriter(new BufferedWriter(new FileWriter(dataPath+"trainResults" + model.name+".txt")));
		ps.println(model.toString());
		ps.println("Settings: " + experimentSettings());
		ps.println("Accuracy: " + accuracy(counter.get(counter.size()-1)) );
		ps.println();
		for (int i = 0; i < counter.size(); i++) {
			ps.println("Epoch " + (i+1));
			ps.println(counter.get(i));
			ps.println(losses.get(i));
			ps.println();
		}
		ps.close();
		System.out.println("Results are saved in file.");
	}
	
	public static double accuracy(String s) {
		String[] ss = s.split(":");
		double accuracy = (Integer.parseInt(ss[1])*1.0) / (Integer.parseInt(ss[2])*1.0);
		return accuracy;		
	}
	
	public String experimentSettings() {
		return learningRate+" "+algorithm+" "+batchSize;
	}
	
	public String samplesSettings() {
		return numberOfClasses+" "+trainingSamples+" "+testSamples+" "+samplesInFile;
	}
	

}
