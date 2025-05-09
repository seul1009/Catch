package com.catcher.catchApp.service;

import com.catcher.catchApp.model.AuthenticationCode;
import com.catcher.catchApp.repository.AuthenticationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to, String ePw)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("Catch 회원가입 인증 코드 "); //메일 제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 이메일 인증 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id,"Catch"));

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public String sendSimpleMessage(String to) throws Exception {
        String ePw = createKey();
        MimeMessage message = createMessage(to, ePw);
        try{
            javaMailSender.send(message); // 메일 발송

            authenticationCodeRepository.findByEmail(to)
                    .ifPresent(authenticationCodeRepository::delete);

            AuthenticationCode authCode = new AuthenticationCode(to, ePw);
            authenticationCodeRepository.save(authCode);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument in sendSimpleMessage: " + e.getMessage());
            throw e;
        }
        return ePw;
    }

    public boolean authCode(String email, String inputCode) {
        Optional<AuthenticationCode> optionalAuthCode = authenticationCodeRepository.findByEmail(email);

        return optionalAuthCode
                .filter(authCode -> authCode.getCode().equals(inputCode))
                .filter(authCode -> {
                    long now = System.currentTimeMillis();
                    long created = authCode.getCreatedAt().getTime();
                    long elapsedMinutes = (now - created) / 1000 / 60;

                    return elapsedMinutes <= 5; // 인증 코드 유효기간 5분
                })
                .isPresent();
    }
}