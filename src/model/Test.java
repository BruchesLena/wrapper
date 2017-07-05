package model;

import java.io.IOException;
import java.util.List;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		Model myModel = new Model();
		myModel.add(new Dense(50, "relu", 735));
		myModel.add(new BatchNormalization());
		myModel.add(new Dense(486, "softmax"));
		
		myModel.setName("MyModel");
		myModel.setPath("D:/Morph/SimpleTest/model.txt");
		
		MyExperiment exp = new MyExperiment(myModel, new model.GrammemPreparation());
		exp.setLearningRate(0.1f);
		exp.setNumberOfClasses(486);
		exp.setTrainingSamples(10);
		exp.setTestSamples(5);
		exp.setBatchSize(5000);

		List<model.Sentence> corpus = model.Corpora.loadCorpus("D:/MorphoRuEval/OpenCorpora_Texts/unamb_sent_14_6.conllu", true);
		
		exp.run(corpus, "D:/Morph/SimpleTest/");
		//model.predict("D:/Morph/SimpleTest/", 3, 5000);
		
	}

}
