package com.fit.se.controllers;

import com.fit.se.models.Order;
import com.fit.se.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;

    @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        order.setCreateAt(LocalDate.now());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) {
        var optionalOrder = orderRepository.findById(id);
        // Check if the order exists
        if (optionalOrder.isPresent()) {
            // If the order exists, delete it and return a success message
            orderRepository.deleteById(id);
            return ResponseEntity.ok("Delete success with order id: " + id);
        } else {
            // If the order does not exist, return a 404 Not Found response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order){
        Long orderId = order.getId();
        if (orderId == null || !orderRepository.existsById(orderId)) {
            return ResponseEntity.notFound().build();
        } else {
            // Retrieve the existing order from the repository
            Order existingOrder = orderRepository.findById(orderId).get();
            // Update attributes of the existing order with new values
//            existingOrder.setSomeProperty(order.getSomeProperty());
            // Update other properties as needed
            existingOrder.setPrice(order.getPrice());
            // Save the updated order back to the repository
            Order updatedOrder = orderRepository.save(existingOrder);
            // Return the updated order with HTTP status OK
            return ResponseEntity.ok(updatedOrder);
        }
    }
}
