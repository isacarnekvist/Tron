package ann;

/**
 * A complete neural network, a small little brain!!!
 * @author Isac Arnekvist
 */
public class Network {
	
	private NeuronLayer inputLayer;
	private WeightsLayer[] weightLayer;
	private NeuronLayer[] hiddenLayer;
	private NeuronLayer outputLayer;
	private int inputs, outputs;	// How many input neurons
	private int hiddenL, hiddenN;	// How many hidden (L)ayers, and (N)eurons in each of those
	
	/**
	 * Constructor of a complete neural network, a small little brain!!!
	 * @param inputNeurons How many neurons in input layer
	 * @param hiddenLayers How many hidden layers
	 * @param hiddenNeurons How many neurons in each of the hidden layers
	 * @param outputNeurons How many neurons in the output layer
	 */
	public Network(int inputNeurons, int hiddenLayers, int hiddenNeurons, int outputNeurons) {
		if (inputNeurons < 1 || outputNeurons < 1 || hiddenLayers < 0) {
			throw new IllegalArgumentException();
		}
		
		if (hiddenLayers > 0 && hiddenNeurons < 1) {
			throw new IllegalArgumentException();
		}
		
		inputs = inputNeurons;
		outputs = outputNeurons;
		hiddenL = hiddenLayers;
		hiddenN = hiddenNeurons;
		
		weightLayer = new WeightsLayer[hiddenLayers + 1];
		inputLayer = new NeuronLayer(inputNeurons);
		
		if(hiddenLayers != 0) { // There are hidden layers
			weightLayer[0] = new WeightsLayer(inputNeurons, hiddenNeurons); // Between input and first hidden
			
			hiddenLayer = new NeuronLayer[hiddenLayers];
			for (int i = 0; i < hiddenLayers; i++) {
				hiddenLayer[i] = new NeuronLayer(hiddenNeurons);
			}
			
			// Create all weight layers
			// Between hidden layers
			for (int i = 1; i < hiddenLayers; i++) {
				weightLayer[i] = new WeightsLayer(hiddenNeurons, hiddenNeurons);
			}
			
			for (int i = 0; i < hiddenLayers; i++) {
				weightLayer[i].setNextNeuronLayer(hiddenLayer[i]);
			}
			
			outputLayer = new NeuronLayer(outputNeurons);
			weightLayer[hiddenLayers] = new WeightsLayer(hiddenNeurons, outputNeurons);
			weightLayer[hiddenLayers].setNextNeuronLayer(outputLayer);
			
			
		} else { // No hidden layers
			hiddenLayer = new NeuronLayer[0];
			weightLayer[0] = new WeightsLayer(inputNeurons, outputNeurons);
			outputLayer = new NeuronLayer(outputNeurons);
			weightLayer[0].setNextNeuronLayer(outputLayer);
		}
	}
	
	/**
	 * Randomizes all weight in network to doubles [-1, 1]
	 */
	public void randomizeWeights() {
		for(WeightsLayer w : weightLayer) {
			w.randomize();
		}
	}
	
	/**
	 * Send signals to all input neurons
	 * @param signals An array of doubles containing value to neuron i at index i.
	 * @return An array of doubles with output values from the network
	 */
	public double[] evaluate(Double[] signals) {
		inputLayer.input(signals);
		process();
		return outputLayer.getOutputs();
	}
	
	/**
	 * Make sure signals travel through net.
	 */
	private void process() {
		if(hiddenLayer.length == 0) { // No hidden layers
			weightLayer[0].send(inputLayer.getOutputs());
		} else {
			weightLayer[0].send(inputLayer.getOutputs());
			for (int i = 1; i < hiddenLayer.length; i++) {
				weightLayer[i].send(hiddenLayer[i - 1].getOutputs());
			}
			weightLayer[weightLayer.length - 1].send(hiddenLayer[hiddenL - 1].getOutputs());
		}
	}
	
	/**
	 * Creates a new network with a mix between this and the argument sent. This is partially randomized
	 * so that several different 'kids' can be obtained through repeated calls.
	 * @param date The other network.
	 * @return A new network with a combined weights.
	 */
	public Network mateWith(Network date) {
		if(date.hiddenL != hiddenL || date.hiddenN != hiddenN ||
		   date.inputs != inputs || date.outputs != outputs) {
			throw new IllegalArgumentException();
		}
		
		Network res = new Network(inputs, hiddenL, hiddenN, outputs);
		for(int i = 0; i < weightLayer.length; i++) {
			WeightsLayer w = weightLayer[i].mateWith(date.weightLayer[i]);
			for(int j = 0; j < w.getInputs(); j++) {
				for(int k = 0; k < w.getOutputs(); k++) {
					res.weightLayer[i].setWeight(j, k, w.getWeight(j, k));
				}
			}
		}
		
		return res;
	}
	
	/**
	 * Let the network mutate itself
	 * @param probability How many in 100 that a weight will be changed.
	 */
	public void mutate(int probability) {
		for(WeightsLayer w : weightLayer) {
			
		}
	}
	
	/**
	 * @return How many layers in network including input and output layers.
	 */
	public int getLayers() {
		return 2 + hiddenLayer.length;
	}
	
	/**
	 * Prints the weights of all weight layers in this network.
	 */
	public void description() {
		for(WeightsLayer w : weightLayer) {
			w.description();
			System.out.println();
		}
	}
}
