import numpy as np
import pandas as pd

#   A simple neural network by Kieran Yee


class Neural_Network:
    # Initialize the network
    def __init__(self, num_inputs, num_hidden, num_outputs, hidden_layer_weights, output_layer_weights, learning_rate, initial_bias):
        self.num_inputs = num_inputs
        self.num_hidden = num_hidden
        self.num_outputs = num_outputs

        self.hidden_layer_weights = hidden_layer_weights
        self.output_layer_weights = output_layer_weights

        self.learning_rate = learning_rate
        self.initial_bias = initial_bias

    # Calculate neuron activation for an input
    def sigmoid(self, input):
        output = 1 / (1 + np.exp(-input))
        return output

    # Feed forward pass input to a network output
    def forward_pass(self, inputs):
        hidden_layer_outputs = []

        for i in range(self.num_hidden):
            weighted_sum = 0
            for j in range(self.num_inputs):
                weighted_sum = weighted_sum + \
                    (inputs[j]*self.hidden_layer_weights[j][i])

            weighted_sum = weighted_sum + self.initial_bias[0][i]
            output = self.sigmoid(weighted_sum)
            hidden_layer_outputs.append(output)

        output_layer_outputs = []

        for i in range(self.num_outputs):
            weighted_sum = 0
            for j in range(self.num_hidden):
                weighted_sum = weighted_sum + \
                    (hidden_layer_outputs[j]*self.output_layer_weights[j][i])

            weighted_sum = weighted_sum + self.initial_bias[1][i]
            output = self.sigmoid(weighted_sum)
            output_layer_outputs.append(output)

        return hidden_layer_outputs, output_layer_outputs

    # Backpropagate error and store in neurons
    def backward_propagate_error(self, inputs, hidden_layer_outputs, output_layer_outputs, desired_outputs):

        output_layer_betas = np.zeros(self.num_outputs)
        for i in range(self.num_outputs):
            beta = desired_outputs[i] - output_layer_outputs[i]
            output_layer_betas = np.insert(output_layer_betas, i, beta)
        # print('OL betas: ', output_layer_betas)

        hidden_layer_betas = np.zeros(self.num_hidden)
        for i in range(self.num_hidden):
            beta = 0
            for j in range(self.num_outputs):
                beta += output_layer_outputs[i]*self.output_layer_weights[i][j]*(
                    1-output_layer_outputs[i])*output_layer_betas[i]
            hidden_layer_betas = np.insert(hidden_layer_betas, i, beta)
        # print('HL betas: ', hidden_layer_betas)

        # This is a HxO array (H hidden nodes, O outputs)
        delta_output_layer_weights = np.zeros(
            (self.num_hidden, self.num_outputs))
        for i in range(self.num_hidden):
            weight = 0
            for j in range(self.num_outputs):
                weight = self.learning_rate*hidden_layer_outputs[i]*output_layer_outputs[j]*(
                    1-output_layer_outputs[j])*output_layer_betas[j]
                delta_output_layer_weights[i][j] = weight

        # This is a IxH array (I inputs, H hidden nodes)
        delta_hidden_layer_weights = np.zeros(
            (self.num_inputs, self.num_hidden))
        for i in range(self.num_inputs):
            weight = 0
            for j in range(self.num_hidden):
                weight = self.learning_rate * \
                    inputs[i]*hidden_layer_outputs[j] * \
                    (1-hidden_layer_outputs[j])*hidden_layer_betas[j]
                delta_hidden_layer_weights[i][j] = weight

        # Return the weights we calculated, so they can be used to update all the weights.
        return delta_output_layer_weights, delta_hidden_layer_weights

    def update_weights(self, delta_output_layer_weights, delta_hidden_layer_weights):
        for i in range(self.num_hidden):
            for j in range(self.num_outputs):
                self.output_layer_weights[i][j] = self.output_layer_weights[i][j] + \
                    delta_output_layer_weights[i][j]

        for i in range(self.num_inputs):
            for j in range(self.num_hidden):
                self.hidden_layer_weights[i][j] = self.hidden_layer_weights[i][j] + \
                    delta_hidden_layer_weights[i][j]
        # print('Weights updated')

    def train(self, instances, desired_outputs, desired_integers, epochs):
        print('\nTraining for '+str(epochs)+' epochs')
        for epoch in range(epochs):
            print('epoch = ', epoch)
            predictions = []
            for i, instance in enumerate(instances):
                hidden_layer_outputs, output_layer_outputs = self.forward_pass(
                    instance)
                delta_output_layer_weights, delta_hidden_layer_weights, = self.backward_propagate_error(
                    instance, hidden_layer_outputs, output_layer_outputs, desired_outputs[i])
                predicted_class = pd.Series(output_layer_outputs).idxmax()
                predictions.append(predicted_class)

                # We use online learning, i.e. update the weights after every instance.
                self.update_weights(delta_output_layer_weights,
                                    delta_hidden_layer_weights)

            # print('Hidden layer weights \n', self.hidden_layer_weights)
            # print('Output layer weights  \n', self.output_layer_weights)

            # Print accuracy achieved over this epoch

            acc = self.accuracy(predictions, desired_integers)
            if epoch == 0:
                print('FIRST ACC: ', acc)
            if epoch == epochs-1:
                print("LAST ACCURACY: ", self.accuracy(
                    predictions, desired_integers))
            else:
                print('acc = ', acc)

    def classify(self, instances, desired_integers):
        print('\nClassiying test set')
        predictions = []
        for i, instance in enumerate(instances):
            hidden_layer_outputs, output_layer_outputs = self.forward_pass(
                instance)
            predicted_class = pd.Series(output_layer_outputs).idxmax()
            predictions.append(predicted_class)

       # Print accuracy achieved over this test set

        acc = self.accuracy(predictions, desired_integers)
        print('Test Set acc = ', acc)

    def accuracy(self, predictions, desired):
        count = 0
        for i in range(len(predictions)):
            if predictions[i] == desired[i][0]:
                count += 1
        accuracy = count / len(predictions)
        return accuracy

    def predict(self, instances):
        predictions = []
        for instance in instances:
            hidden_layer_outputs, output_layer_outputs = self.forward_pass(
                instance)
            # Should be 0, 1, or 2.
            predicted_class = pd.Series(output_layer_outputs).idxmax()
            predictions.append(predicted_class)
        # print(predictions)
        return predictions
