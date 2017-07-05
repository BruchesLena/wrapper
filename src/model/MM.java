package model;

import java.util.List;


public class MM {

	public static void main(String[] args) {
		
		FeatureExtractor<Wordform> featureExtractor=new FeatureExtractor<Wordform>() {
			
			@Override
			public double[] getFeatures(Wordform data) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		CategoryProvider<Wordform> cat=new CategoryProvider<Wordform>() {
			
			@Override
			public int getCategory(Wordform data) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		DataSet<Wordform> ds=new DataSet<Wordform>() {
			
			@Override
			public List<Wordform> getData() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		Experiment<Wordform>ex=new Experiment<>(new Model(), featureExtractor, cat, ds);
		ex.run();
	}
}
