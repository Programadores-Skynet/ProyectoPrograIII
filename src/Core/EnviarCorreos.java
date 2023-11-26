package Core;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class EnviarCorreos {

    // Variables para el correo electrónico del remitente, contraseña y detalles del correo.
    private static String emailFrom = "testlabprogra@gmail.com";
    private static String passwordFrom = "rgpn yoqj mwql eaqs";
    private String emailTo;
    private String subject;
    private String content;
    private String nombreP;
    private int codigo;

    // Propiedades y configuraciones para la conexión SMTP.
    private Properties mProperties;
    private Session mSession;
    private MimeMessage mCorreo;

    // Método para establecer el código de verificación
    public void establecerCodigoVerificacion(String nombre, int codigo) {
        this.nombreP = nombre;
        this.codigo = codigo;
    }

    public void Correo(String correo) {
        mProperties = new Properties(); // Inicializar mProperties
        crearCorreo(correo);
        EnviarCorreo();
    }

    /**
     * Método para crear el contenido del correo electrónico.
     */
    private void crearCorreo(String emailPara) {
        // Obtiene los datos ingresados en la interfaz.
        emailTo = emailPara;
        subject = "Confirmación de inscripción al torneo";
        System.out.print("Code" + codigo);
        if (codigo != 0) {
            content = "Estimado/a " + nombreP + ".\n "
                    + " ¡Gracias por inscribirte en nuestro torneo! Esperamos que te encuentres bien. "
                    + " Tu inscripción se ha realizado con éxito y te damos la bienvenida al torneo. A continuación, te proporcionamos tu código de inscripción: "
                    + " Código de inscripción: " + codigo
                    + ".   Si tienes alguna pregunta o necesitas más información, no dudes en contactarnos. ¡Nos vemos en el torneo! "
                    + " Atentamente, Fernando Delgado & Daniel Brenes ";
        } else {
            content = "Estimado/a " + nombreP + ". "
                    + " ¡Gracias por inscribirte en nuestro torneo! Esperamos que te encuentres bien. "
                    + " Tu inscripción se ha realizado con éxito, te queremos contar que por politicas del campeonato, "
                    + "el usuario no puede participar en el torneo actual, pero queda registrado para un proximo torneo. ";
        }
        // Configura las propiedades para el envío SMTP a través de Gmail.
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");

        mSession = Session.getDefaultInstance(mProperties);

        try {
            // Crea un nuevo mensaje de correo electrónico.
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(subject);
            mCorreo.setText(content, "ISO-8859-1", "html");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    /**
     * Método para enviar el correo electrónico.
     */
    private void EnviarCorreo() {
        try {
            Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(emailFrom, passwordFrom);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();
            JOptionPane.showMessageDialog(null, "Correo Enviado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
