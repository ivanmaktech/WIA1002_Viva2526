package viva2;

public class UndoAction {
    int productId;
    int quantityAdded;
    UndoAction next;

    public UndoAction(int productId, int quantityAdded) {
        this.productId = productId;
        this.quantityAdded = quantityAdded;
        this.next = null;
    }
}