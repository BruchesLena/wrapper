import argparse
import os

import numpy

from keras.models import Sequential
from keras.layers.normalization import BatchNormalization
from keras.layers.core import Flatten, Dense
from keras.optimizers import SGD, Adam, RMSprop
import keras.callbacks


parser = argparse.ArgumentParser()
parser.add_argument('model_path')
parser.add_argument('data_path')
parser.add_argument('learning_rate', type=float)
parser.add_argument('algorithm')
parser.add_argument('batch_size', type=int)
parser.add_argument('number_of_classes', type=int)
parser.add_argument('training_samples', type=int)
parser.add_argument('test_samples', type=int)
parser.add_argument('samples_in_file', type=int)
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


def parseAlgorithm(algorithm, learning_rate):
    if algorithm == 'SGD':
        return SGD(lr=learning_rate)
    if algorithm == 'Adam':
        return Adam(lr=learning_rate)
    if algorithm == 'RMSprop':
        return RMSprop(lr=learning_rate)
    else:
        print "Algorithm is unknown"


class DataChunk:
    def __init__(self, path,pathData):
        self.path = path
        v=path.index("/");
        self.pathDaa=pathData+path[v+1:len(path)]

    def getInput(self):
        return self.path+"input.npy"

    def getOutput(self):
        return self.path + "output.npy"

    def aquireData(self):
        inputPath=self.getInput()
        outputPath = self.getOutput()
        if os.path.exists(inputPath)&os.path.exists(outputPath):
            inputArray=numpy.load(inputPath);
            outputArray = numpy.load(outputPath);
            return inputArray,outputArray
        if os.path.exists(self.path):
            self.prepareData()
            if os.path.exists(inputPath) & os.path.exists(outputPath):
                return self.aquireData();
            raise IOError("Data preparation failed")
        raise IOError("Chunk file does not exists")

    def prepareData(self):
        print "Preparing"
        inputs,outputs=self.internalPrepare();
        inputs = numpy.array(inputs)
        outputs = numpy.array(outputs)
        numpy.save(self.getInput(), inputs)
        numpy.save(self.getOutput(), outputs)

    def internalPrepare(self):
        f = file(self.path, "r")
        inputs = []
        outputs = []
        num = 0
        for l in f.readlines():
            last = l.index(';');
            first = l[0: last].strip()
            if first == 'null':
                continue
            second = l[last + 1:len(l)].strip();
            inputArray = numpy.fromstring(second, dtype=numpy.float32, sep=",");
            output = numpy.zeros(dtype=numpy.float32, shape=args.number_of_classes)
            output = numpy.array(output);
            catNum = int(first);
            num = num + 1
            if (num % 1000 == 0):
                print num
            output[catNum] = 1;
            input = numpy.array(inputArray);
            #input = input.reshape(200, 35)
            inputs.append(input);
            outputs.append(output);
        return inputs,outputs


def chunker(path,p1,pref):
    res=[]
    for v in range(len(os.listdir(path))):
        fp=path + pref + str(v);
        if os.path.exists(fp):
            res.append(DataChunk(fp,p1))
        else:
            return res
    return res


def GeneratorFromChunks(chunks):
    while True:
        for i in chunks:
            (x, y) = i.aquireData()
            yield x, y


class StatPrinter(keras.callbacks.Callback):
    def __init__(self, model,batch):
        super(StatPrinter, self).__init__()
        self.model=model
        self.batch=batch

    def on_epoch_end(self, epoch, logs={}):
        sucCount = 0
        totalCount = 0;
        for v in self.batch:
            input,output=v.aquireData();
            classes = self.model.predict_classes(input);
            for i in range(0, len(output)):
                num = numpy.argmax(output[i]);
                if (classes[i] == num):
                    sucCount = sucCount + 1;
                totalCount = totalCount + 1;
        print ""
        print "Samples count:"+str(sucCount)+":"+str(totalCount)

ds=chunker(args.data_path, args.data_path, "learnSet")
training=GeneratorFromChunks(ds[0:args.training_samples])
test=GeneratorFromChunks(ds[args.training_samples:args.training_samples+args.test_samples])
modelPath = args.data_path + "weightsOut"


def train():
    global ds,test,training, modelPath
    saver=keras.callbacks.ModelCheckpoint(modelPath, monitor='val_loss', verbose=1, save_best_only=True,mode='auto')
    stopper=keras.callbacks.EarlyStopping(monitor='val_loss', patience=5, verbose=1, mode='auto')
    model = parseModel(args.model_path)
    st=StatPrinter(model,ds[args.training_samples:args.training_samples+args.test_samples])
    algorithm = parseAlgorithm(args.algorithm, args.learning_rate)
    model.compile(optimizer=algorithm, loss='categorical_crossentropy')
    training_samples = args.training_samples
    validation_samples = args.test_samples
    model.fit_generator(training,steps_per_epoch=training_samples,epochs=2,max_q_size=3,validation_data=test,validation_steps=validation_samples,callbacks=[saver,stopper, st])

train()

