package com.company;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.PaymentInfo;
import com.qiwi.billpayments.sdk.model.out.BillResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class Qiwi {
    static String secretKey;
    static String publicKey;
    static int sum;
    static String currency;
    static String build = UUID.randomUUID().toString();

    static BillPaymentClient client;

    public String getPayLink(String publicKey, String secretKey, int sum, String currency) {
        Qiwi.publicKey = publicKey;
        Qiwi.secretKey = secretKey;
        Qiwi.sum = sum;
        Qiwi.currency = currency;
        Qiwi.client = BillPaymentClientFactory.createDefault(secretKey);
        MoneyAmount amount = new MoneyAmount(BigDecimal.valueOf(sum), Currency.getInstance(currency));
        String successUrl = "";
        return Qiwi.client.createPaymentForm(new PaymentInfo(publicKey, amount, build, successUrl));
    }

    public boolean getStatusValue() {
        BillResponse response = Qiwi.client.getBillInfo(build);
        return response.getStatus().getValue().toString().equals("BillStatus{value='PAID'}");
    }

    public String getReqUser() {
        BillResponse response = Qiwi.client.getBillInfo(build);
        return response.getCustomer().getPhone();
    }

    public String getSum() {
        BillResponse response = Qiwi.client.getBillInfo(build);
        return response.getAmount().getValue().toString();
    }

    public void refreshBuild() {
        Qiwi.build = UUID.randomUUID().toString();
    }

    public void setSum(int sum) {
        Qiwi.sum = sum;
    }
}
