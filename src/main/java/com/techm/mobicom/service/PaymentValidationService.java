package com.techm.mobicom.service;

import com.techm.mobicom.dto.RechargeRequestDTO;
import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.models.PaymentMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PaymentValidationService {

    private static final Pattern UPI_PATTERN = Pattern.compile("^[a-zA-Z0-9.-]+@[a-zA-Z0-9]+$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^\\d{3}$");
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^\\d{9,18}$");
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    public void validatePaymentDetails(RechargeRequestDTO request) {
        RechargeRequestDTO.PaymentDetails details = request.getPaymentDetails();
        if (details == null) {
            throw new CustomException("Payment details are required", HttpStatus.BAD_REQUEST);
        }

        PaymentMode mode;
        try {
            mode = PaymentMode.valueOf(request.getPaymentMode().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException("Invalid payment mode", HttpStatus.BAD_REQUEST);
        }

        switch (mode) {
            case UPI:
                if (details.getUpiId() == null || !UPI_PATTERN.matcher(details.getUpiId()).matches()) {
                    throw new CustomException("Invalid UPI ID. Format: user@bank (e.g., user@okaxis, user@ybl)", HttpStatus.BAD_REQUEST);
                }
                break;
            case CREDIT_CARD:
            case DEBIT_CARD:
                if (details.getCardNumber() == null || !CARD_NUMBER_PATTERN.matcher(details.getCardNumber()).matches()) {
                    throw new CustomException("Invalid card number. Must be 16 digits", HttpStatus.BAD_REQUEST);
                }
                if (details.getCardExpiry() == null || !CARD_EXPIRY_PATTERN.matcher(details.getCardExpiry()).matches()) {
                    throw new CustomException("Invalid card expiry. Format: MM/YY", HttpStatus.BAD_REQUEST);
                }
                if (details.getCardCvv() == null || !CVV_PATTERN.matcher(details.getCardCvv()).matches()) {
                    throw new CustomException("Invalid CVV. Must be 3 digits", HttpStatus.BAD_REQUEST);
                }
                break;
            case NET_BANKING:
            case BANK_TRANSFER:
                if (details.getBankAccountNumber() == null || !ACCOUNT_NUMBER_PATTERN.matcher(details.getBankAccountNumber()).matches()) {
                    throw new CustomException("Invalid account number. Must be 9-18 digits", HttpStatus.BAD_REQUEST);
                }
                if (details.getBankIfscCode() == null || !IFSC_PATTERN.matcher(details.getBankIfscCode()).matches()) {
                    throw new CustomException("Invalid IFSC code. Format: ABCD0XXXXXX", HttpStatus.BAD_REQUEST);
                }
                break;
            default:
                throw new CustomException("Unsupported payment mode", HttpStatus.BAD_REQUEST);
        }
    }
}