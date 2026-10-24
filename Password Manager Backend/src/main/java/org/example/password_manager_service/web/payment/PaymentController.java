package org.example.password_manager_service.web.payment;

import jakarta.validation.Valid;
import org.example.password_manager_service.service.payment.CreatePaymentRequest;
import org.example.password_manager_service.service.payment.PaymentService;
import org.example.password_manager_service.service.payment.PaymentStatusResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @SuppressWarnings("java:S2259")
    @PostMapping
    public PaymentStatusResponse createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(SecurityContextHolder.getContext().getAuthentication().getName(), request);
    }

}
