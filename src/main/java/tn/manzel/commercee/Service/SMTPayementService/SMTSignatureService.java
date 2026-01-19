package tn.manzel.commercee.Service.SMTPayementService;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Service
public class SMTSignatureService {

    public static String sign(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("HMAC signing error", e);
        }
    }



    public static boolean verify(String payload, String signature, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));

            String expected =
                    Base64.getEncoder().encodeToString(mac.doFinal(payload.getBytes()));

            return expected.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
