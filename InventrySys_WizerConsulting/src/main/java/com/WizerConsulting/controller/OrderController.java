package com.WizerConsulting.controller;
import com.WizerConsulting.model.Order;
import com.WizerConsulting.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestParam Long productId,
                            @RequestParam int quantity,
                            @RequestParam String customerName,
                            @RequestParam String customerPhone) {
        return orderService.placeOrder(productId, quantity, customerName, customerPhone);
    }
}

