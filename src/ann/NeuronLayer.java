package ann;

public class NeuronLayer {
	
	private Neuron[] neurons;
	
	/**
	 * Constructor
	 * @param size How many neurons in this layer
	 */
	protected NeuronLayer(int size) {
		if(size < 1) {
			throw new IllegalArgumentException();
		}
		
		neurons = new Neuron[size];
		for (int i = 0; i < size; i++) {
			neurons[i] = new Neuron();
		}
	}
	
	/**
	 * Input a signal to one of the neurons
	 * @param signal The signal value as double
	 * @param neuron The index of the neuron to send to
	 */
	protected void input(Double signal, int neuron) {
		neurons[neuron].input(signal);
	}
	
	/**
	 * Input signals to all neurons in layer
	 * @param signal An array of doubles with value to neuron i at index i.
	 */
	protected void input(Double[] signal) {
		for(int i = 0; i < signal.length; i++) {
			neurons[i].input(signal[i]);
		}
	}
	
	/**
	 * @return An array of doubles with all the corresponding outputs from this
	 * layers neurons. This does reset their outputs!
	 */
	protected double[] getOutputs() {
		double[] res = new double[neurons.length];
		
		for(int i = 0; i < neurons.length; i++) {
			res[i] = neurons[i].getOutput();
			neurons[i].reset();
		}
		
		return res;
	}
	
	/**
	 * @return Amount of neurons in layer.
	 */
	protected int getSize() {
		return neurons.length;
	}
	
}
