package database;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

// Esta clase permite crear la conexión con la base de datos
public class CConexion {

    // Importamos la librería para conectar con MySQL
    Connection conectar;

    // Variables que almacenan las credenciales
    String usuario = "root"; // Nombre de usuario de la base de datos
    String contrasenia = "test"; // Contraseña de la base de datos
    String db = "registrocompetencia"; // Nombre de la base de datos
    String ip = "localhost"; // Dirección IP del servidor de la base de datos
    String puerto = "3306"; // Puerto utilizado por la base de datos

    // Creamos una cadena de conexión
    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + db;

    // Método para establecer la conexión
    public Connection establecerconexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Cargamos el controlador de MySQL
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia); // Establecemos la conexión
        } catch (Exception e) {
            // En caso de error, se muestra un mensaje de error
            JOptionPane.showMessageDialog(null, "Problemas en la conexión: " + e.toString());
        }
        return conectar; // Devolvemos la conexión establecida
    }
}
