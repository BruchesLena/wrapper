package model;

public interface FeatureExtractor<T> {
	
	public double[] getFeatures(T data);
}
