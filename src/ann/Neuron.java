package ann;

/**
 * Handles a single neuron. Keeps track of state and calculates output values.
 * @author Isac Arnekvist
 *
 */
public class Neuron {
	
	private double value;
	
	/**
	 * Constructor
	 */
	protected Neuron() {
		
	}
	
	/**
	 * Send this neuron a signal
	 * @param signal A double value. My idea is: (-1, 1)
	 */
	protected void input(double signal) {
		value += signal;
	}
	
	/**
	 * Resets neuron to zero, should be done before sending new inputs to it.
	 */
	protected void reset() {
		value = 0;
	}
	
	/**
	 * Calculates signal from previously given inputs. 
	 * @return Double value within (-1, 1)
	 */
	protected double getOutput() {
		return Math.tanh(value);
	}
	
}
