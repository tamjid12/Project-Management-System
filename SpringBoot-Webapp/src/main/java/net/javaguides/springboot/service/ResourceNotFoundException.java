package net.javaguides.springboot.service;
/**
 * Author: Akid Tamjid Rahman
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
