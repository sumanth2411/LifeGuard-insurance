package com.techlabs.app.service;

import com.techlabs.app.dto.ResetPasswordRequestDto;

public interface EmailService {

  String sendOtpForForgetPassword(String userNameOrEmail);

  String verifyOtpAndSetNewPassword(ResetPasswordRequestDto forgetPasswordRequest);

}