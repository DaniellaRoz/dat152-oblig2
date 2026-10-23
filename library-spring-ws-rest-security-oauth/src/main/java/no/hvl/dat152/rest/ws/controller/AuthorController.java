
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthor() {

        List<Author> authors = authorService.findAll();

        if (authors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(authors, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable int id) {

        try {

            Author author = authorService.findById(id);
            return new ResponseEntity<>(author, HttpStatus.OK);

        } catch (AuthorNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable int id) {

        try {

            Set<Book> books = authorService.findBooksByAuthorId(id);
            return new ResponseEntity<>(books, HttpStatus.OK);

        } catch (AuthorNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {

        Author savedAuthor = authorService.saveAuthor(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody Author author) {

        try {

            Author updatedAuthor = authorService.updateAuthor(author, id);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);

        } catch (AuthorNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {

        try {

            authorService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthorNotFoundException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
        }

    }
}
