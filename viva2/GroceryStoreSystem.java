package viva2;

import java.util.Scanner;

public class GroceryStoreSystem {
    private static final String FILENAME = "inventory.txt";
    private static InventoryManager inventory = new InventoryManager();
    private static CartList cart = new CartList();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Grocery Store Management System!");
        inventory.loadFromFile(FILENAME);

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = -1;
            try {
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: inventory.displayAll(); break;
                case 2: searchProductById(); break;
                case 3: searchProductByName(); break;
                case 4: addProductToInventory(); break;
                case 5: removeProductFromInventory(); break;
                case 6: updateInventoryStock(); break;
                case 7: addItemToCart(); break;
                case 8: viewCart(); break;
                case 9: removeItemFromCart(); break;
                case 10: updateItemQuantityInCart(); break;
                case 11: undoLastCartAddition(); break;
                case 12: generateBillAndCheckout(); break;
                case 13: clearCart(); break;
                case 14:
                    System.out.print("Do you want to save inventory changes before exiting? (y/n): ");
                    String save = scanner.nextLine().trim().toLowerCase();
                    if (save.equals("y") || save.equals("yes")) {
                        inventory.saveToFile(FILENAME);
                    }
                    System.out.println("Thank you for using the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option from 1 to 14.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=========================================");
        System.out.println("            MAIN MENU                    ");
        System.out.println("=========================================");
        System.out.println("[Inventory Operations]");
        System.out.println("1. Display all products");
        System.out.println("2. Search product by ID");
        System.out.println("3. Search product by Name");
        System.out.println("4. Add new product");
        System.out.println("5. Remove product");
        System.out.println("6. Update product stock");
        System.out.println("\n[Cart Operations]");
        System.out.println("7. Add item to cart");
        System.out.println("8. View cart");
        System.out.println("9. Remove item from cart");
        System.out.println("10. Update quantity in cart");
        System.out.println("11. Undo last cart addition");
        System.out.println("12. Generate Bill / Checkout");
        System.out.println("13. Clear Cart");
        System.out.println("\n14. Save and Exit");
        System.out.println("=========================================");
    }

    private static void searchProductById() {
        System.out.print("Enter Product ID to search: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Product p = inventory.searchById(id);
            if (p != null) System.out.println(p);
            else System.out.println("Product not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void searchProductByName() {
        System.out.print("Enter Product Name to search: ");
        String name = scanner.nextLine().trim();
        ArrayList<Product> results = inventory.searchByName(name);
        if (results.isEmpty()) {
            System.out.println("No matching products found.");
        } else {
            for (Product p : results) System.out.println(p);
        }
    }

    private static void addProductToInventory() {
        try {
            System.out.print("Enter ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter Initial Stock: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());
            
            inventory.addProduct(new Product(id, name, price, stock));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format entered. Product creation failed.");
        }
    }

    private static void removeProductFromInventory() {
        System.out.print("Enter Product ID to remove: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            inventory.removeProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void updateInventoryStock() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter New Stock Quantity: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());
            inventory.updateStock(id, stock);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    private static void addItemToCart() {
        try {
            System.out.print("Enter Product ID to add: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            if (qty <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

            if (inventory.isAvailable(id, qty)) {
                Product p = inventory.getProductById(id);
                cart.addItem(p, qty);
                p.setStock(p.getStock() - qty); // Temporarily reduce stock
                System.out.println("Item added to cart successfully.");
            } else {
                System.out.println("Error: Insufficient stock or product not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    private static void viewCart() {
        cart.displayCart();
    }

    private static void removeItemFromCart() {
        System.out.print("Enter Product ID to remove from cart: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            CartNode node = cart.findItem(id);
            if (node != null) {
                // Restore stock
                Product p = inventory.getProductById(id);
                if (p != null) p.setStock(p.getStock() + node.quantity);
                
                cart.removeItem(id);
                System.out.println("Item removed from cart. Stock restored.");
            } else {
                System.out.println("Item not found in cart.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void updateItemQuantityInCart() {
        try {
            System.out.print("Enter Product ID in cart: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            CartNode node = cart.findItem(id);
            
            if (node == null) {
                System.out.println("Item not found in cart.");
                return;
            }

            System.out.print("Enter new quantity: ");
            int newQty = Integer.parseInt(scanner.nextLine().trim());

            int diff = newQty - node.quantity;
            Product p = inventory.getProductById(id);

            if (diff > 0 && !inventory.isAvailable(id, diff)) {
                System.out.println("Insufficient stock to increase quantity.");
            } else {
                p.setStock(p.getStock() - diff); 
                cart.updateQuantity(id, newQty);
                System.out.println("Cart quantity updated.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
    }

    private static void undoLastCartAddition() {
        int[] undoneData = cart.popUndo();
        if (undoneData == null) {
            System.out.println("No recent additions to undo.");
        } else {
            int pId = undoneData[0];
            int qtyRestored = undoneData[1];
            
            CartNode node = cart.findItem(pId);
            if (node != null) {
                node.quantity -= qtyRestored;
                if (node.quantity <= 0) {
                    cart.removeItem(pId);
                }
            }

            Product p = inventory.getProductById(pId);
            if (p != null) {
                p.setStock(p.getStock() + qtyRestored);
            }
            System.out.println("Undo successful. Restored " + qtyRestored + " unit(s) of Product ID " + pId + " back to inventory.");
        }
    }

    private static void clearCart() {
        CartNode current = cart.getHead();
        while (current != null) {
            Product p = inventory.getProductById(current.product.getId());
            if (p != null) {
                p.setStock(p.getStock() + current.quantity); // restore stock
            }
            current = current.next;
        }
        cart.clear();
        System.out.println("Cart cleared. All stocks restored to inventory.");
    }

    private static void generateBillAndCheckout() {
        if (cart.isEmpty()) {
            System.out.println("Cannot checkout. Cart is empty.");
            return;
        }
        System.out.println("\n--- RECEIPT ---");
        cart.displayCart();
        System.out.println("Thank you for your purchase!");
        
        cart.clear();
        inventory.saveToFile(FILENAME);
    }
}