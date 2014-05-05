package game;


// Keeps track of players on the grid and whether there are any collisions
public class Map {
	Bike player1, player2;

	public Map(Bike player1, Bike player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public boolean collisions() {
		boolean collision = player1.collision(player2);
		if (collision) {
			System.out.print("Collision!");
		}
		return collision;
	}
}