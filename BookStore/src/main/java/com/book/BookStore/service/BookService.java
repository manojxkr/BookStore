package com.book.BookStore.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.book.BookStore.DTO.BookMapper;
import com.book.BookStore.DTO.BookResponseDTO;
import com.book.BookStore.Repo.BookRepo;
import com.book.BookStore.entity.Book;
import com.book.BookStore.exceptions.BookNotFound;
import org.springframework.lang.NonNull;

@Service
public class BookService {
    private final BookRepo repo;

    public BookService(BookRepo repo) {
        this.repo = repo;

    }

    @NonNull
    public BookResponseDTO addbook(@NonNull Book book) {
        return BookMapper.toDTO(repo.save(book));
    }

    public Page<BookResponseDTO> getAllBooks(int page, int size, String sortBy, String direction) {
        // Pageable pageable = PageRequest.of(page, size);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return repo.findAll(pageable).map(BookMapper::toDTO);
    }

    public Book getBookById(@NonNull Long id) {
        return repo.findById(id).orElseThrow(() -> new BookNotFound(id));

    }

    public List<BookResponseDTO> searchBooks(String keyword) {
        return repo.findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(keyword, keyword).stream()
                .map(BookMapper::toDTO).toList();
    }

    @NonNull
    public BookResponseDTO updateBook(@NonNull Long id, Book updateBook) {
        Book book = getBookById(id);

        book.setTitle(updateBook.getTitle());
        book.setAuthors(updateBook.getAuthors());
        book.setPrice(updateBook.getPrice());
        book.setStock(updateBook.getStock());
        book.setImageUrl(updateBook.getImageUrl());
        book.setIsbn(updateBook.getIsbn());
        book.setDescription(updateBook.getDescription());
        book.setGenre(updateBook.getGenre());

        return BookMapper.toDTO(repo.save(book));

    }

    public void deleteBook(@NonNull Long id) {
        Book book = getBookById(id);
        repo.delete(book);
    }

}
