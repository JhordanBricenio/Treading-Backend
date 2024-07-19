package com.codej.controller;


import com.codej.domain.VerificationType;
import com.codej.model.ForgotPasswordToken;
import com.codej.model.UserEntity;
import com.codej.model.VerificationCode;
import com.codej.request.ResetPasswordRequest;
import com.codej.response.ApiResponse;
import com.codej.response.AuthResponse;
import com.codej.request.ForgotPasswordRequest;
import com.codej.service.EmailService;
import com.codej.service.IForgotPasswordService;
import com.codej.service.IUserService;
import com.codej.service.IVerificationCodeService;
import com.codej.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private IUserService userService;

    private EmailService emailService;

    private IVerificationCodeService verificationCodeService;

    private final IForgotPasswordService forgotPasswordService;

    @GetMapping("/user/profile")
    public ResponseEntity<UserEntity> getUserProfile(@RequestHeader("Authorization") String jwt){
        UserEntity userEntity = userService.findUserProfileByJwt(jwt);
        return  new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
    }

    @PostMapping("/user/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerification(@RequestHeader("Authorization") String jwt,
                                                       @PathVariable VerificationType verificationType) throws MessagingException {
        UserEntity userEntity = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode= verificationCodeService.getVerificationCodeByUser(userEntity.getId());
        if(verificationCode==null){
            verificationCode= verificationCodeService.sendVerificationCode(userEntity, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(userEntity.getEmail(), verificationCode.getOtp());
        }
        return  new ResponseEntity<String>("verification otp send successfully", HttpStatus.OK);
    }

    @PatchMapping("/user/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<UserEntity> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                                    @PathVariable String otp) throws  Exception{
        UserEntity userEntity = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode= verificationCodeService.getVerificationCodeByUser(userEntity.getId());
        String sendTo= verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified= verificationCode.getOtp().equals(otp);
        if (isVerified){
            UserEntity updatedUser= userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(), sendTo, userEntity);

            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return  new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw  new Exception("wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        UserEntity user = userService.findByEmail(request.getSendTo());
        String otp= OtpUtils.generateOtp();
        UUID uuid= UUID.randomUUID();
        String id= uuid.toString();

        ForgotPasswordToken token= forgotPasswordService.findByUser(user.getId());
        if(token==null){
            token=forgotPasswordService.createToken(user, id, otp, request.getVerificationType(), request.getSendTo());
        }
        if (request.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse authResponse= new AuthResponse();
        authResponse.setSession(token.getId());
        authResponse.setMessage("Password reset otp sent successfully");
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                                     @RequestBody ResetPasswordRequest reset,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {
        ForgotPasswordToken forgotPasswordToken= forgotPasswordService.findById(id);
        boolean isVerified= forgotPasswordToken.getOtp().equals(reset.otp());
        if (isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), reset.password());
            ApiResponse apiResponse= new ApiResponse("password update successfully");
            return  new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw  new Exception("wrong otp");


    }
}
