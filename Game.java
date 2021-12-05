import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private void printBlue(String text) {
        System.out.println(TEXT_BLUE + text + TEXT_RESET);
    }
    public static void debug(String text) {
        System.out.println(TEXT_YELLOW + text + TEXT_RESET);
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
    Table table;
    Phase currentPhase;
    int smallBlind;

    Game () {
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

        printGreen("Type smallBlind for this game");
        smallBlind = in.nextInt();
        while (smallBlind < 0) {
            printRed("Value of smallBlind is invalid, type again");
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

    public void printOptions(Player p, final int currentStake) {
        int allMoney = p.getMoney() + p.getCurrentBet();
        if (allMoney > currentStake) {
            printGreen("raise");
        }
        if (allMoney >= currentStake) {
            printGreen("check");
        }
        printGreen("fold");
        printGreen("all-in");
        printGreen("playerinfo");
        printGreen("tableinfo");
        printGreen("Now, type your bet, please");
    }
    public void resolveBet(Player p) {
        boolean successfulBet = false;
        while (!successfulBet) {
            String type = in.next();
            int value;
            int oldBet;
            switch (type) {
                case "options" -> printOptions(p, table.stake);
                case "raise" -> {
                    printGreen("Type value");
                    value = in.nextInt();
                    oldBet = p.getCurrentBet();
                    if (p.raise(value, table.stake)) {
                        table.addMoney(value - oldBet);
                        table.stake = value;
                        successfulBet = true;
                    } else {
                        printRed("invalid raise");
                    }
                }
                case "check" -> {
                    oldBet = p.getCurrentBet();
                    if (p.check(table.stake)) {
                        table.addMoney(table.stake - oldBet);
                        successfulBet = true;
                    } else {
                        printRed("invalid check (maybe you should all-in)");
                    }
                }
                case "fold" -> {
                    p.fold();
                    successfulBet = true;
                }
                case "all-in" -> {
                    oldBet = p.getCurrentBet();
                    int allMoney = p.getMoney() + p.getCurrentBet();
                    p.allIn();
                    table.addMoney(allMoney - oldBet);
                    table.stake = Math.max(table.stake, allMoney);
                    successfulBet = true;
                }
                case "playerinfo" -> {
                    printGreen(p.toString());
                }
                case "tableinfo" -> {
                    printGreen(table.toString());
                }
                default -> printRed("invalid bet");
            }
        }

    }
    public void passControl() {
        printGreen("Type something to end your move and pass control to another player");
        String sth = in.next();
    }

    private int noPlayersMatchingStake;
    public void casualBet(Player p) {
        int oldStake = table.stake;
        if (p.active) {
            printGreen("Type your bet, or type 'options' to see your betting options");
            resolveBet(p);
        } else {
            printBlue(p.getName() + "is inactive");
        }
        if (oldStake == table.stake) {
            noPlayersMatchingStake++;
        } else {
            noPlayersMatchingStake = 1;
        }
    }



    public void betting() {
        while (true) {
            for (int i = table.dealer + 1; i <= table.players.size() + table.dealer; i++) {
                Player p = table.players.get(i);
                casualBet(p);
                debug("noPlayersMatchingStake " + noPlayersMatchingStake);
                debug("players.size() " + table.players.size());
                passControl();
                if (noPlayersMatchingStake == table.players.size()) {
                    noPlayersMatchingStake = 0;
                    return;
                }
            }
        }
    }

    public void playRound() {
        try {
            currentPhase = Phase.preFlop;
            printGreen("preFlop");
            table.dealCardsToPlayers();

            for (int i = table.dealer + 1; i <= table.players.size() + table.dealer; i++) {
                Player p = table.players.get(i);
                if (i == table.dealer + 1) {
                    printGreen("Hi " + p.getName() + ", you are SmallBlind");
                    int bet = p.blind(smallBlind);
                    table.stake = smallBlind;
                    table.addMoney(bet);
                    passControl();
                } else if (i == table.dealer + 2) {
                    printGreen("Hi " + p.getName() + " you are BigBlind");
                    int bet = p.blind(2*smallBlind);
                    table.stake = 2*smallBlind;
                    table.addMoney(bet);
                    passControl();
                } else {
                    casualBet(p);
                    passControl();
                }
            }
            betting();

            currentPhase = Phase.flop;
            printGreen("flop");
            for (int cardNo = 0; cardNo < 3; cardNo++) {
                table.putCardOnTable();
            }
            betting();

            printGreen("turn");
            currentPhase = Phase.turn;
            table.putCardOnTable();
            betting();

            printGreen("river");
            currentPhase = Phase.river;
            table.putCardOnTable();
            betting();

            currentPhase = Phase.showDown;
            printGreen("showDown");

            for (Player p : table.players) {
                ArrayList<Card> cards = new ArrayList<>(Arrays.asList(p.GetCards()));
                cards.addAll(table.onTable);
                p.pHand = Hand.FindHand(cards);
            }

            Player bestPlayer = Collections.max(table.players);


            for (Player p : table.players) {
                printGreen(p.getName());
                if (p.isActive())
                    printGreen("their cards: " + p.firstCard.toString() + " " + p.secondCard.toString());
                else
                    printGreen("is not active");
            }
            printGreen("cards on table are " + table.onTable.toString());
            printGreen(bestPlayer.getName() + "won and gets " + table.moneyOnTable);

            bestPlayer.Reward(table.moneyOnTable);

            table.moneyOnTable = 0;
            table.stake = 0;

            for (Player p : table.players) {
                if(p.getMoney() > 0)
                    p.active = true;
            }
            for (Player p : table.players) {
                p.clearBet();
                p.clearCards();
            }
            table.dealer = (table.dealer + 1) % table.players.size();
            table.cards.reset();
            table.reset();

        } catch (NoMoreCardsException ee) {
            printRed("no more cards ?!");
        }

    }

    public boolean endOfGame() {
        int playersLeft = table.players.size();
        for (Player p : table.players) {
            int allMoney = p.getMoney() + p.getCurrentBet();
            if (allMoney == 0) {
                playersLeft--;
            }
        }
        assert playersLeft >= 0;
        return playersLeft == 1;
    }
    public void playOut() {
        currentPhase = Phase.register;
        registerPlayers();
        while (true) {
            printGreen("NEW ROUND");
            playRound();
            if(endOfGame()) {
                break;
            }
        }
        printGreen("Game finished");
    }
    public static void main(String[] args) {
        Game game = new Game();
        game.playOut();
    }
}
