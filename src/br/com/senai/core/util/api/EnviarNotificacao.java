package br.com.senai.core.util.api;

import java.io.IOException;
import java.util.Properties;

import br.com.senai.core.util.properties.Manipulador;

public class EnviarNotificacao {
    
	public static void enviarNotificacaoComTabela(String mensagemSMS, String mensagemEmail, boolean isAlteracao) throws IOException {
        Properties prop = Manipulador.getProp();
        String metodoNotificacao = prop.getProperty("metodoNotificacao");
        if ("SMS".equalsIgnoreCase(metodoNotificacao)) {
            EnviarSMS.enviarSMS(mensagemSMS);
        } else if ("email".equalsIgnoreCase(metodoNotificacao)) {
            EnviarEmail.enviarEmail(mensagemEmail, isAlteracao);
        } else {
            System.out.println("Método de notificação não configurado corretamente.");
        }
    }
}
