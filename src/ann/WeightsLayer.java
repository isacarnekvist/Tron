package ann;

import java.util.Random;

/**
 * Handles signals and weights between layers.
 * @author Isac Arnekvist
 *
 */
public class WeightsLayer {
	
	private double[][] weights; // m x n matrix, m is input, n is output
	private NeuronLayer next;
	
	/**
	 * Creates a new Weights layer
	 * @param in How many sending neurons in previous layer
	 * @param out How many receiving neurons in next layer.
	 */
	protected WeightsLayer(int in, int out){
		if(in < 1 || out < 1) {
			throw new IllegalArgumentException();
		}
		
		weights = new double[in][out];
		next = null;
	}
	
	/**
	 * Sends a signal from one neuron in previous layer to all neurons in next layer
	 * @param signal Signal value as double [-1, 1]
	 * @param from The index value of the sending neuron.
	 */
	protected void send(double signal, int from) {
		for(int i = 0; i < weights[0].length; i++) {
			next.input(signal*weights[from][i], i);
		}
	}
	
	/**
	 * Sends signals to all neurons in next layer
	 * @param signal Signal value as double [-1, 1]
	 */
	protected void send(double[] signals) {
		for(int i = 0; i < signals.length; i++) {
			for(int j = 0; j < next.getSize(); j++) {
				next.input(signals[i]*weights[i][j], j);
			}
		}
	}
	
	/**
	 * Sets all the weights in this layer to randomized values [-1, 1]
	 */
	protected void randomize() {
		Random r = new Random(System.nanoTime());
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[0].length; j++) {
				weights[i][j] = r.nextFloat()*2 - 1;
			}
		}
	}
	
	/**
	 * Set what layer to send signals to.
	 * @param nl Next neuron layer
	 */
	protected void setNextNeuronLayer(NeuronLayer nl) {
		if (nl.getSize() != weights[0].length) {
			throw new IllegalArgumentException();
		}
		next = nl;
	}
	
	/**
	 * All arguments are including that index! [0 length-1]
	 * @param fromInput
	 * @param toInput
	 * @param fromOutput
	 * @param toOutput
	 * @return A sub matrix with row indices as inputs and its indices as outputs
	 */
	protected double[][] getWeights(int fromInput, int toInput, int fromOutput, int toOutput) {
		double[][] res = new double[toInput - fromInput + 1][toOutput - fromOutput + 1];
		
		for(int row = fromInput; row <= toInput; row++) {
			for(int col = fromOutput; col <= toOutput; col++) {
				res[row - fromInput][col - fromOutput] = weights[row][col];
			}
		}
		
		return res;
	}
	
	/**
	 * Inserts new values for weights
	 * @param newWeights A 2-d array of doubles with new weights
	 * @param fromInput
	 * @param toInput
	 * @param fromOutput
	 * @param toOutput
	 */
	protected void insertWeights(double[][] newWeights, int fromInput, int toInput, int fromOutput, int toOutput) {
		for(int row = fromInput; row <= toInput; row++) {
			for(int col = fromOutput; col <= toOutput; col++) {
				weights[row][col] = newWeights[row - fromInput][col - fromOutput];
			}
		}
	}
	
	/**
	 * @param input
	 * @param output
	 * @param toValue The new value to be set to
	 */
	protected void setWeight(int input, int output, double toValue) {
		weights[input][output] = toValue;
	}
	
	/**
	 * @param input
	 * @param output
	 * @return The weight for this indices
	 */
	protected double getWeight(int input, int output) {
		return weights[input][output];
	}
	
	/**
	 * Mix this with the argument sent. Randomly mixed.
	 * @param date The matrix to mix with
	 * @return The next generations weights
	 */
	protected WeightsLayer mateWith(WeightsLayer date) {
		Random r = new Random(System.nanoTime());
		WeightsLayer res = new WeightsLayer(getInputs(), getOutputs());
		for(int i = 0; i < getInputs(); i++) {
			for(int j = 0; j < getOutputs(); j++) {
				switch (r.nextInt(2)) {
				case 0:
					res.setWeight(i, j, weights[i][j]);
					break;
				case 1:
					res.setWeight(i, j, date.weights[i][j]);
					break;
				}
			}
		}
		
		return res;
	}
	
	/**
	 * @return How many inputs
	 */
	protected int getInputs() {
		return weights.length;
	}
	
	/**
	 * @return How many outputs
	 */
	protected int getOutputs() {
		return weights[0].length;
	}
	
	/**
	 * Prints the weights matrix to System.out
	 */
	protected void description() {
		for(double[] row : weights) {
			for(double d : row) {
				System.out.printf("%.1f\t", d);
			}
			System.out.println();
		}
	}
}
