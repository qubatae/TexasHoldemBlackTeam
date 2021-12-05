import java.util.ArrayList;
import java.util.Collections;

public class Hand implements Comparable<Hand> {
    @Override
    public int compareTo(Hand o) {
        if(handType.GetValue() > o.GetType().GetValue())
            return 1;
        if(handType.GetValue() < o.GetType().GetValue())
            return -1;
        if(maxValue1.value > o.GetMaxValue1().value)
            return 1;
        if(maxValue1.value < o.GetMaxValue1().value)
            return -1;
        if(maxValue2.value > o.GetMaxValue2().value)
            return 1;
        if(maxValue2.value < o.GetMaxValue2().value)
            return -1;
        if(maxValue.value > o.GetMaxValue().value)
            return 1;
        if(maxValue.value < o.GetMaxValue().value)
            return -1;
        return 0;
    }

    public HandType GetType(){
        return handType;
    }
    public Card.Value GetMaxValue(){
        return maxValue;
    }
    public Card.Value GetMaxValue1() {return maxValue1;}
    public Card.Value GetMaxValue2() {return maxValue2;}

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

        HandType(int handValue) {
            HandValue = handValue;
        }

        public int GetValue() {
            return HandValue;
        }
    }

    private HandType handType;
    private Card.Value maxValue;
    private Card.Value maxValue1;
    private Card.Value maxValue2;

    public Hand(HandType type, Card.Value value) {
        handType = type;
        maxValue = value;
    }

    private static boolean is_pair(ArrayList<Card> cards, Hand x) {
        x.maxValue = cards.get(0).getValue();
        boolean find_pair = false;
        for(int i = 0; i < 5; i++){
            for(int j = i + 1; j < 5; j++) {
                if(cards.get(i).getValue() == cards.get(j).getValue()) {
                    x.handType = HandType.PAIR;
                    x.maxValue1 = x.maxValue2 = cards.get(i).getValue();
                    find_pair = true;
                }
            }
            x.maxValue = cards.get(i).getValue();
        }
        return find_pair;
    }

    private static boolean is_two_pair(ArrayList<Card> cards, Hand x) {
        x.maxValue = cards.get(0).getValue();
        boolean find_two_pair = false;
        for(int i = 0; i < 5; i++){
            for(int j = i + 1; j < 5; j++) if(cards.get(i).getValue() == cards.get(j).getValue())
                for(int i1 = j + 1; i1 < 5; i1++)
                    for(int j1 = i1 + 1; j1 < 5; j1++) if(cards.get(i1).getValue() == cards.get(j1).getValue()){
                        if (!find_two_pair) {
                            x.handType = HandType.TWO_PAIRS;
                            x.maxValue1 = cards.get(i1).getValue();
                            x.maxValue2 = cards.get(i).getValue();
                        }
                        find_two_pair = true;
                    }
            x.maxValue = cards.get(i).getValue();
        }
        return find_two_pair;
    }

    private static boolean is_triple(ArrayList<Card> cards, Hand x) {
        boolean find_triple = false;
        for(int i = 0; i < 5; i++){
            for(int j = i + 1; j < 5; j++){
                for(int k = j + 1; k < 5; k++){
                    if(cards.get(i).getValue() == cards.get(j).getValue()
                            && cards.get(j).getValue() == cards.get(k).getValue()) {
                        x.handType = HandType.TRIPLE;
                        x.maxValue1 = x.maxValue2 = cards.get(i).getValue();
                        find_triple = true;
                    }
                }
            }
            x.maxValue = cards.get(i).getValue();
        }
        return find_triple;
    }

    private static boolean is_straight(ArrayList<Card> cards, Hand x){
        Card.Value prevValue = cards.get(0).getValue();
        if(cards.get(4).getValue() == Card.Value.ace && cards.get(0).getValue() == Card.Value.two){
            if(cards.get(3).value == Card.Value.ace) return false;
            for(int i = 1; i < 4; i++){
                if(cards.get(i).getValue().value == prevValue.value + 1){
                    prevValue = cards.get(i).getValue();
                }
                else return false;
            }
        }
        else {
            for (int i = 1; i < 5; i++) {
                if (cards.get(i).getValue().value == prevValue.value + 1) {
                    prevValue = cards.get(i).getValue();
                }
                else return false;
            }
        }
        x.maxValue = x.maxValue1 = x.maxValue2 = prevValue;
        x.handType = HandType.STRAIGHT;
        return true;
    }

    private static boolean is_flush(ArrayList<Card> cards, Hand x){
        if(cards.get(0).suite == cards.get(1).suite
                && cards.get(0).suite == cards.get(2).suite
                && cards.get(0).suite == cards.get(3).suite
                && cards.get(0).suite == cards.get(4).suite){
            x.maxValue = x.maxValue1 = x.maxValue2 = cards.get(4).getValue();
            x.handType = HandType.FLUSH;
            return true;
        }
        return false;
    }

    private static boolean is_full_house(ArrayList<Card> cards, Hand x) {
        if(cards.get(0).getValue() == cards.get(1).getValue() && cards.get(2).getValue() == cards.get(4).getValue()){
            x.maxValue1 = cards.get(2).getValue();
            x.maxValue2 = cards.get(0).getValue();
            x.maxValue = cards.get(4).getValue();
            x.handType = HandType.FULL_HOUSE;
            return true;
        }
        if(cards.get(0).getValue() == cards.get(2).getValue() && cards.get(3).getValue() == cards.get(4).getValue()){
            x.maxValue1 = cards.get(0).getValue();
            x.maxValue2 = cards.get(3).getValue();
            x.maxValue = cards.get(4).getValue();
            x.handType = HandType.FULL_HOUSE;
            return true;
        }
        return false;
    }

    private static boolean is_four(ArrayList<Card> cards, Hand x) {
        if(cards.get(0).getValue() == cards.get(3).getValue()){
            x.maxValue1 = x.maxValue2 = cards.get(0).getValue();
            x.maxValue = cards.get(4).getValue();
            x.handType = HandType.FOUR;
            return true;
        }
        if(cards.get(1).getValue() == cards.get(4).getValue()){
            x.maxValue1 = x.maxValue2 = cards.get(1).getValue();
            x.maxValue = cards.get(4).getValue();
            x.handType = HandType.FOUR;
            return true;
        }
        return false;
    }

    private static boolean is_straight_flush(ArrayList<Card> cards, Hand x){
        if (is_straight(cards, x) && is_flush(cards, x)){
            x.maxValue1 = x.maxValue2 = cards.get(4).getValue();
            x.maxValue = cards.get(4).getValue();
            x.handType = HandType.STRAIGHT_FLUSH;
            return true;
        }
        return false;
    }

    private static boolean is_royal_flush(ArrayList<Card> cards, Hand x) {
        if(cards.get(0).getValue() == Card.Value.ten && cards.get(4).getValue() == Card.Value.ace
                && cards.get(0).suite == cards.get(1).suite
                && cards.get(0).suite == cards.get(2).suite
                && cards.get(0).suite == cards.get(3).suite
                && cards.get(0).suite == cards.get(4).suite
        ) {
            x.maxValue = x.maxValue1 = x.maxValue2 = cards.get(4).getValue();
            x.handType = HandType.FLUSH_ROYAL;
            return true;
        }
        return false;
    }

    public static Hand FindHand(ArrayList<Card> cards) {
        Hand currentHand = new Hand(HandType.HIGHCARD, cards.get(0).getValue());

        for(int i = 0; i < 7; i++)
            for(int j = i + 1; j < 7; j++) if(cards.get(i).compareTo(cards.get(j)) > 0) Collections.swap(cards, i, j);

        for(int i = 0; i < 128; i++) if(Integer.bitCount(i) == 5) {

            ArrayList<Card> fives = new ArrayList<Card>();
            Hand tryHand = new Hand(HandType.HIGHCARD, cards.get(0).getValue());

            for(int j = 0, s = 1; j < 7; j++, s *= 2) if((s & i) > 0) fives.add(cards.get(j));

            //Pair
            if (is_pair(fives, tryHand)) {
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }

            //Two_Pair
            if (is_two_pair(fives, tryHand)) {
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }

            //Triple
            if(is_triple(fives, tryHand)){
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }

            //Straight
            if(is_straight(fives, tryHand)){
                if(tryHand.compareTo(currentHand) > 0){
                    currentHand = tryHand;
                }
            }

            //Flush
            if(is_flush(fives, tryHand)){
                if(tryHand.compareTo(currentHand) > 0){
                    currentHand = tryHand;
                }
            }

            //Full_House
            if (is_full_house(fives, tryHand)) {
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }

            //Four
            if (is_four(fives, tryHand)) {
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }

            if(is_straight_flush(fives, tryHand)){
                if(tryHand.compareTo(currentHand) > 0){
                    currentHand = tryHand;
                }
            }

            //Royal
            if (is_royal_flush(fives, tryHand)) {
                if (tryHand.compareTo(currentHand) > 0) {
                    currentHand = tryHand;
                }
            }
        }

        return currentHand;
    }
}
