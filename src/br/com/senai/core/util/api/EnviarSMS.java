package br.com.senai.core.util.api;

import java.io.IOException;
import java.util.Properties;

import br.com.senai.core.util.properties.Manipulador;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnviarSMS {

    public static Response enviarSMS(String mensagem) throws IOException {
    	
    	Properties prop = Manipulador.getProp();
	    String accountSid = prop.getProperty("accountSid");
        String authToken = prop.getProperty("authToken");
        String fromPhoneNumber = prop.getProperty("numeroT");
        String toPhoneNumber = prop.getProperty("numero");
        
        String twilioUrl = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
        
        OkHttpClient client = new OkHttpClient();
        String credentials = Credentials.basic(accountSid, authToken);
        
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");
        String postData = "To=" + toPhoneNumber + "&From=" + fromPhoneNumber + "&Body=" + mensagem;
        
        RequestBody body = RequestBody.create(postData, JSON);

        Request request = new Request.Builder()
                .url(twilioUrl)
                .post(body)
                .addHeader("Authorization", credentials)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response;
        } 
    }
}