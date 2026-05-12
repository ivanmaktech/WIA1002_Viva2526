package viva2;

public class LinkedListStack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    public LinkedListStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T value = top.data;
        top = top.next;
        size--;
        return value;
    }

    public T peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}
