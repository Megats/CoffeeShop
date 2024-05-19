package com.example.groupproject.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jdk.jfr.DataAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class paymentdto {
    private int payment_id;
    private int order_id;
    private String payment_status;
    private double payment_total;

}
