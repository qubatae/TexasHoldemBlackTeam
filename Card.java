public class Card {
    Suite suite;
    Value value;
    Card (Suite suite, Value value) {
        this.suite = suite;
        this.value = value;
    }
    enum Suite {
        clubs, diamonds, hearts, spades
    }
    enum Value {
        ace(1),
        two(2),
        three(3),
        four(4),
        five(5),
        six(6),
        seven(7),
        eight(8),
        nine(9),
        ten(10),
        jack(11),
        queen(12),
        king(13);

        private Value(int value) {
            this.value = value;
        }

        int value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + " " + suite;
    }
}

