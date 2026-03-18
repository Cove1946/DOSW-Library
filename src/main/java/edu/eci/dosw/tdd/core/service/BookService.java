package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final List<User> users = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();
    private final Map<Book, Integer> books = new HashMap<>();

    public void addBook(Book book, int copies){
        books.put(book, copies);
    }

    public List<Book> getAllBooks(){
        return new ArrayList<>(books.keySet());
    }

    public Book getBookById(String id){
        return books.keySet().stream()
                .filter( b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void updateBookAvailability (String id, boolean available){
        Book book = getBookById(id);
        book.setAvailable(available);
    }



}