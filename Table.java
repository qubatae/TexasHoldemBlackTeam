package sample;

import java.util.ArrayList;
import java.util.Random;

public class Table {

    int dealer;
    int round = 0;
    Deck cards;
    ArrayList<Player> players;
    ArrayList<Card> onTable;
    Table (ArrayList<Player> players) {
        this.players = players;
        Random r = new Random();
        dealer = r.nextInt(players.size());
        cards = new Deck();
        cards.reset();
    }
    public void dealCardsToPlayers() throws Exception {
        for (Player player : players) {
            player.AddCard(cards.getCard());
            player.AddCard(cards.getCard());
        }
    }
    public void putCardOnTable() throws Exception{
        onTable.add(cards.getCard());
    }



}