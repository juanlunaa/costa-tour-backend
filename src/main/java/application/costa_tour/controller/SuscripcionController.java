package application.costa_tour.controller;

import application.costa_tour.dto.payment.OrderRequest;
import application.costa_tour.dto.payment.PaymentResponse;
import application.costa_tour.model.Usuario;
import application.costa_tour.model.UsuarioSuscripcion;
import application.costa_tour.service.PaymentService;
import application.costa_tour.service.SuscripcionService;
import application.costa_tour.service.UsuarioService;
import com.mercadopago.resources.payment.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.math.BigDecimal;

@RestController
@RequestMapping("/subscription")
public class SuscripcionController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SuscripcionService suscripcionService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/order")
    public ResponseEntity<?> crearSuscripcion(@RequestParam Long userId) {
        Usuario user = usuarioService.getUserById(userId);

        UsuarioSuscripcion userSuscripcion = suscripcionService.crearSuscripcion(user);

        PaymentResponse response = paymentService.createOrder(
                    OrderRequest.builder()
                        .title("Suscripcion para convertirse en usuario exclusivo de Costa Tour")
                        .price(new BigDecimal(userSuscripcion.getSuscripcion().getPrecio()))
                        .quantity(1)
                        .currencyId("COP")
                        .build(),
                    userSuscripcion.getIdPago(),
                    "subscription/webhook-payment");

        suscripcionService.updatePaymentId(userSuscripcion.getId(), response.getPaymentId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook-payment")
    public String webhook(
            @RequestParam(value = "data.id", required = false) Long dataId,
            @RequestParam(value = "type", required = false) String type
    ) {
        if (type != null && type.equals("payment")) {
            Payment paymentInfo = paymentService.getPaymentInfo(dataId);
            if (paymentInfo.getStatus().equals("approved")) {
                System.out.println(paymentInfo.getExternalReference());
                suscripcionService.activarSuscripcion(paymentInfo.getExternalReference(), 30);
            } else {
                System.out.println(paymentInfo.getExternalReference());
                suscripcionService.cancelarSuscripcion(paymentInfo.getExternalReference());
            }
        } else {
            System.out.println("Subscription webhook received");
        }
        return "Webhook";
    }
}
