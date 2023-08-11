package br.com.senai.core.util.api;

import java.io.IOException;
import java.util.Properties;

import br.com.senai.core.util.properties.Manipulador;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnviarEmail {
	
    public static final MediaType JSON = MediaType.get("application/json");
    
    public static Response enviarEmail(String mensagem, boolean isAlteracao) throws IOException {
    	
    	Properties prop = Manipulador.getProp();
	    String adminEmail = prop.getProperty("emailAdministrador");

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.sendgrid.com/v3/mail/send";
        String token = prop.getProperty("token");
        String subject = isAlteracao ? "Ei! Alguém mexeu em um restaurante." : "Eba! Tem um novo restaurante no sistema.";
        String body = String.format(
            "{\"personalizations\":[{\"to\":[{\"email\":\"%s\",\"name\":\"Eduarda Manenti\"}],\"subject\":\"%s\"}],"
            + "\"content\": [{\"type\": \"text/html\", \"value\": \"%s\"}],"
            + "\"from\":{\"email\":\"dudamanenti65@gmail.com\",\"name\":\"Eduarda Manenti\"},"
            + "\"reply_to\":{\"email\":\"dudamanenti65@gmail.com\",\"name\":\"Eduarda Manenti\"}}",
            adminEmail, subject, mensagem
        );

        RequestBody requestBody = RequestBody.create(body, JSON);
        Request request = new Request.Builder().url(url).post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
        	System.out.println(response.isSuccessful());
        	 System.out.println("Response Code: " + response.code());
             System.out.println("Response Body: " + response.body().string());
            return response;
        } 
    }
    
}
