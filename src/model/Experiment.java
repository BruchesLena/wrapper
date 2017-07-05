package model;

public class Experiment<T> {
	
	Model model;

	float learningRate=0.01f;
	String algorithm="SGD";
	int batchSize=100;
	
	FeatureExtractor<T> featureExtractor;
	
	public Experiment(Model model, FeatureExtractor<T> featureExtractor, CategoryProvider<T> cat, DataSet<T> ds) {
		super();
		this.model = model;
		this.featureExtractor = featureExtractor;
		this.cat = cat;
		this.ds = ds;
	}

	CategoryProvider<T> cat;
	
	DataSet<T> ds;

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

	public FeatureExtractor<T> getFeatureExtractor() {
		return featureExtractor;
	}

	public void setFeatureExtractor(FeatureExtractor<T> featureExtractor) {
		this.featureExtractor = featureExtractor;
	}

	public CategoryProvider<T> getCat() {
		return cat;
	}

	public void setCat(CategoryProvider<T> cat) {
		this.cat = cat;
	}

	public DataSet<T> getDs() {
		return ds;
	}

	public void setDs(DataSet<T> ds) {
		this.ds = ds;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
	

}
