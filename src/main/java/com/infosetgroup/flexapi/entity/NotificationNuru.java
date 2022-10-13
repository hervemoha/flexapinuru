package com.infosetgroup.flexapi.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificationNuru {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String code;
    @Column(length = 100)
    private String reference;
    @Column(length = 100)
    private String orderNumber;
    @Column(length = 10)
    private String channel;
    @Column(length = 50)
    private String createdDate;
    @Column(length = 100)
    private String provider_reference;
    @Column(length = 50)
    private String amount;
    @Column(length = 50)
    private String amountCustomer;
    @Column(length = 50)
    private String currency;
    @Column(length = 20)
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String contentResponse;
    @Column(length = 20)
    private String responseCode;

    public NotificationNuru() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProvider_reference() {
        return provider_reference;
    }

    public void setProvider_reference(String provider_reference) {
        this.provider_reference = provider_reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountCustomer() {
        return amountCustomer;
    }

    public void setAmountCustomer(String amountCustomer) {
        this.amountCustomer = amountCustomer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getContentResponse() {
        return contentResponse;
    }

    public void setContentResponse(String contentResponse) {
        this.contentResponse = contentResponse;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @PrePersist
    void onCreate() {
        this.setCreatedAt(LocalDateTime.now());
        this.setModifiedAt(LocalDateTime.now());
    }

    @PreUpdate
    void onUpdate() {
        this.setModifiedAt(LocalDateTime.now());
    }
}
