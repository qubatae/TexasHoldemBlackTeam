import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    enum Phase {
        register,
        preFlop,
        flop,
        turn,
        river,
        showDown;
    }

    Scanner in;
    Integer noPlayers;
    Game () {
        this.in = new Scanner(System.in);
    }
    int round_no = 0;

    public ArrayList<Player> readPlayers() {
        System.out.println("Type number of players: this should be <= 10");
        noPlayers = in.nextInt();
        while (noPlayers > 10 || noPlayers < 0) {
            System.out.println("Value of players is incorrect");
            noPlayers = in.nextInt();
        }
        String playerName = in.next();
        ArrayList<Player> players = new ArrayList<>();
        return  players;
    }
    public void registerPlayers (ArrayList<Player> players) throws Exception {

    }
    public static void main(String[] args) {
        Game game = new Game();
        game.readPlayers();

    }
}
