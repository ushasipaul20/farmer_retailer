////package com.farmer_retailer.service;
////
////import org.springframework.mail.SimpleMailMessage;
////import org.springframework.mail.javamail.JavaMailSender;
////import org.springframework.stereotype.Service;
////
////@Service
////public class EmailService {
////
////    private final JavaMailSender mailSender;
////
////    public EmailService(JavaMailSender mailSender) {
////        this.mailSender = mailSender;
////    }
////
////    public void sendEmail(String to, String subject, String body) {
////        SimpleMailMessage message = new SimpleMailMessage();
////        message.setTo(to);
////        message.setSubject(subject);
////        message.setText(body);
////        message.setFrom("yourgmail@gmail.com");
////
////        mailSender.send(message);
////    }
////}
////
/// working
//package com.farmer_retailer.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    private static final String FROM_EMAIL = "ushasipaul20@gmail.com"; // SAME AS username
//
//    public void sendEmail(String to, String subject, String body) {
//
//        System.out.println("📧 Sending email to: " + to);
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(FROM_EMAIL);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        mailSender.send(message);
//
//        System.out.println("✅ Email sent successfully to: " + to);
//    }
//}

package com.farmer_retailer.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String FROM_EMAIL = "ushasipaul20@gmail.com";

    // ===============================
    // 📧 SIMPLE TEXT EMAIL
    // (Used in Farmer Verification)
    // ===============================
    public void sendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("✅ Email sent to " + to);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // 📎 EMAIL WITH PDF ATTACHMENT
    // (Used for Orders)
    // ===============================
    public void sendEmailWithPdf(
            String to,
            String subject,
            String body,
            byte[] pdfBytes
    ) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setFrom(FROM_EMAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(
                    "Order_Details.pdf",
                    new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);
            System.out.println("✅ Email with PDF sent to " + to);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
