package com.book.BookStore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.book.BookStore.DTO.OderMapper;
import com.book.BookStore.DTO.OrderItemRequest;
import com.book.BookStore.DTO.OrderRequest;
import com.book.BookStore.DTO.OrderResponseDTO;
import com.book.BookStore.Repo.BookRepo;
import com.book.BookStore.Repo.OrderRepo;
import com.book.BookStore.entity.Book;
import com.book.BookStore.entity.Order;
import com.book.BookStore.entity.OrderItem;
import com.book.BookStore.entity.OrderStatus;
import com.book.BookStore.entity.User;
import com.book.BookStore.exceptions.BookNotFound;
import com.book.BookStore.exceptions.OrderNotFoundException;
import com.book.BookStore.exceptions.OutOfStockException;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final BookRepo bookRepo;

    public OrderResponseDTO placeOrder(User user, OrderRequest request) {

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus("PAID");

        double totalAmount = 0;
        for (OrderItemRequest itemReq : request.getItems()) {
            Book book = bookRepo.findById(itemReq.getBookId())
                    .orElseThrow(() -> new BookNotFound(itemReq.getBookId()));

            // check stock
            if (book.getStock() < itemReq.getQuantity()) {
                throw new OutOfStockException(book.getTitle());

            }
            // reduce stock
            book.setStock(book.getStock() - itemReq.getQuantity());
            // create OrderItem
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(itemReq.getQuantity());
            item.setPriceAtPurchase(book.getPrice());
            totalAmount = totalAmount + book.getPrice() * itemReq.getQuantity();

            order.addItem(item);

        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepo.save(order);
        return OderMapper.toDTO(savedOrder);

    }

    // Orders which Only Admin can
    public List<OrderResponseDTO> getAllOrders(String sortBy,String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return orderRepo.findAll(sort).stream().map(OderMapper::toDTO).toList();
    }

    public Order updatOrderStatus(@NonNull Long orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        order.setOrderStatus(status);
        return orderRepo.save(order);
    }

    public List<OrderResponseDTO> getOrdersByUser(User user) {
        return orderRepo.findByUserId(user.getId()).stream().map(OderMapper::toDTO).toList();
    }

}
