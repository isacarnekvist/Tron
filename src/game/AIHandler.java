package game;

import java.util.LinkedList;
import java.util.Random;

import ann.Network;

public class AIHandler {
	
	private LinkedList<Network> anns;		// Artificial neural networks
	private LinkedList<Network> winners; 	// Which networks won?
	private Network player1AI;
	private Network player2AI;
	private final int startAmount = 10;
	private Random rand;
	private long iteration;					// How many games have run?
	
	/**
	 * 
	 * @param args
	 */
	public AIHandler(int args) {
		anns = new LinkedList<Network>();
		for (int i = 0; i < startAmount; i++) {
			Network n = new Network(args, 2, (args+1)/2, 1, true);
			n.randomizeWeights();
			anns.addLast(n);		
		}
		winners = new LinkedList<Network>();
		player1AI = anns.getFirst();
		player2AI = anns.getFirst();
		
		rand = new Random(System.nanoTime());
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	public int getPlayer1Decision(Double[] args) {
		return getDecision(player1AI, args);
	}
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	public int getPlayer2Decision(Double[] args) {
		return getDecision(player2AI, args);
	}
	
	/**
	 * 
	 * @param player
	 * @param args
	 * @return
	 */
	private int getDecision(Network player, Double[] args) {
		double d[] = player.evaluate(args);
		if(Math.abs(d[0]) < 0.1) {
			return 0;
		} else {
			return (int)Math.signum(d[0]);
		}
	}
	
	/**
	 * Automatically assigns new players so that calls to getPlayer*Decision can be made
	 * @param playerNumber 0 if tie, x if player x
	 */
	public void setWinner(int playerNumber) {
		switch (playerNumber) {
		case 0: // No one won
			
			break;
		case 1:
			winners.addLast(player1AI);
			break;
		case 2:
			winners.addLast(player2AI);
			break;
		}
		
		if(winners.size() >= 2) {
			while(winners.size() > 1) {
				Network n1 = winners.pollFirst();
				Network n2 = winners.pollFirst();
				while(anns.size() < startAmount) {
					anns.addLast(n1);
					anns.addLast(n2);
					anns.addLast(n1.mateWithS(n2, 15));
					anns.addLast(n2.mateWithS(n1, 15));
					anns.addLast(n1.mateWithS(n2, 30));
					anns.addLast(n2.mateWithS(n1, 30));
					anns.addLast(n1.mateWithS(n2, 50));
					anns.addLast(n2.mateWithS(n1, 50));
					anns.addLast(n1.mateWithR(n2));
					anns.addLast(n2.mateWithR(n1));
				}
			}
		}
		
		anns.get(rand.nextInt(anns.size())).mutate(2);
		player1AI = anns.remove(rand.nextInt(anns.size()));
		player2AI = anns.pollFirst();
		
		iteration++;
		System.out.printf("Iteration %d: anns.size() = %d, winners.size() = %d\n", 
				iteration, anns.size(), winners.size());
	}
	
	/**
	 * Returns how many iterations have run
	 * @return
	 */
	public long getIteration() {
		return iteration;
	}
}
