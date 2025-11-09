package com.hugo.payment.service;

import com.hugo.payment.dto.PaymentRequestDTO;
import com.hugo.payment.dto.PaymentResponseDTO;
import com.hugo.payment.model.Payment;
import com.hugo.payment.model.PaymentStatus;
import com.hugo.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponseDTO registrarPagamento(PaymentRequestDTO request) {
        String paymentCode = generatePaymentCode();

        Payment payment = new Payment(
            paymentCode,
            request.getOrderId(),
            request.getAmount(),
            PaymentStatus.CONFIRMADO
        );
        
        payment.setProcessedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        System.out.println("💰 Pagamento registrado: " + paymentCode + 
                          " - Pedido: " + request.getOrderId() + 
                          " - Valor: R$" + request.getAmount());

        return convertToResponseDTO(savedPayment);
    }

    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return convertToResponseDTO(payment);
    }

    public PaymentResponseDTO getPaymentByCode(String paymentCode) {
        Payment payment = paymentRepository.findByPaymentCode(paymentCode)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return convertToResponseDTO(payment);
    }

    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado para o pedido"));
        return convertToResponseDTO(payment);
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private String generatePaymentCode() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setPaymentCode(payment.getPaymentCode());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus().name());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setExpiresAt(payment.getExpiresAt());
        dto.setProcessedAt(payment.getProcessedAt());
        return dto;
    }
}