package com.infosetgroup.flexapi.apalia;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VerificationRequest {
    private String merchantCode;
    private String referenceNumber;
    private String customerMsisdn;
    private String customer;
}
