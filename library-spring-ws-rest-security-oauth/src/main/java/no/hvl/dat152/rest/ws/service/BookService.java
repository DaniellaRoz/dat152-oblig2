
package no.hvl.dat152.rest.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) {

        return bookRepository.save(book);
		
    }

    public List<Book> findAll() {

        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;

    }

    public Book findByISBN(String isbn) throws BookNotFoundException {

        return bookRepository.findByIsbn(isbn)
            .orElseThrow(() ->
                new BookNotFoundException(
                    "Book with ISBN = " + isbn + " not found!"));

    }

    public Book updateBook(Book book, String isbn) throws BookNotFoundException {

        Book existingBook = findByISBN(isbn);

        existingBook.setTitle(book.getTitle());

        if (book.getAuthors() != null) {
            existingBook.setAuthors(new HashSet<>(book.getAuthors()));
        }

        return bookRepository.save(existingBook);

    }

    public List<Book> findAllPaginate(Pageable pageable) {

        return bookRepository.findAll(pageable).getContent();

    }

    public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {

        Book book = findByISBN(isbn);
        return book.getAuthors();

    }

    public void deleteById(long id) {

        bookRepository.deleteById(id);

    }

    public void deleteByISBN(String isbn) throws BookNotFoundException {

        Book book = findByISBN(isbn);
        bookRepository.delete(book);

    }
}
