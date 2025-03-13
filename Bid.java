public class Bid {
    private final User bidder;
    private final double amount;

    public Bid(User bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    public User getBidder() {
        return bidder;
    }

    public double getAmount() {
        return amount;
    }
}

