package application.costa_tour.controller;

import application.costa_tour.dto.payment.OrderRequest;
import application.costa_tour.dto.payment.PaymentResponse;
import application.costa_tour.service.PaymentService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder() {
        PaymentResponse response = paymentService.createOrder(OrderRequest.builder()
                        .title("El mejor plan de tu vida")
                        .quantity(1)
                        .price(new BigDecimal(500000))
                        .currencyId("COP")
                        .build());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public String success() {
        return "Payment success";
    }

    @GetMapping("/failure")
    public String failure() {
        return "Payment failure";
    }

    @GetMapping("/pending")
    public String pending() {
        return "Payment pending";
    }

    @PostMapping("/webhook")
    public String webhook(HttpServletRequest req) {
        System.out.println(req.getQueryString() + " Webhook received");
        return "Webhook";
    }
}
