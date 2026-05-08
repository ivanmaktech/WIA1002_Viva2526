package viva2;

public class CartList {
    private CartNode head;
    private int size;
    private UndoAction undoStackHead; // Stack for undo functionality

    public CartList() {
        head = null;
        size = 0;
        undoStackHead = null;
    }

    public void addItem(Product p, int qty) {
        CartNode existing = findItem(p.getId());
        if (existing != null) {
            existing.quantity += qty;
        } else {
            CartNode newNode = new CartNode(p, qty);
            if (head == null) {
                head = newNode;
            } else {
                CartNode current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }
        
        // Push action to undo stack
        pushUndo(p.getId(), qty);
    }

    // Stack Push
    private void pushUndo(int productId, int quantity) {
        UndoAction action = new UndoAction(productId, quantity);
        action.next = undoStackHead;
        undoStackHead = action;
    }

    // Stack Pop
    public int[] popUndo() {
        if (undoStackHead == null) return null;
        int pId = undoStackHead.productId;
        int qtyToUndo = undoStackHead.quantityAdded;
        undoStackHead = undoStackHead.next;
        return new int[]{pId, qtyToUndo};
    }

    public void removeItem(int productId) {
        if (head == null) return;

        if (head.product.getId() == productId) {
            head = head.next;
            size--;
            return;
        }

        CartNode current = head;
        while (current.next != null && current.next.product.getId() != productId) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
        }
    }

    public void updateQuantity(int productId, int newQty) {
        CartNode node = findItem(productId);
        if (node != null) {
            if (newQty <= 0) {
                removeItem(productId);
            } else {
                node.quantity = newQty;
            }
        }
    }

    public CartNode findItem(int productId) {
        CartNode current = head;
        while (current != null) {
            if (current.product.getId() == productId) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public void displayCart() {
        if (isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-15s | %-10s | %-12s | %-10s\n", "Name", "Quantity", "Unit Price", "Subtotal");
        System.out.println("----------------------------------------------------------------------");
        CartNode current = head;
        while (current != null) {
            double subtotal = current.quantity * current.product.getPrice();
            System.out.printf("%-15s | %-10d | RM%-10.2f | RM%-8.2f\n", 
                current.product.getName(), current.quantity, current.product.getPrice(), subtotal);
            current = current.next;
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("TOTAL BILL: RM%.2f\n", calculateTotal());
    }

    public double calculateTotal() {
        double total = 0.0;
        CartNode current = head;
        while (current != null) {
            total += (current.quantity * current.product.getPrice());
            current = current.next;
        }
        return total;
    }

    public void clear() {
        head = null;
        size = 0;
        undoStackHead = null; // Clears the undo stack as per Task 3 requirements
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return head == null; }
    public CartNode getHead() { return head; }
}