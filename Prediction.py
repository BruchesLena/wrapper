import argparse
import numpy as np
from keras.models import Sequential
from keras.layers.normalization import BatchNormalization
from keras.layers.core import Flatten, Dense

parser = argparse.ArgumentParser()
parser.add_argument('model_path')
parser.add_argument('data_path')
parser.add_argument('number_of_data', type=int)
parser.add_argument('data_in_file', type=int)
args = parser.parse_args()

def parseModel(model_path):
    with open(model_path, 'r') as f:
        lines = f.readlines()
    model = Sequential()
    for line in lines:
        parts = line.split(", ")
        if "BatchNormalization" in parts[0]:
            model.add(BatchNormalization())
            continue
        if "Flatten" in parts[0]:
            model.add(Flatten())
            continue
        if "Dense" in parts[0]:
            function = parts[1].split("=")[1]
            neurons = parts[2].split("=")[1]
            input_dim = 0
            if len(parts) == 4:
                input_dim = parts[3].split("=")[1]
            if input_dim != 0:
                model.add(Dense(int(neurons), activation=function, input_dim=int(input_dim)))
            else:
                model.add(Dense(int(neurons), activation=function))
    return model


def predict():
    model = parseModel(args.model_path)
    model.load_weights(args.data_path+"weightsOut")
    for i in range(0, args.number_of_data):
        print "Predicting " + str(i) + " file"
        with open(args.data_path+"learnSet"+str(i), 'r') as f:
            lines = f.read().split('\n')
        samples = []
        for l in range(args.data_in_file):
            line = lines[l]
            d = line.split(";")
            inputArray = np.fromstring(d[1], dtype=np.float32, sep=",")
            samples.append(inputArray)
        samples = np.array(samples)
        predictions = model.predict_on_batch(samples)
        with open(args.data_path+"res"+str(i)+".txt", 'w') as ff:
            num = 0
            for z in predictions:
                s = ""
                numPos = 0
                for val in z:
                    if val > 0.01:
                        s = s + str(numPos) + ":" + str(val) + ","
                    numPos += 1
                ff.write(s + "\n")
                num += 1

predict()
