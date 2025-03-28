/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examen2labprogra2;

/**
 *
 * @author laraj
 */
public class HashTable {
    private Entry head;

    public HashTable() {
        this.head = null;
    }

    public void add(String username, long pos) {
        Entry newEntry = new Entry(username, pos);
        if (head == null) {
            head = newEntry;
        } else {
            Entry current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newEntry);
        }
    }
    
    public boolean remove(String username) {
        if (head == null) {
            return false;
        }

        if (head.getUsername().equals(username)) {
            head = head.getNext();
            return true;
        }

        Entry current = head;
        while (current.getNext() != null) {
            if (current.getNext().getUsername().equals(username)) {
                current.setNext(current.getNext().getNext());
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    public Long search(String username) {
        Entry current = head;
        username = username.toLowerCase();  
        while (current != null) {
            if (current.getUsername().toLowerCase().equals(username)) { 
                return current.getPosition();
            }
            current = current.getNext();
        }
        return -1L;
    }
}
