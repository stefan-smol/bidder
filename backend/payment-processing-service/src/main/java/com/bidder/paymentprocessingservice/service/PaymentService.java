package com.bidder.paymentprocessingservice.service;

import com.bidder.paymentprocessingservice.model.Payment;
import com.bidder.paymentprocessingservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        System.out.println("PaymentService initialized");
    }

    public List<Payment> findAllPayments() {
        System.out.println("Executing findAllPayments");
        return paymentRepository.findAll();
    }

    public Payment findPaymentById(Long orderId) {
        System.out.println("Executing findPaymentById for orderId: " + orderId);
        return paymentRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this id: " + orderId));
    }

    public List<Payment> findPaymentsByUsername(String username) {
        System.out.println("Executing findPaymentsByUsername for username: " + username);
        return paymentRepository.findByUsername(username);
    }

    public Payment findPaymentByAuctionId(Long auctionId) {
        System.out.println("Executing findPaymentByAuctionId for auctionId: " + auctionId);
        return paymentRepository.findByAuctionId(auctionId);
    }

    public Payment savePayment(Payment payment) {
        System.out.println("Executing savePayment with payment details: " + payment);
        System.out.println("Executing savePayment with CSC: " + payment.getCsc());
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long orderId, Payment paymentDetails) {
        System.out.println("Executing updatePayment for orderId: " + orderId + " with new details: " + paymentDetails);
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this id: " + orderId));

        payment.setUsername(paymentDetails.getUsername());
        payment.setCreditCard(paymentDetails.getCreditCard());
        payment.setAuctionId(paymentDetails.getAuctionId());
        payment.setTotalAmount(paymentDetails.getTotalAmount());

        return paymentRepository.save(payment);
    }

    public void deletePayment(Long orderId) {
        System.out.println("Executing deletePayment for orderId: " + orderId);
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this id: " + orderId));

        paymentRepository.delete(payment);
    }
}
