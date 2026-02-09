package com.book.BookStore.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.BookStore.entity.Order;


public interface OrderRepo extends JpaRepository<Order, Long> {
  List<Order> findByUserId(Long userid);

  // Page<Order> findAll(Pageable pageable);

}
