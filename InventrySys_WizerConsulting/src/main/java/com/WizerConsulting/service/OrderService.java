package com.WizerConsulting.service;

import com.WizerConsulting.model.Order;
import com.WizerConsulting.model.Product;
import com.WizerConsulting.repository.OrderRepository;
import com.WizerConsulting.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Order placeOrder(Long productId, int quantity, String customerName, String customerPhone) {
        Product product = productRepository.findById(productId).orElseThrow();
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerPhone(customerPhone);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setOrderDate(java.time.LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Send order details to Kafka for reporting
        kafkaTemplate.send("order-topic", "New order placed: " + savedOrder.getId());

        return savedOrder;
    }
}

