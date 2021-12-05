import java.util.ArrayList;

public class Player {
    Card firstCard, secondCard;
    int money;
    private int currentBet;
    private String name;

    public Player(int money, String name){
        this.money = money;
        this.name = name;
        currentBet = 0;
    }

    public Card[] GetCards(){
        return new Card[]{ firstCard, secondCard };
    }

    public void AddCard(Card c){
        if(firstCard == null){
            firstCard = c;
        }else{
            secondCard = c;
        }
    }

    public String GetName(){
        return name;
    }

    public void clearCards(){
        firstCard = secondCard = null;
    }

    public boolean SetBet(int x)
    {
        if(x > money){
            return false;
        }
        currentBet = x;
        return true;
    }

    public int Bet(){
        money -= currentBet;
        int x = currentBet;
        currentBet = 0;
        return x;
    }

    public boolean blind(int value) { //forced bet: smallblind or bigblind

        money
    }
    public boolean raise(int value) { // check if value > 0 and player has enough money
        if (value <= currentBet || value > money)
            return false;
    }
    public boolean check(int value) {
        ...
    }
    public boolean fold() {
        ...
    }
    public boolean allIn() {
        ...
    }

    public void Reward(int value) {
        money += value;
    }

    @Override
    public String toString(){
        return String.format("%s \n Cards: %s, %s \n Balance: %s \n Current bet: %s \n", name, firstCard, secondCard, money, currentBet);
    }
}
