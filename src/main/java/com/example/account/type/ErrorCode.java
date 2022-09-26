package com.example.account.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자가 없습니다"),
    Account_NOT_FOUND("계좌가 없습니다"),
    USER_ACCOUNT_UNMACTH("사용자의 계좌가 아닙니다"),
    ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지되었습니"),
    BALANCE_NOT_EMPTY("잔액이 있는 계좌는 해지할수 없습니다"),

    MAX_ACCOUNT_PER_UESR_10("사용자 최대 계좌는 10개 입니다");

    private final String description;
}
