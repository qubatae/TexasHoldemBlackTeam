import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";

    public static void printRed(String text) {
        System.out.println(TEXT_RED + text + TEXT_RESET);
    }
    public static void printGreen(String text) {
        System.out.println(TEXT_GREEN + text + TEXT_RESET);
    }

    enum Phase {
        register,
        preFlop,
        flop,
        turn,
        river,
        showDown;
    }

    Scanner in;

//    CircularList<Player> players;
    int round;
    Table table;
    Phase currentPhase;
    int smallBlind;

    Game () {
        round = 1;
        this.in = new Scanner(System.in);
    }

    public class CircularList<E> extends ArrayList<E> {
        @Override
        public E get(int index) {
            return super.get(index % size());
        }
    }

    public CircularList<Player> readPlayers() {
        printGreen("Type number of players: this should be <= 10");
        int noPlayers = in.nextInt();
        while (noPlayers > 10 || noPlayers < 0) {
            printRed("Value of players is incorrect, type again");
            noPlayers = in.nextInt();
        }
        printGreen("Type amount of cash each player starts game with");
        int val;
        while (true) {
            val = in.nextInt();
            if (val <= 0) {
                printRed("cash must greater than 0");
            } else {
                break;
            }
        }
        final int initialMoney = val;

        printGreen("Type SMALLBLIND for this game");
        smallBlind = in.nextInt();
        while (smallBlind < 0) {
            printRed("Value of SMALLBLIND is incorrect, type again");
            smallBlind = in.nextInt();
        }

        CircularList<Player> players = new CircularList<>();
        for (int i = 0; i < noPlayers; i++) {
            printGreen("Type name of player " + i);
            String playerName = in.next();
            players.add(new Player(initialMoney, playerName));
        }
        return players;
    }
    public void registerPlayers () {
        this.table = new Table(readPlayers());
    }

    public void printOptions() {
        ...
    }

    public void resolveBet(Player p) {
        String type = in.next();
        int value;
        switch (type) {
            case "options":
                printOptions();
                break;
            case "raise":
                printGreen("Type value");
                value = in.nextInt();
                p.raise(value);
                break;
            case "check":
                p.check();
                break;
            case "fold":
                p.fold();
                break;
            case "all-in":
                p.allIn();
                break;
            default:
                printRed("invalid bet");
        }
    }
    public void passControl() {
        printGreen("Type something to end your move and pass control to another player");
        String sth = in.next();
    }

    public void playRound() {
        try {
            currentPhase = Phase.preFlop;
            table.dealCardsToPlayers();
            for (int i = table.dealer + 1; i <= table.players.size() + table.dealer; i++) {

                Player p = table.players.get(i);
                if (i == table.dealer + 1) {
                    printGreen("You are Smallblind");
                    p.blind(smallBlind);
                    passControl();
                } else if (i == table.dealer) {
                    printGreen("You are BigBlind");
                    p.blind(2*smallBlind);
                    passControl();
                } else {
                    printGreen("Type your bet, or type 'options' to see your betting options");
                    resolveBet(p);
                }
            }
        } catch (NoMoreCardsException ee) {
            printRed("no more cards ?!");
        }

    }

    public void playOut() {
        currentPhase = Phase.register;
        registerPlayers();
        while (true) { // TODO change so that break when one player wins
            printGreen("ROUND" + round++);
            playRound();
        }
    }
    public static void main(String[] args) {
        Game game = new Game();
        game.playOut();
    }
}
