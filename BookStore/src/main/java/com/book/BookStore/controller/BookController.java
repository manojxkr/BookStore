package com.book.BookStore.controller;

import java.util.List;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.BookStore.DTO.BookResponseDTO;
import com.book.BookStore.Response.Response;
import com.book.BookStore.entity.Book;
import com.book.BookStore.service.BookService;

import org.springframework.lang.NonNull;

@RequestMapping("api")
@RestController
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;

    }

    @GetMapping("/books/search")
    public ResponseEntity<Response<List<BookResponseDTO>>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(
                new Response<>(true, "Search results", service.searchBooks(keyword)));
    }

    @GetMapping("/books")
    public Page<BookResponseDTO> getAllBooks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return service.getAllBooks(page, size, sortBy, direction);

    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Response<Book>> getBook(@NonNull @PathVariable Long id) {
        Book book = service.getBookById(id);
        return ResponseEntity.ok(
                new Response<>(true, "Book found", book));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/book/{id}")
    public ResponseEntity<Response<BookResponseDTO>> updateBook(@NonNull @PathVariable Long id,
            @RequestBody Book book) {
        BookResponseDTO updated = service.updateBook(id, book);
        return ResponseEntity.ok(
                new Response<>(true, "Book updated successfully", updated));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/books")
    public ResponseEntity<Response<BookResponseDTO>> addBook(@NonNull @RequestBody Book book) {
        BookResponseDTO saved = service.addbook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(true, "Books added successufully", saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Response<Void>> deleteBook(@NonNull @PathVariable Long id) {
        service.deleteBook(id);
        return ResponseEntity.ok(new Response<>(true, "Book deleted successfully", null));

    }

}
