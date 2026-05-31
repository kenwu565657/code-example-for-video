package org.example;

public class EmptyProductException extends RuntimeException {
    public EmptyProductException() {
        super("Product Is Empty");
    }
}
