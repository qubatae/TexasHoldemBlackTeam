import java.util.ArrayList;

public class Player implements Comparable<Player>{
    Card firstCard, secondCard;
    private int money;
    private int currentBet;
    private String name;
    boolean active;
    Hand pHand;
    public Player(int money, String name){
        this.money = money;
        this.name = name;
        currentBet = 0;
        active = true;
    }

    public boolean isActive() {
        return active;
    }
    public int getMoney() {
        return money;
    }
    public int getCurrentBet() {
        return currentBet;
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

    public void blind(int value) {
        assert currentBet == 0;
        money += currentBet;
        currentBet = Math.min(value, money);
        money -= currentBet;
    }
    public boolean raise(int value, final int currentStake) { // player adds value money to the table
        if (value > money + currentBet || value <= currentStake) {
            return false;
        }
        money += currentBet;
        currentBet = value;
        money -= currentBet;
        return true;
    }

    public boolean check(final int currentStake) {
        if (currentStake > money + currentBet) {
            return false;
        }
        money += currentBet;
        currentBet = currentStake;
        money -= currentBet;
        return true;
    }

    public void fold() {
        this.active = false;
    }

    public void allIn() {
        money += currentBet;
        currentBet = money;
        money -= currentBet;
    }

    public void Reward(int value) {
        money += value;
    }

    @Override
    public String toString(){
        return String.format("%s \n Cards: %s, %s \n Balance: %s \n Current bet: %s \n", name, firstCard, secondCard, money, currentBet);
    }

    @Override
    public int compareTo(Player o) {
        if (!active && o.active)
            return -1;
        if (active && !o.active)
            return 1;
        if (active && o.active)
            return pHand.compareTo(o.pHand);
        return 0;
    }
}
