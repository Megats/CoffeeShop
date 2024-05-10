package com.example.groupproject.controller;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentController {

//    public void createPaymentIntent() {
//        //Stripe Key
//        Stripe.setApiKey("sk_test_51PEWXdCVQMcEGtfuYpFL60VeiSXXIuqETALW960OQwtOAFJkNrT4H4COiSik9u9ThR9MD0G7ujeBY0UiufqyeTf200kSZqbE1q");
//        PaymentIntentCreateParams params =
//                PaymentIntentCreateParams.builder()
//                        .setAmount(500L)
//                        .setCurrency("gbp")
//                        .setPaymentMethod("pm_card_visa")
//                        .build();
//
//        try {
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}