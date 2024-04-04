import numpy as np
import pandas as pd
import tkinter
from tkinter import Tk 
from tkinter import filedialog as fd

#   Naive Bayes Method by Kieran Yee

#   Data query and pre-processing
Tk().withdraw()
print('Select the TRAINING dataset')
file1 = fd.askopenfilename(initialdir="/", title="Select TRAINING data file",
                    filetypes=(("CSV files", "*.CSV"),("all files", "*.*")))
print('Select the TEST dataset')
file2 = fd.askopenfilename(initialdir="/", title="Select TEST data file",
                    filetypes=(("CSV files", "*.CSV"),("all files", "*.*")))
traindata = pd.read_csv(file1)
testdata = pd.read_csv(file2)
trainlabels = traindata.iloc[:,1]
traininstances = traindata.iloc[:,1:]
trainfeatures = list(traininstances.columns)[1:]
test_labels = testdata.iloc[:,1]
test_instances = testdata.iloc[:,1:]
labels = trainlabels.values
instances = traininstances.values
testlabels = test_labels.values
testinstances = test_instances.values

#   Function to calculate prior probabilities e.g. P(y) from trainset (not used in end)
def calc_prior(labels):    
    priors = {}
    for i in range(len(labels)):
        prob = sum(labels == labels[i])
        priors[labels[i]] = prob / len(labels)
    return priors

#   Alg 1: Training the Naive Bayes Classifier
#   Input: The training set 
#   Outputs: A probability table
def trainer(labels, instances):
    counts = {}    
    #   Initialize counts to 1
    for label in set(labels):
        counts[label] = 1
        for feature in trainfeatures:
            for value in traininstances[str(feature)]:
                counts[feature, value, label] = 1
    
    #   Count the numbers of each class / feature value based on trainset
    for instance in instances:
        #print(instance)
        label = instance[0]
        counts[label] = counts[label] + 1
        for i in range(len(trainfeatures)):
            counts[trainfeatures[i], instance[1:][i], label] = counts[trainfeatures[i], instance[1:][i], label] + 1
    
    #   Calculate the total/denoms
    class_tot = 0
    totals = {}
    for label in set(labels):
        class_tot = class_tot + counts[label]        
        for feature in trainfeatures:
            totals[feature, label] = 0
            for value in traininstances[str(feature)].drop_duplicates():
                totals[feature, label] = totals[feature, label] + counts[feature, value, label]

    #   Calculate probablities from the counted nums
    probs = {}
    for label in set(labels):        
        probs[label] = counts[label] / class_tot        
        for feature in trainfeatures:
         #   print(feature)
            for value in traininstances[str(feature)].drop_duplicates():
         #       print(value)
                probs[feature, value, label] = counts[feature, value, label] / totals[feature, label]
   
    return probs

#   Alg 2: Calcution of class score
#   Input: A test instance / class label / probability table
#   Outputs: The score
def score(instance, class_label, prob):
    score = prob[class_label]
    cnt = 0
    for i in range(len(trainfeatures)):
            try:
                score = score * prob[trainfeatures[i], instance[i+1], class_label]
                cnt = cnt + 1                
            except KeyError:
                pass    
    return score
    
def predict(instance, prob):
    unilabels = np.unique(labels)
    a = score(instance, unilabels[0], prob)
    b = score(instance, unilabels[1], prob)
    if a > b:
        print("Predicted class: "+unilabels[0]+" Real class: "+instance[0])
        if unilabels[0] == instance[0]:
            return 1
        else:
            return 0
    if b > a:
        print("Predicted class: "+unilabels[1]+" Real class: "+instance[0])
        if unilabels[1] == instance[0]:
            return 1
        else:
            return 0
    
if __name__ == '__main__':  
    prob = trainer(labels, instances)
    pred = score(testinstances[0], labels[0], prob)

    for testinst in testinstances:
        unilabels = np.unique(labels)
        sc1 = score(testinst, unilabels[0], prob)
        sc2 = score(testinst, unilabels[1], prob)
        
    preds = 0
    its = 0
    for inst in testinstances:
        p = predict(inst, prob)
        preds = preds + p
        its = its + 1
        print("Correct predictions: "+str(preds)+" "+str((preds/its)))

    print("\nTotal correct predictions: "+str(preds)+"/"+str(its))
    print("Final classification accuracy: "+str(preds/its))