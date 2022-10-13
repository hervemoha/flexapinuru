package com.infosetgroup.flexapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosetgroup.flexapi.entity.NotificationNuru;
import com.infosetgroup.flexapi.entity.VerificationNuru;
import com.infosetgroup.flexapi.repository.NotificationNuruRepository;
import com.infosetgroup.flexapi.repository.VerificationNuruRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.infosetgroup.flexapi.apalia.VerificationRequest;
import com.infosetgroup.flexapi.apalia.VerificationResponse;
import com.infosetgroup.flexapi.apalia.NotificationRequest;

@RestController
@RequestMapping(value = "api/rest")
public class ApiRestApalia {

    @Autowired
    private VerificationNuruRepository verificationNuruRepository;

    @Autowired
    private NotificationNuruRepository notificationNuruRepository;

    @PostMapping(value = "/v1/nuru/verification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VerificationResponse> verification(@RequestBody VerificationRequest requestDto) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("/v1/nuru/verification : " + formatter.format(now));
        System.out.println(requestDto);
        VerificationResponse response = new VerificationResponse();
        VerificationNuru verificationNuru = new VerificationNuru();
        try {
            String referenceNumber = requestDto.getReferenceNumber().trim();
            String merchantCode = requestDto.getMerchantCode().trim();
            String customerMsisdn = requestDto.getCustomerMsisdn().trim();
            String customer = requestDto.getCustomer().trim();

            verificationNuru.setReferenceNumber(referenceNumber);
            verificationNuru.setMerchantCode(merchantCode);
            verificationNuru.setCustomerMsisdn(customerMsisdn);
            verificationNuru.setCustomer(customer);
            this.verificationNuruRepository.save(verificationNuru);

            String DEFAULT_USER = "flexpay";
            //String DEFAULT_PASS = "{?o023ZSRKR+VJCD";
            String DEFAULT_PASS = "fT322As*84ok";
            String auth = DEFAULT_USER + ":" + DEFAULT_PASS;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .build();
            //String url = "https://dev.nurucongo.cloud/checkCustomerCode/flexpay/";
            String url = "https://nurucongo.cloud/checkCustomerCode/flexpay/";
            HttpGet request = new HttpGet(url+referenceNumber);
            //request.addHeader("content-type", "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            HttpResponse httpResponse = httpClient.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("Response code : " + responseCode);
            if (responseCode == 200 || responseCode == 201) {
                String resp = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(resp);
                response = new ObjectMapper().readValue(resp, VerificationResponse.class);

                verificationNuru.setStatus(response.getStatus());
                this.verificationNuruRepository.save(verificationNuru);
            }else {
                System.out.println("Erreur, impossible d'atteindre la passerelle");
                response.setStatus("ERROR");
                verificationNuru.setStatus(response.getStatus());
                this.verificationNuruRepository.save(verificationNuru);
            }
        }catch (Exception exception) {
            System.out.println("Exception : " + exception.getMessage());
            response.setStatus("ERROR");
            verificationNuru.setStatus(response.getStatus());
            this.verificationNuruRepository.save(verificationNuru);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/nuru/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> notification(@RequestBody NotificationRequest requestDto) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println("/v1/nuru/notification : " + formatter.format(now));
        System.out.println(requestDto);

        String code = requestDto.getCode().trim();
        String reference = requestDto.getReference().trim();
        String orderNumber = requestDto.getOrderNumber().trim();
        String channel = requestDto.getChannel().trim();
        String createdAt = requestDto.getCreatedAt().trim();
        String provider_reference = requestDto.getProvider_reference().trim();
        String amount = requestDto.getAmount().trim();
        String amountCustomer = requestDto.getAmountCustomer().trim();
        String currency = requestDto.getCurrency().trim();
        String phone = requestDto.getPhone().trim();

        NotificationNuru notificationNuru = new NotificationNuru();
        notificationNuru.setCode(code);
        notificationNuru.setReference(reference);
        notificationNuru.setOrderNumber(orderNumber);
        notificationNuru.setChannel(channel);
        notificationNuru.setCreatedDate(createdAt);
        notificationNuru.setProvider_reference(provider_reference);
        notificationNuru.setAmount(amount);
        notificationNuru.setAmountCustomer(amountCustomer);
        notificationNuru.setCurrency(currency);
        notificationNuru.setPhone(phone);

        this.notificationNuruRepository.save(notificationNuru);

        if (requestDto.getCode().equalsIgnoreCase("0")) {
            try {
                //String merchantCode = requestDto.getMerchantCode().trim();
                int amountToSend = (int) Double.parseDouble(requestDto.getAmountCustomer());
                String orderNumberToSend = requestDto.getOrderNumber().trim();
                String phoneToSend = "+"+requestDto.getPhone().trim();
                String referenceNumberTosend = requestDto.getReference().trim();
                String currencyToSend = requestDto.getCurrency().trim().toUpperCase();
                String urlParameters = "{\"mmoTransactionRef\": \""+orderNumberToSend+"\", \"customerCode\": \""+referenceNumberTosend+"\", \"currency\": \""+currencyToSend+"\", \"amount\": "+amountToSend+", \"msisdn\": \""+phoneToSend+"\"}";
                System.out.println("Json : " + urlParameters);
                StringEntity params = new StringEntity(urlParameters);

                String DEFAULT_USER = "flexpay";
                String DEFAULT_PASS = "fT322As*84ok";
                //String DEFAULT_PASS = "{?o023ZSRKR+VJCD";
                String auth = DEFAULT_USER + ":" + DEFAULT_PASS;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(StandardCharsets.ISO_8859_1));
                String authHeader = "Basic " + new String(encodedAuth);

                HttpClient httpClient = HttpClientBuilder
                        .create()
                        .build();
                //String url = "https://dev.nurucongo.cloud/api/v1/flexpay/payment";
                String url = "https://nurucongo.cloud/checkCustomerCode/flexpay/";
                HttpPost request = new HttpPost(url);
                request.addHeader("content-type", "application/json");
                request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
                request.setEntity(params);
                HttpResponse httpResponse = httpClient.execute(request);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                System.out.println("Response code : " + responseCode);

                notificationNuru.setResponseCode(responseCode+"");
                this.notificationNuruRepository.save(notificationNuru);

                if (responseCode == 200 || responseCode == 201) {
                    String resp = EntityUtils.toString(httpResponse.getEntity());
                    System.out.println(resp);

                    notificationNuru.setContentResponse(resp);
                    this.notificationNuruRepository.save(notificationNuru);
                    return new ResponseEntity<>(resp, HttpStatus.OK);
                }else {
                    System.out.println("Erreur, impossible d'atteindre la passerelle");
                    notificationNuru.setContentResponse(responseCode+"");
                    this.notificationNuruRepository.save(notificationNuru);
                }
            }catch (Exception exception) {
                System.out.println("Exception : " + exception.getMessage());
                exception.printStackTrace();
                notificationNuru.setContentResponse(exception.getMessage());
                this.notificationNuruRepository.save(notificationNuru);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
