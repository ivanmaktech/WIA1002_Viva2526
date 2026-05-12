package viva2;

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