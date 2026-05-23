import java.util.ArrayList;

public class Hand {
    private final ArrayList<Card> cards = new ArrayList<>();

    public void addCards(Card card){
        cards.add(card);
    }

    public ArrayList<Card> getCards(){
        return cards;
    }
    
    public void clear(){
        cards.clear();
    }

    public int calculateValue(){
        int total =0, aces = 0;
        for(Card card : cards){
            total += card.getRank().value;
            if(card.rank == Card.Rank.ACE){
                aces++;
            }
        }
        while(total > 21 && aces > 0 ){
                total -= 10;
                aces--;
        }
        return total;
    }
}
