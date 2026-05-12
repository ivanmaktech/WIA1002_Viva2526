package viva2;

public class CartAction {
    private final int productId;
    private final int quantityAdded;

    public CartAction(int productId, int quantityAdded) {
        this.productId = productId;
        this.quantityAdded = quantityAdded;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantityAdded() {
        return quantityAdded;
    }

    @Override
    public String toString() {
        return "CartAction{productId=" + productId + ", quantityAdded=" + quantityAdded + "}";
    }
}
