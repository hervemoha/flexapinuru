package com.infosetgroup.flexapi.apalia;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationRequest {
    private String code;
    private String reference;
    private String orderNumber;
    private String channel;
    private String createdAt;
    private String provider_reference;
    private String amount;
    private String amountCustomer;
    private String currency;
    private String phone;
}
