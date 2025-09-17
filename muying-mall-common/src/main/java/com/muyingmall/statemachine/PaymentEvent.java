package com.muyingmall.statemachine;

public enum PaymentEvent {
    PAY_CREATE,
    PAY_PROCESS,
    PAY_SUCCESS,
    PAY_FAIL,
    PAY_TIMEOUT,
    PAY_CANCEL,
    PAY_REFUND
}