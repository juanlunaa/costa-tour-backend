package application.costa_tour.service;

import application.costa_tour.dto.payment.OrderRequest;
import application.costa_tour.dto.payment.PaymentResponse;
import application.costa_tour.exception.BadRequestException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Value("${https.domain.ngrok}")
    private String httpsDomainNgrok;

    public PaymentResponse createOrder(OrderRequest order, String externalReference, String webhookRoute, String successUrl, String failureUrl) {
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
                .externalReference(externalReference)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(successUrl)
                        .failure(failureUrl)
                        .pending("http://localhost:4000/payment/pending")
                        .build())
                .notificationUrl(httpsDomainNgrok + webhookRoute)
//                .autoReturn("approved")
                .build();

        try {
            Preference response = client.create(preferenceRequest);
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setInitPoint(response.getInitPoint());
            paymentResponse.setStatus("created");
            paymentResponse.setPaymentId(response.getExternalReference());

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

    public Payment getPaymentInfo(Long paymentId) {
        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            PaymentClient client = new PaymentClient();

            Payment payment = client.get(paymentId);

            return payment;
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
