package com.lagou.oauth.mult.authenticator.sms.result;


import com.lagou.oauth.exception.AuthErrorType;
import lombok.Data;

@Data
public class SmsCodeValidateResult {
    private boolean success = true;
    private AuthErrorType authErrorType;
}
