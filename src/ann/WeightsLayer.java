package ann;

import java.util.Random;

/**
 * Handles signals and weights between layers.
 *
 */
public class WeightsLayer {
	
	private double[][] weights; // m x n matrix, m is input, n is output
	private NeuronLayer next;
	private boolean biased;
	
	/**
	 * Creates a new Weights layer
	 * @param in How many sending neurons in previous layer
	 * @param out How many receiving neurons in next layer.
	 * @param biased Should each neuron receive an extra '1' and extra corr. weights?
	 */
	protected WeightsLayer(int in, int out, boolean biased){
		if(in < 1 || out < 1) {
			throw new IllegalArgumentException();
		}
		if(biased) {
			this.biased = true;
			weights = new double[in + 1][out];
		} else {
			this.biased = false;
			weights = new double[in][out];
		}
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
		if(biased) { // Send bias signals
			for(int i = 0; i < next.getSize(); i++) { 
				next.input(weights[getInputs() - 1][i], i);
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
	 * Lets the weight get mutated
	 * @param probability How many times of a hundred that a weight will be redefined.
	 */
	protected void mutate(int probability) {
		Random rand = new Random(System.nanoTime());
		for (int i = 0; i < getInputs(); i++) {
			for (int j = 0; j < getOutputs(); j++) {
				if(rand.nextInt(100) < probability) {
					weights[i][j] = rand.nextFloat()*2 - 1;
				}
			}
		}
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
	protected WeightsLayer mateWithR(WeightsLayer date) {
		Random r = new Random(System.nanoTime());
		WeightsLayer res = new WeightsLayer(getInputs(), getOutputs(), false);
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
		
		if(biased) {
			res.biased = true;
		}
		
		return res;
	}
	
	/**
	 * Splits layers in half an combines.
	 * @param date
	 * @param ratio How many neurons of 100 that should be kept
	 * @return
	 */
	protected WeightsLayer mateWithS(WeightsLayer date, int ratio) {
		int split = ratio*date.getOutputs()/100;
		WeightsLayer res = new WeightsLayer(getInputs(), getOutputs(), false);
		
		for(int i = 0; i < getInputs(); i++) {
			for(int j = 0; j < getOutputs(); j++) {
				if (j > split) {
					res.setWeight(i, j, weights[i][j]);
				} else {
					res.setWeight(i, j, date.weights[i][j]);
				}
			}
		}
		
		if(biased) {
			res.biased = true;
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
