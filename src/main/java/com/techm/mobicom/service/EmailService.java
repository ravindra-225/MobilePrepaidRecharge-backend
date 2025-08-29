package com.techm.mobicom.service;

import com.techm.mobicom.dto.RechargeRequestDTO;
import com.techm.mobicom.models.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRechargeConfirmationEmail(String toEmail, RechargeRequestDTO request, Plan plan, String transactionId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Recharge Confirmation - Mobile Prepaid Recharge");
        helper.setText(
            "<h2>Recharge Confirmation</h2>" +
            "<p>Dear Customer,</p>" +
            "<p>Your recharge has been successfully processed. Below are the details:</p>" +
            "<ul>" +
            "<li><b>Mobile Number:</b> " + request.getMobileNumber() + "</li>" +
            "<li><b>Plan:</b> " + plan.getName() + " (" + plan.getDescription() + ")</li>" +
            "<li><b>Amount:</b> ₹" + request.getAmount() + "</li>" +
            "<li><b>Payment Mode:</b> " + request.getPaymentMode() + "</li>" +
            "<li><b>Transaction ID:</b> " + transactionId + "</li>" +
            "<li><b>Payment Details:</b> " + getPaymentDetailsSummary(request.getPaymentDetails(), request.getPaymentMode()) + "</li>" +
            "</ul>" +
            "<p>Thank you for choosing our service!</p>" +
            "<p>Regards,<br>Tech Mahindra Mobile Recharge Team</p>",
            true
        );

        mailSender.send(message);
    }

    private String getPaymentDetailsSummary(RechargeRequestDTO.PaymentDetails details, String paymentMode) {
        switch (paymentMode.toUpperCase()) {
            case "UPI":
                return "UPI ID: " + maskUpiId(details.getUpiId());
            case "CREDIT_CARD":
            case "DEBIT_CARD":
                return "Card Ending: " + maskCardNumber(details.getCardNumber());
            case "NET_BANKING":
            case "BANK_TRANSFER":
                return "Account Ending: " + maskAccountNumber(details.getBankAccountNumber()) + ", IFSC: " + details.getBankIfscCode();
            default:
                return "N/A";
        }
    }

    private String maskUpiId(String upiId) {
        if (upiId == null) return "N/A";
        int atIndex = upiId.indexOf('@');
        if (atIndex <= 2) return upiId;
        return upiId.substring(0, 2) + "****" + upiId.substring(atIndex);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "N/A";
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "N/A";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}