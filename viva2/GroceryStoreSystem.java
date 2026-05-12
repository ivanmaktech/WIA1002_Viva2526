package viva2;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GroceryStoreSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();
        CartList cart = new CartList();
        LinkedListStack<CartAction> undoStack = new LinkedListStack<>();
        cart.setUndoStack(undoStack);

        String inventoryFile = resolveInventoryFile();
        inventory.loadFromFile(inventoryFile);
        System.out.println("Inventory loaded from: " + inventoryFile);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    if (!cart.isEmpty()) {
                        System.out.println("Cannot reload inventory while cart is not empty. Clear the cart first.");
                        break;
                    }
                    inventory.loadFromFile(inventoryFile);
                    System.out.println("Inventory reloaded from: " + inventoryFile);
                    break;
                case 2:
                    inventory.displayAll();
                    break;
                case 3:
                    handleSearchById(inventory);
                    break;
                case 4:
                    handleSearchByName(inventory);
                    break;
                case 5:
                    handleAddProduct(inventory);
                    break;
                case 6:
                    handleRemoveProduct(inventory, cart, undoStack);
                    break;
                case 7:
                    handleUpdateStock(inventory);
                    break;
                case 8:
                    inventory.saveToFile(inventoryFile);
                    System.out.println("Inventory saved to: " + inventoryFile);
                    break;
                case 9:
                    handleAddItemToCart(inventory, cart, undoStack);
                    break;
                case 10:
                    cart.displayCart();
                    break;
                case 11:
                    handleRemoveItemFromCart(cart, undoStack);
                    break;
                case 12:
                    handleUpdateCartQuantity(cart, undoStack);
                    break;
                case 13:
                    cart.clear();
                    undoStack.clear();
                    System.out.println("Cart cleared and stock restored.");
                    break;
                case 14:
                    // Undo last cart addition
                    if (cart.undo()) {
                        System.out.println("Undo successful.");
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                    break;
                case 15:
                    handleCheckout(cart, undoStack, inventory, inventoryFile);
                    break;
                case 16:
                    inventory.saveToFile(inventoryFile);
                    System.out.println("Inventory saved. Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== Grocery Store Management System =====");
        System.out.println("1  - Load inventory from file (reload)");
        System.out.println("2  - Display all products");
        System.out.println("3  - Search product by ID");
        System.out.println("4  - Search product by name");
        System.out.println("5  - Add new product");
        System.out.println("6  - Remove product");
        System.out.println("7  - Update stock quantity");
        System.out.println("8  - Save inventory to file");
        System.out.println("9  - Add item to cart");
        System.out.println("10 - View cart");
        System.out.println("11 - Remove item from cart");
        System.out.println("12 - Update item quantity in cart");
        System.out.println("13 - Clear cart (restore stock)");
        System.out.println("14 - Undo last cart addition");
        System.out.println("15 - Generate bill / checkout");
        System.out.println("16 - Save and exit");
    }

    private static void handleSearchById(InventoryManager inventory) {
        int id = readInt("Enter product ID: ");
        Product p = inventory.searchById(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.println(p);
    }

    private static void handleSearchByName(InventoryManager inventory) {
        System.out.print("Enter name keyword: ");
        String q = scanner.nextLine();
        ArrayList<Product> matches = inventory.searchByName(q);
        if (matches.isEmpty()) {
            System.out.println("No matching products.");
            return;
        }
        System.out.printf("%-8s %-25s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
        System.out.println("-----------------------------------------------------------");
        for (Product p : matches) {
            System.out.printf("%-8d %-25s %-10.2f %-10d%n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }

    private static void handleAddProduct(InventoryManager inventory) {
        int id = readInt("Enter new product ID: ");
        if (inventory.searchById(id) != null) {
            System.out.println("Duplicate ID. Product not added.");
            return;
        }
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        double price = readDouble("Enter price: ");
        int stock = readInt("Enter stock quantity: ");
        if (stock < 0 || price < 0) {
            System.out.println("Price/stock cannot be negative.");
            return;
        }

        boolean ok = inventory.addProduct(new Product(id, name, price, stock));
        System.out.println(ok ? "Product added." : "Failed to add product.");
    }

    private static void handleRemoveProduct(InventoryManager inventory, CartList cart,
            LinkedListStack<CartAction> undoStack) {
        int id = readInt("Enter product ID to remove: ");
        if (cart.findItem(id) != null) {
            System.out.println("Cannot remove: product is currently in the cart.");
            return;
        }
        boolean ok = inventory.removeProduct(id);
        if (ok) {
            undoStack.clear();
            System.out.println("Product removed.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void handleUpdateStock(InventoryManager inventory) {
        int id = readInt("Enter product ID: ");
        int newStock = readInt("Enter new stock quantity: ");
        boolean ok = inventory.updateStock(id, newStock);
        System.out.println(ok ? "Stock updated." : "Update failed (product not found or invalid stock).");
    }

    private static void handleAddItemToCart(InventoryManager inventory, CartList cart,
            LinkedListStack<CartAction> undoStack) {
        int id = readInt("Enter product ID: ");
        int qty = readInt("Enter quantity to add: ");
        Product p = inventory.getProductById(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be > 0.");
            return;
        }

        if (!inventory.isAvailable(id, qty)) {
            System.out.println("Insufficient stock.");
            return;
        }

        boolean ok = cart.addItem(p, qty);
        if (!ok) {
            System.out.println("Insufficient stock.");
            return;
        }

        undoStack.push(new CartAction(id, qty));
        System.out.println("Item added to cart.");
    }

    private static void handleRemoveItemFromCart(CartList cart, LinkedListStack<CartAction> undoStack) {
        int id = readInt("Enter product ID to remove from cart: ");
        boolean ok = cart.removeItem(id);
        if (ok) {
            undoStack.clear();
            System.out.println("Item removed from cart and stock restored.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    private static void handleUpdateCartQuantity(CartList cart, LinkedListStack<CartAction> undoStack) {
        int id = readInt("Enter product ID in cart: ");
        int newQty = readInt("Enter new quantity (0 removes item): ");
        boolean ok = cart.updateQuantity(id, newQty);
        if (ok) {
            undoStack.clear();
            System.out.println("Cart quantity updated.");
        } else {
            System.out.println("Update failed (item missing, invalid qty, or insufficient stock).");
        }
    }

    // Handle the undo operation using the cart's undo feature

    private static void handleCheckout(CartList cart, LinkedListStack<CartAction> undoStack, InventoryManager inventory,
            String inventoryFile) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }

        System.out.println("\n===== BILL =====");
        cart.displayCart();
        System.out.println("===== END BILL =====");

        // Clear the cart items permanently without adding stock back to inventory
        cart.clearWithoutRestoringStock();
        undoStack.clear();
        System.out.println("Checkout complete. Cart cleared.");

        System.out.print("Save inventory changes now? (Y/N): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        if (ans.equals("y") || ans.equals("yes")) {
            inventory.saveToFile(inventoryFile);
            System.out.println("Inventory saved to: " + inventoryFile);
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static String resolveInventoryFile() {
        File f1 = new File("viva2" + File.separator + "inventory.txt");
        if (f1.exists()) {
            return f1.getPath();
        }
        return "inventory.txt";
    }
}