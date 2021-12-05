import java.util.ArrayList;
import java.util.Random;

public class Table {

    int dealer;
    Deck cards;
    ArrayList<Player> players;
    ArrayList<Card> onTable;
    int moneyOnTable;
    int stake;
    Table (ArrayList<Player> players) {
        this.players = players;
        Random r = new Random();
        dealer = r.nextInt(players.size());
        cards = new Deck();
        cards.reset();
    }
    public void dealCardsToPlayers() throws NoMoreCardsException {
        for (Player player : players) {
            player.AddCard(cards.getCard());
            player.AddCard(cards.getCard());
        }
    }
    public void putCardOnTable() throws NoMoreCardsException {
        onTable.add(cards.getCard());
    }

    public void addMoney(int value) {
        assert value >= 0;
        this.moneyOnTable += value;
    }
}
