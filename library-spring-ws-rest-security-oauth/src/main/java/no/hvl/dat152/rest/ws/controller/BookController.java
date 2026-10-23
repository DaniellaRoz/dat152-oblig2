/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.BookService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@PreAuthorize ("hasAuthority('ADMIN')")
	@GetMapping("/books")
	public ResponseEntity<Object> getAllBooks(){
		
		List<Book> books = bookService.findAll();
		
		if(books.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(books, HttpStatus.OK);		
	}
	
	@PreAuthorize ("hasAuthority('ADMIN')")
	@GetMapping("/books/{isbn}")
	public ResponseEntity<Object> getBook(@PathVariable String isbn) throws BookNotFoundException{
		
		Book book = bookService.findByISBN(isbn);
		
		return new ResponseEntity<>(book, HttpStatus.OK);
				
	}
	
	@PreAuthorize ("hasAuthority('ADMIN')")
	@PostMapping("/books")
	public ResponseEntity<Book> createBook(@RequestBody Book book){
		
		Book nbook = bookService.saveBook(book);
		
		return new ResponseEntity<>(nbook, HttpStatus.CREATED);
	}
	

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/books/{isbn}/authors")
    public ResponseEntity<Set<Author>> getAuthorsOfBookByISBN(
            @PathVariable String isbn) {
        try {
            Set<Author> authors =
                    bookService.findAuthorsOfBookByISBN(isbn);

            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/books/{isbn}")
    public ResponseEntity<Book> updateBookByISBN(
            @PathVariable String isbn,
            @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(book, isbn);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<Void> deleteBookByISBN(@PathVariable String isbn) {
        try {
            bookService.deleteByISBN(isbn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
