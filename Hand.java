package sample;

import java.util.ArrayList;

public class Hand implements Comparable<Hand> {
    @Override
    public int compareTo(Hand o)
    {
        if(handType.GetValue() > o.GetType().GetValue())
            return 1;
        if(handType.GetValue() < o.GetType().GetValue())
            return -1;
        if(maxValue.GetValue() > o.GetValue().GetValue())
            return 1;
        if(maxValue.GetValue() < o.GetValue().GetValue())
            return -1;
        return 0;
    }

    public HandType GetType(){
        return handType;
    }

    public Value GetValue(){
        return maxValue;
    }

    public enum  HandType{
        HIGHCARD(0),
        PAIR(1),
        TWO_PAIRS(2),
        TRIPLE(3),
        STRAIGHT(4),
        FLUSH(5),
        FULL_HOUSE(6),
        FOUR(7),
        STRAIGHT_FLUSH(8),
        FLUSH_ROYAL(9);


        private int HandValue;

        HandType(int handValue)
        {
            HandValue = handValue;
        }

        public int GetValue()
        {
            return HandValue;
        }
    }
    private HandType handType;
    private Value maxValue;

    public Hand(HandType type, Value value)
    {
        handType = type;
        maxValue = value;
    }

    public static Hand FindHand(ArrayList<Card> cards)
    {
        Hand currentHand = new Hand(HandType.HIGHCARD, cards.get(0).GetValue());

    }
}
