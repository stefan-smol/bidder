package com.bidder.paymentprocessingservice.controller;

import com.bidder.paymentprocessingservice.model.Payment;
import com.bidder.paymentprocessingservice.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
        System.out.println("PaymentController initialized");
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        System.out.println("GET /api/v1/payments - getAllPayments called");
        try {
            List<Payment> payments = paymentService.findAllPayments();
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error in getAllPayments: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        System.out.println("GET /api/v1/payments/" + orderId + " - getPaymentByOrderId called");
        try {
            Payment payment = paymentService.findPaymentById(orderId);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            System.out.println("Payment not found for orderId: " + orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Error in getPaymentByOrderId for orderId " + orderId + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Payment>> getPaymentsByUsername(@PathVariable String username) {
        System.out.println("GET /api/v1/payments/user/" + username + " - getPaymentsByUsername called");
        try {
            List<Payment> payments = paymentService.findPaymentsByUsername(username);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error in getPaymentsByUsername for username " + username + ": " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<Payment> getPaymentsByAuctionId(@PathVariable Long auctionId) {
        System.out.println("GET /api/v1/payments/auction/" + auctionId + " - getPaymentsByAuctionId called");
        try {
            Payment payment = paymentService.findPaymentByAuctionId(auctionId);
            if (payment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error in getPaymentsByAuctionId for auctionId " + auctionId + ": " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        System.out.println("POST /api/v1/payments - createPayment called with payment: " + payment);
        try {
            Payment createdPayment = paymentService.savePayment(payment);
            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error in createPayment: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long orderId, @RequestBody Payment paymentDetails) {
        System.out.println("PUT /api/v1/payments/" + orderId + " - updatePayment called with paymentDetails: " + paymentDetails);
        try {
            Payment updatedPayment = paymentService.updatePayment(orderId, paymentDetails);
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            System.out.println("Payment not found for update with orderId: " + orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Error in updatePayment for orderId " + orderId + ": " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable Long orderId) {
        System.out.println("DELETE /api/v1/payments/" + orderId + " - deletePayment called");
        try {
            paymentService.deletePayment(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            System.out.println("Payment not found for deletion with orderId: " + orderId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Error in deletePayment for orderId " + orderId + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
