import java.util.ArrayList;
import java.util.List;

public class Auction {
    private final Item item;
    private final List<Bid> bids;

    public Auction(Item item) {
        this.item = item;
        this.bids = new ArrayList<>();
    }

    public void placeBid(Bid bid) {
        bids.add(bid);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public Item getItem() {
        return item;
    }

    
    public Bid determineWinner() {
        if (bids.isEmpty()) {
            return null;
        }

        Bid highestBid = bids.get(0);
        for (Bid bid : bids) {
            if (bid.getAmount() > highestBid.getAmount()) {
                highestBid = bid;
            }
        }
        return highestBid;
    }
}

