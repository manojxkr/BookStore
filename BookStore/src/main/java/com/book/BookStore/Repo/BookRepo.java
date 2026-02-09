package com.book.BookStore.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.book.BookStore.entity.Book;

public interface BookRepo extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorsContainingIgnoreCase(String authors);
    
    List<Book> findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(String title, String authors);

}
