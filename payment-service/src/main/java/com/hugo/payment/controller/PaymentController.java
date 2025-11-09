package com.hugo.payment.controller;

import com.hugo.common.dto.PaymentRequestDTO;
import com.hugo.common.dto.PaymentResponseDTO;
import com.hugo.payment.model.PaymentStatus;
import com.hugo.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/registrar")
    public ResponseEntity<PaymentResponseDTO> registrarPagamento(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO payment = paymentService.registrarPagamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/code/{paymentCode}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByCode(@PathVariable String paymentCode) {
        PaymentResponseDTO payment = paymentService.getPaymentByCode(paymentCode);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        PaymentResponseDTO payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable String status) {
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByStatus(paymentStatus);
        return ResponseEntity.ok(payments);
    }
}