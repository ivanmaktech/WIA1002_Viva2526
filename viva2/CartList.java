package viva2;

public class CartList {
    private CartNode head;
    private CartNode tail;
    private int size;
    private LinkedListStack<CartAction> undoStack;

    public CartList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.undoStack = null;
    }

    public void setUndoStack(LinkedListStack<CartAction> undoStack) {
        this.undoStack = undoStack;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public CartNode findItem(int productId) {
        CartNode current = head;
        while (current != null) {
            if (current.product != null && current.product.getId() == productId) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public boolean addItem(Product product, int qty) {
        if (product == null || qty <= 0) {
            return false;
        }
        if (product.getStock() < qty) {
            return false;
        }

        CartNode existing = findItem(product.getId());
        if (existing != null) {
            existing.quantity += qty;
        } else {
            CartNode node = new CartNode(product, qty);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            size++;
        }

        product.setStock(product.getStock() - qty);
        return true;
    }

    //Removes item completely and restores stock. Returns true if item was found and removed.
    public boolean removeItem(int productId) {
        CartNode current = head;
        CartNode prev = null;
        while (current != null) {
            if (current.product != null && current.product.getId() == productId) {
                //Restore stock
                current.product.setStock(current.product.getStock() + current.quantity);

                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                if (current == tail) {
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public boolean updateQuantity(int productId, int newQty) {
        CartNode node = findItem(productId);
        if (node == null) {
            return false;
        }
        if (newQty < 0) {
            return false;
        }

        int oldQty = node.quantity;
        int delta = newQty - oldQty;
        Product product = node.product;
        if (product == null) {
            return false;
        }

        if (delta > 0) {
            //need more stock
            if (product.getStock() < delta) {
                return false;
            }
            product.setStock(product.getStock() - delta);
        } else if (delta < 0) {
            //return stock
            product.setStock(product.getStock() + (-delta));
        }

        if (newQty == 0) {
            //remove node from list
            removeNodeWithoutStockChange(productId);
        } else {
            node.quantity = newQty;
        }
        return true;
    }

    //Clears entire cart and restores stock for all items
    public void clear() {
        CartNode current = head;
        while (current != null) {
            if (current.product != null) {
                current.product.setStock(current.product.getStock() + current.quantity);
            }
            current = current.next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    public void clearWithoutRestoringStock() {
        head = null;
        tail = null;
        size = 0;
    }

    public void displayCart() {
        if (isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n=== Shopping Cart ===");
        System.out.printf("%-8s %-25s %-8s %-10s %-10s%n", "ID", "Name", "Qty", "Unit", "Subtotal");
        System.out.println("---------------------------------------------------------------");
        CartNode current = head;
        while (current != null) {
            Product p = current.product;
            double subtotal = p.getPrice() * current.quantity;
            System.out.printf("%-8d %-25s %-8d %-10.2f %-10.2f%n",
                    p.getId(), p.getName(), current.quantity, p.getPrice(), subtotal);
            current = current.next;
        }
        System.out.println("---------------------------------------------------------------");
        System.out.printf("Total: %.2f%n", calculateTotal());
    }

    public double calculateTotal() {
        double total = 0.0;
        CartNode current = head;
        while (current != null) {
            if (current.product != null) {
                total += current.product.getPrice() * current.quantity;
            }
            current = current.next;
        }
        return total;
    }

    //Undo a previous add-to-cart action
    public boolean undoAddAction(CartAction action) {
        if (action == null) {
            return false;
        }
        CartNode node = findItem(action.getProductId());
        if (node == null || node.product == null) {
            return false;
        }
        int qtyToRemove = action.getQuantityAdded();
        if (qtyToRemove <= 0) {
            return false;
        }

        if (qtyToRemove >= node.quantity) {
            //restore all and remove node
            node.product.setStock(node.product.getStock() + node.quantity);
            removeNodeWithoutStockChange(action.getProductId());
            return true;
        }

        node.quantity -= qtyToRemove;
        node.product.setStock(node.product.getStock() + qtyToRemove);
        return true;
    }

    //CartList.undo() uses the configured undo stack.
    public boolean undo() {
        if (undoStack == null) {
            return false;
        }
        CartAction action = undoStack.pop();
        return undoAddAction(action);
    }

    //Internal removal helper that does NOT change stock (caller already handled stock adjustments)
    private boolean removeNodeWithoutStockChange(int productId) {
        CartNode current = head;
        CartNode prev = null;
        while (current != null) {
            if (current.product != null && current.product.getId() == productId) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                if (current == tail) {
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }
}