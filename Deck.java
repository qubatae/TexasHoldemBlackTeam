import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList<Card> cards;
    private int index = 0;
    Deck() {
        cards = new ArrayList<>();
        for (Card.Suite suite : Card.Suite.values()) {
            for (Card.Value value : Card.Value.values()) {
                cards.add(new Card(suite, value));
            }
        }
    }
    public void shuffle() {
        Collections.shuffle(cards);
    }
    public Card getCard() throws Exception {
        if (index >= cards.size()) {
            throw new Exception();
        }
        return cards.get(index++);
    }
    public void reset() {
        shuffle();
        index = 0;
    }
    @Override
    public String toString() {
        return cards.toString();
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println(deck);
    }
}
