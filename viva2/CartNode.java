package viva2;

package grocerystoresystem;

// Node for the Shopping Cart Singly Linked List
public class CartNode {
    Product product;
    int quantity;
    CartNode next;

    public CartNode(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.next = null;
    }
}

// Node for the Undo Stack
class UndoAction {
    int productId;
    int quantityAdded;
    UndoAction next;

    public UndoAction(int productId, int quantityAdded) {
        this.productId = productId;
        this.quantityAdded = quantityAdded;
        this.next = null;
    }
}