# wrapper
A Java wrapper around Keras

# Prerequisites
The following Python packages are required:
* numpy
* Keras

The following Java packages are required:
* com.onpositive.wordnet

# Installation
Download a release zip-file and extract it.

# Wrapper functionality
* Training your own network;
* Predicting classes for data using trained network

# Available elements
* Layers: Dense, BatchNormalization
* Activation functions: relu, softmax
* Algorithms: SGD

# Demo example for training
1. Create your own model:
* Create an instance of Model:
```java
Model model = new Model();
```
* Construct your model by adding layers: 
```java
model.add(new Dense(50, "relu", 735));
model.add(new BatchNormalization());
model.add(new Dense(486, "softmax"));
```
*Note that it is needed to indentify an input dimension for the first layer!*
* Name your model: 
```java
model.setName("MyModel");
```
* Provide the path, where the model will be saved: 
```java
model.setPath("D:/Morph/SimpleTest/model.txt");
```
2. Create an experiment:
* Create an instance of Experiment with the Model and the way of data preparation: 
```java
MyExperiment exp = new MyExperiment(model, new GrammemPreparation());
```
* Set the parameters for your Experiment:
  ```java
  exp.setNumberOfClasses(486); // number of target classes
	exp.setTrainingSamples(300); // number of batches (files) with training samples
	exp.setTestSamples(100); // number of batches (files) with validation samples
	exp.setSamplesInFile(5000); // number of samples in batch (file), by default is 1000
  exp.setLearningRate(0.1f) // learning rate, by default is 0.01
  exp.setAlgorithm("Adam") // algorithm for learning, by default is "SGD"
  ```
3. Provide the path to the python file "SimpleTest.py":
```java
exp.setPythonFile(YOUR_PATH)
```
4. Load your data (for this demo you can download [this corpus](https://github.com/dialogue-evaluation/morphoRuEval-2017/blob/master/OpenCorpora_Texts.rar)):
```java
List<Sentence> corpus = Corpora.loadCorpus(PATH_TO_DATA, true);
```
*Note that 'true' is only for the corpus on the link above!*

5. Now you are ready to run this experiment:
```java
exp.run(corpus, DIRECTORY_WITH_DATA);
```
Once the experiment finishes, in the directory with your data two new files will be created:
  * weights for the trained network;
  * statistics for this experiment
  
# Demo example for predicting
1. Create model for weights you have:
* Create an instance of Model:
```java
Model model = new Model();
```
* Construct your model by adding layers: 
```java
model.add(new Dense(50, "relu", 735));
model.add(new BatchNormalization());
model.add(new Dense(486, "softmax"));
```
*Note that it is needed to indentify an input dimension for the first layer!*
* Name your model: 
```java
model.setName("MyModel");
```
* Provide the path, where the model will be saved: 
```java
model.setPath("D:/Morph/SimpleTest/model.txt");
```
2. Provide the path to the python file "Prediction.py":
```java
model.setPythonFile(YOUR_PATH);
```
3. Now you are ready to get predictions:
```java
model.predict(DIRECTORY_WITH_DATA, 3, 5000);
```
*Here in example 3 is the general number of batches (files) you want to get prediction for and 1500 is the general number of samples in a batch (file).
For this demo you can use the corpus on the link above.*

*Make sure that data and weights for the model are located in the same directory!*

Once the predictions are made, corresponding files with results will be created in the directory with data.


