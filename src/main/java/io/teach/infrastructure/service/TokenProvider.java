package io.teach.infrastructure.service;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenProvider {

     private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
     private static final String bodyEncryptionAlgorithm = "SHA-256";

     public static String createYWT(final Long mno, final Long ynmno, final String yid, final String encodedBody) {
          final Map<String, Object> tokenMap = new HashMap<>();
          tokenMap.put("mno", mno);
          tokenMap.put("ynmno", ynmno);
          tokenMap.put("yid", yid);
          final String header = encoder.encodeToString(new Gson().toJson(tokenMap).getBytes(StandardCharsets.UTF_8));

          return new StringBuilder()
                  .append(header)
                  .append(".")
                  .append(encodedBody)
                  .toString();

     }

     public static String encodedBody(final String email, final String phone) {
          final Map<String, Object> tokenMap = new HashMap<>();
          tokenMap.put("email", email);
          tokenMap.put("phone", phone);

          byte[] body = new Gson().toJson(tokenMap).getBytes(StandardCharsets.UTF_8);

          try {
               final MessageDigest digest =  MessageDigest.getInstance(bodyEncryptionAlgorithm);
               digest.update(body);

               return DatatypeConverter.printHexBinary(digest.digest()).toLowerCase();
          } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
          }

          return null;
     }
}
