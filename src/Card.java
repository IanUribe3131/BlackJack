public class Card {
    enum Suit {HEARTS, DIAMONDS, CLUBS, SPADES}
    enum Rank{
        TWO(2), TRHEE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), 
        NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);
        final int value;
        Rank(int value){this.value=value;}
    }
    public final Suit suit;
    public final Rank rank;

    public Card(Suit suit, Rank rank){
        this.suit=suit;
        this.rank=rank;
    }

    public int getValue(){
        return rank.value;
    }
    public Rank getRank(){
        return rank;
    }
    public Suit getSuit(){
        return suit;
    }
    public String toString(){
        return rank + " of " + suit;
    }
}
