package viva2;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class InventoryManager {
    private ArrayList<Product> inventory;

    public InventoryManager() {
        this.inventory = new ArrayList<>();
    }

    public void loadFromFile(String filename) {
        inventory.clear();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int stock = Integer.parseInt(parts[3].trim());
                    inventory.add(new Product(id, name, price, stock));
                }
            }
            System.out.println("Inventory loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("inventory.txt not found. Starting with an empty inventory.");
        } catch (Exception e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : inventory) {
                writer.println(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock());
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void addProduct(Product p) {
        if (searchById(p.getId()) != null) {
            System.out.println("Error: Product with ID " + p.getId() + " already exists.");
        } else {
            inventory.add(p);
            System.out.println("Product added successfully.");
        }
    }

    public void removeProduct(int id) {
        Product p = searchById(id);
        if (p != null) {
            inventory.remove(p);
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Error: Product not found.");
        }
    }

    public Product searchById(int id) {
        for (Product p : inventory) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public ArrayList<Product> searchByName(String name) {
        ArrayList<Product> results = new ArrayList<>();
        String lowerName = name.toLowerCase();
        for (Product p : inventory) {
            if (p.getName().toLowerCase().contains(lowerName)) {
                results.add(p);
            }
        }
        return results;
    }

    public void updateStock(int id, int newStock) {
        Product p = searchById(id);
        if (p != null) {
            p.setStock(newStock);
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Error: Product not found.");
        }
    }

    public void displayAll() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-10s | %-5s\n", "ID", "Name", "Price", "Stock");
        System.out.println("---------------------------------------------------------------");
        for (Product p : inventory) {
            System.out.printf("%-5d | %-15s | RM%-8.2f | %-5d\n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
        System.out.println("---------------------------------------------------------------");
    }

    public Product getProductById(int id) {
        return searchById(id);
    }

    public boolean isAvailable(int id, int requestedQty) {
        Product p = searchById(id);
        return p != null && p.getStock() >= requestedQty;
    }
}