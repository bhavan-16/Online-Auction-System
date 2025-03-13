public class Item {
    private final String name;
    private final String role;
    private final double basePrice;

    public Item(String name, String role, double basePrice) {
        this.name = name;
        this.role = role;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getBasePrice() {
        return basePrice;
    }
}


