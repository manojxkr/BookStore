package com.book.BookStore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.BookStore.DTO.OrderRequest;
import com.book.BookStore.DTO.OrderResponseDTO;
import com.book.BookStore.DTO.UpdateOrderStatus;
import com.book.BookStore.Response.Response;
import com.book.BookStore.entity.Order;
import com.book.BookStore.entity.User;
import com.book.BookStore.service.CustomUserDetailsService;
import com.book.BookStore.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/orders")
    public ResponseEntity<Response<OrderResponseDTO>> placeOrder(@Valid @RequestBody OrderRequest orderRequest,
            Authentication authentication) {
        String email = authentication.getName();
        User user = customUserDetailsService.getUserByEmail(email);
        OrderResponseDTO order = orderService.placeOrder(user, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response<>(true, "Order placed successfully", order));
    }

    // Only ADMin can orders Of all Users
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<List<OrderResponseDTO>>> getAllOrders(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        List<OrderResponseDTO> orders = orderService.getAllOrders(sortBy, direction);
        return ResponseEntity.ok(
                new Response<>(true, "All orders fetched", orders));
    }

    @PutMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<Order>> updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatus req) {
        Order updated = orderService.updatOrderStatus(id, req.getStatus());
        return ResponseEntity.ok(
                new Response<>(true, "Order status updated", updated));

    }

    @GetMapping("/orders/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Response<List<OrderResponseDTO>>> getMyOrders(Authentication authentication) {

        String email = authentication.getName();
        User user = customUserDetailsService.getUserByEmail(email);

        return ResponseEntity.ok(
                new Response<>(true, "Yours orders fetched successfully", orderService.getOrdersByUser(user)));
    }

}
