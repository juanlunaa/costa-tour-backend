package application.costa_tour.service;

import application.costa_tour.dto.payment.OrderRequest;
import application.costa_tour.dto.payment.PaymentResponse;
import application.costa_tour.exception.BadRequestException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PaymentService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    public PaymentResponse createOrder(OrderRequest order) {
        MercadoPagoConfig.setAccessToken(accessToken);

        PreferenceClient client = new PreferenceClient();

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title(order.getTitle())
                .quantity(order.getQuantity())
                .unitPrice(order.getPrice())
                .currencyId(order.getCurrencyId())
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(item))
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:4000/payment/success")
                        .failure("http://localhost:4000/payment/failure")
                        .pending("http://localhost:4000/payment/pending")
                        .build())
                .notificationUrl("https://4c3f-2800-e2-3580-1113-a4cb-dd8c-d165-d59d.ngrok-free.app/payment/webhook")
//                .autoReturn("approved")
                .build();

        try {
            Preference response = client.create(preferenceRequest);
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setInitPoint(response.getInitPoint());
            paymentResponse.setStatus("created");

            return paymentResponse;
        } catch (MPApiException ex) {
            throw new BadRequestException(String
                    .format("%s | api status: %s",
                            ex.getApiResponse().getContent(),
                            ex.getApiResponse().getStatusCode()));
        } catch (MPException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Error creating order");
        }
    }
}
