package com.book.BookStore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Table
@Entity
@AllArgsConstructor

public class Book {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String authors;
    private Double price;
    private Integer stock;
    @Column(length = 10000)
    private String description;
    private String genre;
    private String imageUrl;
    @Column(unique = true)
    private String isbn;

    public Book() {
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuthors() {
        return authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
