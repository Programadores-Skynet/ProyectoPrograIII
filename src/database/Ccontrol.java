package database;

import Core.EnviarCorreos;
import Core.GestionTorneo;
import Core.ListaDoble;
import Core.Resultado;
import JuegoSnake.MenuPrincipal;
import UI.Login;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Ccontrol {

    //Variables privadas para el uso global
    private String nombreP;
    private int puntuaP;
    private int tiempoF;

    //Metodos de validar el usuario
    public void validarUsuario(String nickname, int codigoParticipacion) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Crear una instancia de la clase de conexión a la base de datos
            CConexion objetoCConexion = new CConexion();
            connection = objetoCConexion.establecerconexion(); // Establecer la conexión

            // Consulta SQL para buscar un usuario en la base de datos
            String consulta = "SELECT * FROM participaciones WHERE nickname = ? AND codigo_participacion = ?";
            ps = connection.prepareStatement(consulta);

            ps.setString(1, nickname); // Establecer el nickname en la consulta
            ps.setInt(2, codigoParticipacion); // Establecer el código de participación en la consulta
            rs = ps.executeQuery(); // Ejecutar la consulta

            if (rs.next()) {
                // Si se encuentra un usuario, mostrar un mensaje de usuario correcto
                JOptionPane.showMessageDialog(null, "El usuario es correcto");
                // Llamada al método comprobarIntento
                comprobarIntento(nickname);

            } else {
                // Si no se encuentra un usuario, mostrar un mensaje de usuario incorrecto
                JOptionPane.showMessageDialog(null, "El usuario es incorrecto, vuelva a intentar");
            }
        } catch (SQLException e) {
            // En caso de error en la base de datos, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            try {
                // Cerrar los recursos de base de datos en un bloque finally
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // En caso de error al cerrar recursos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
            }
        }
    }

    public void comprobarIntento(String nickname) {
        Login login = new Login();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            CConexion objetoCConexion = new CConexion();
            connection = objetoCConexion.establecerconexion();
            GestionTorneo gestionTorneo = new GestionTorneo(); // Instancia de la clase GestionTorneo

            String consulta = "SELECT puntuacion_final FROM participaciones WHERE nickname = ?";
            ps = connection.prepareStatement(consulta);
            ps.setString(1, nickname);
            rs = ps.executeQuery();

            if (rs.next()) {
                int puntuacion = rs.getInt("puntuacion_final");
                if (puntuacion == -1) {
                    JOptionPane.showMessageDialog(null, "El usuario puede participar");

                    // Verificar si el nickname está en la lista y eliminarlo si está presente
                    boolean nombreEnOrden = gestionTorneo.verificarNombre(nickname);

                    if (nombreEnOrden) {
                        JOptionPane.showMessageDialog(null, "El usuario está en el orden para participar");
                        // Abre el menú principal
                        MenuPrincipal menu = new MenuPrincipal();
                        menu.setNombreParticipante(nickname);
                        menu.show(true);
                    } else {
                        // Si el nombre no está en la lista
                        JOptionPane.showMessageDialog(null, "El usuario no está en el orden para participar");
                        login.show(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "El usuario ya ha participado");
                    login.show(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                login.show(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
            login.show(true);

        } finally {
            try {
                // Cerrar los recursos de base de datos en un bloque finally
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // En caso de error al cerrar recursos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
            }
        }
    }

    /**
     * ***************************************************************
     */
    //Metodos para registro de usuario
    
    public void registrarUsuarioNuevo(String nickname, String nombre, String apellido, String email, String residencia, int dia, String mes, int anio, int cedula, int codigoP) {
        // Verificar el formato del correo antes de proceder
        if (comprobacionCorreo(email) && verificarDatosNoRepetidos(nickname, email, cedula)) {
            Connection connection = null;
            PreparedStatement ps = null;
            int puntuacionF = 0;
            int tiempo = 0;
            if (codigoP == 0) {
                puntuacionF = -2;

            } else {
                puntuacionF = -1;
            }
            try {
                // Crear una instancia de la clase de conexión a la base de datos
                CConexion objetoCConexion = new CConexion();
                connection = objetoCConexion.establecerconexion(); // Establecer la conexión

                // Consulta SQL para insertar un nuevo usuario en la base de datos
                String consulta = "INSERT INTO participaciones (nickname, codigo_participacion, puntuacion_final, tiempo) VALUES (?, ?, ?, ?)";
                ps = connection.prepareStatement(consulta);
                ps.setString(1, nickname); // Establecer el nickname en la consulta
                ps.setInt(2, codigoP); // Establecer el codigo participacion en la consulta
                ps.setInt(3, puntuacionF); //Establece la puntiacion en -1 para hacer la comprobacion de los turnos
                ps.setInt(4, tiempo); //Establece el valor del tiempo

                int filasAfectadas = ps.executeUpdate(); // Ejecutar la consulta de inserción

                if (filasAfectadas > 0) {
                    // Si se insertó correctamente en la tabla "participaciones", continuar con la inserción en la tabla "usuarios"
                    consulta = "INSERT INTO usuarios (cedula, nombre, apellido, email, dia, mes, anio, lugar_residencia) VALUES (?,?, ?, ?, ?, ?, ?, ?)";
                    ps = connection.prepareStatement(consulta);
                    ps.setInt(1, cedula);
                    ps.setString(2, nombre); // Establecer el nombre en la consulta
                    ps.setString(3, apellido); // Establecer el apellido en la consulta
                    ps.setString(4, email); // Establecer el email en la consulta
                    ps.setInt(5, dia); // Establecer el día en la consulta
                    ps.setString(6, mes); // Establecer el mes en la consulta
                    ps.setInt(7, anio); // Establecer el año en la consulta
                    ps.setString(8, residencia); // Establecer la residencia en la consulta

                    filasAfectadas = ps.executeUpdate(); // Ejecutar la segunda consulta de inserción

                    if (filasAfectadas > 0) {
                        // Si se insertó correctamente en la tabla "usuarios", mostrar un mensaje de éxito
                        JOptionPane.showMessageDialog(null, "El usuario se ha registrado correctamente");
                        EnviarCorreos correo = new EnviarCorreos();
                        correo.establecerCodigoVerificacion(nombre, codigoP);
                        correo.Correo(email);

                        //Metodo de las listas
                        ListaDoble lista = new ListaDoble();
                        if (codigoP != 0) { //Si la persona tiene codigo 0 entonces no se agrega a la lista de participantes
                            lista.agregar(nickname);
                        }
                    } else {
                        // Si no se insertó correctamente en la tabla "usuarios", muestra un mensaje de error
                        JOptionPane.showMessageDialog(null, "Error al registrar el usuario en la tabla 'usuarios'");
                    }
                } else {
                    // Si no se insertó correctamente en la tabla "participaciones", mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "Error al registrar el usuario en la tabla 'participaciones'");
                }
            } catch (SQLException e) {
                // En caso de error en la base de datos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error: " + e.toString());
            } finally {
                try {
                    // Cerrar los recursos de base de datos en un bloque finally
                    if (ps != null) {
                        ps.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // En caso de error al cerrar recursos, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
                }
            }
        } else {
            // Mensaje de error si el correo no cumple con el formato básico o si ya está en uso
            JOptionPane.showMessageDialog(null, "El correo no cumple con el formato válido o ya está en uso");
        }
    }

    public boolean verificarDatosNoRepetidos(String nickname, String email, int cedula) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean datosNoRepetidos = false;

        try {
            // Crear una instancia de la clase de conexión a la base de datos
            CConexion objetoCConexion = new CConexion();
            connection = objetoCConexion.establecerconexion(); // Establecer la conexión

            // Consulta SQL para verificar si el nickname ya existe
            String consulta = "SELECT * FROM participaciones WHERE nickname = ?";
            ps = connection.prepareStatement(consulta);
            ps.setString(1, nickname); // Establecer el nickname en la consulta
            rs = ps.executeQuery(); // Ejecutar la consulta

            // Verificar si se encontró un resultado (nickname repetido)
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "El nickname ya está en uso, elige otro");
            } else {
                // Consulta SQL para verificar si el email ya existe
                consulta = "SELECT * FROM usuarios WHERE email = ?";
                ps = connection.prepareStatement(consulta);
                ps.setString(1, email); // Establecer el email en la consulta
                rs = ps.executeQuery(); // Ejecutar la consulta

                // Verificar si se encontró un resultado (email repetido)
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "El email ya está en uso, elige otro");
                } else {
                    // Consulta SQL para verificar si el email ya existe
                    consulta = "SELECT * FROM usuarios WHERE cedula = ?";
                    ps = connection.prepareStatement(consulta);
                    ps.setInt(1, cedula); // Establecer el email en la consulta
                    rs = ps.executeQuery(); // Ejecutar la consulta
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "El numero de cedula ya esta en uso");
                    } else {
                        // Si no se encontraron repeticiones, los datos no están repetidos
                        datosNoRepetidos = true;
                    }
                }
            }
        } catch (SQLException e) {
            // En caso de error en la base de datos, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            try {
                // Cerrar los recursos de base de datos en un bloque finally
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // En caso de error al cerrar recursos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
            }
        }

        return datosNoRepetidos;
    }

    public boolean comprobacionCorreo(String correo) {
        // Expresión regular para verificar el formato de un correo electrónico básico
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // Compilar la expresión regular en un patrón
        Pattern pattern = Pattern.compile(regex);

        // Crear un objeto Matcher para el correo dado
        Matcher matcher = pattern.matcher(correo);

        // Verificar si el correo coincide con el patrón (estructura básica de correo)
        return matcher.matches();
    }

    /**
     * ***************************************************************
     */
    //Metodos para asignacion de puntuacion y tiempo
    
    public void nombre(String nombre) {
        this.nombreP = nombre;
        System.out.println(nombreP); // Este print es opcional, puedes quitarlo si no es necesario

        // Guardar el nombre en un archivo de texto
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            writer.write(nombre); // Escribir el nombre en el archivo
            System.out.println("Nombre guardado en temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void puntuacion(int puntua) {
        this.puntuaP = puntua;
        System.out.println(puntuaP);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt", true))) {
            writer.write("," + puntua); // Agregar la puntuación al final del archivo, separada por coma
            System.out.println("Puntuacion guardada en temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tiempoFinal(int tiempo) {
        this.tiempoF = tiempo;
        System.out.println(tiempoF);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt", true))) {
            writer.write("," + tiempo); // Agregar la puntuación al final del archivo, separada por coma
            System.out.println("Tiempo Final guardado en temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrarArchivoTemporal() {
        try {
            File file = new File("temp.txt");
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Archivo 'temp.txt' eliminado correctamente.");
                } else {
                    System.out.println("No se pudo eliminar el archivo 'temp.txt'.");
                }
            } else {
                System.out.println("El archivo 'temp.txt' no existe.");
            }
        } catch (SecurityException ex) {
            System.err.println("No se tienen permisos para borrar el archivo: " + ex.getMessage());
        }

    }

    public void impresion() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("temp.txt"))) {
            String linea = bufferedReader.readLine(); // Leer la línea completa del archivo

            if (linea != null) {
                String[] datos = linea.split(","); // Separar los datos por coma
                String nombre = datos[0]; // Obtener el nombre
                int puntuacion = Integer.parseInt(datos[1]); // Obtener la puntuación como entero
                int tiempo = Integer.parseInt(datos[2]); // Obtener el tiempo como entero

                System.out.println("Nombre: " + nombre); // Imprimir el nombre en la consola
                System.out.println("Puntuacion: " + puntuacion); // Imprimir la puntuación en la consola
                System.out.println("Tiempo: " + tiempo); // Imprimir el tiempo en la consola
                guardarResultadosSnake(nombre, puntuacion, tiempo);

            } else {
                System.out.println("No se encontró ningún dato en el archivo.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Llamar al método para borrar el archivo
            borrarArchivoTemporal();
        }
    }

    public void guardarResultadosSnake(String nickname, int puntuacionManzanas, int tiempo) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            // Crear una instancia de la clase de conexión a la base de datos
            CConexion objetoCConexion = new CConexion();
            connection = objetoCConexion.establecerconexion(); // Establecer la conexión

            // Consulta SQL para actualizar la puntuación final y tiempo por nickname en la tabla "participaciones"
            String consulta = "UPDATE participaciones SET puntuacion_final = ?, tiempo = ? WHERE nickname = ?";
            ps = connection.prepareStatement(consulta);
            ps.setInt(1, puntuacionManzanas); // Establecer la nueva puntuación final en la consulta
            ps.setInt(2, tiempo); // Establecer el tiempo recorrido en la consulta
            ps.setString(3, nickname); // Establecer el nickname en la consulta

            int filasAfectadas = ps.executeUpdate(); // Ejecutar la consulta de actualización

            if (filasAfectadas > 0) {
                // Si se actualizó correctamente, mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(null, "La puntuación final y tiempo se actualizaron correctamente para " + nickname);
            } else {
                // Si no se actualizó correctamente, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al actualizar la puntuación final y tiempo para " + nickname);
            }
        } catch (SQLException e) {
            // En caso de error en la base de datos, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            try {
                // Cerrar los recursos de base de datos en un bloque finally
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // En caso de error al cerrar recursos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
            }
        }
    }

    /**
     * ***************************************************************
     */
    //Metodo de asignacion de puntuacion en caso de abandono
    public void abandonojuego(String nickname, int puntuacion) {
        Connection connection = null;
        PreparedStatement ps = null;

        puntuacion = 0;
        try {
            // Crear una instancia de la clase de conexión a la base de datos
            CConexion objetoCConexion = new CConexion();
            connection = objetoCConexion.establecerconexion(); // Establecer la conexión

            // Consulta SQL para actualizar la puntuación final por nickname en la tabla "participaciones"
            String consulta = "UPDATE participaciones SET puntuacion_final = ? WHERE nickname = ?";
            ps = connection.prepareStatement(consulta);
            ps.setInt(1, puntuacion); // Establecer la nueva puntuación final en la consulta
            ps.setString(2, nickname); // Establecer el nickname en la consulta

            int filasAfectadas = ps.executeUpdate(); // Ejecutar la consulta de actualización

            if (filasAfectadas > 0) {
                // Si se actualizó correctamente, mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(null, "La puntuación final se actualizó correctamente");
            } else {
                // Si no se actualizó correctamente, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al actualizar la puntuación final");
            }
        } catch (SQLException e) {
            // En caso de error en la base de datos, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            try {
                // Cerrar los recursos de base de datos en un bloque finally
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // En caso de error al cerrar recursos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.toString());
            }
        }
    }

    /**
     * ***************************************************************
     */
    //Metodos para asignar los puestos de resultados
    
    public ArrayList<Resultado> obtenerResultados() {
        ArrayList<Resultado> resultados = new ArrayList<>();

        try {
            CConexion objetoCConexion = new CConexion();
            Connection connection = objetoCConexion.establecerconexion();

            String consulta = "SELECT * FROM participaciones";
            PreparedStatement ps = connection.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nickname = rs.getString("nickname");
                int puntuacion = rs.getInt("puntuacion_final");
                int tiempo = rs.getInt("tiempo");

                Resultado resultado = new Resultado(nickname, puntuacion, tiempo);
                resultados.add(resultado);
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        return resultados;
    }

    public String obtenerNicknamePuntuacionMasAlta() {
        String nicknamePuntuacionMasAlta = "";

        try {
            CConexion objetoCConexion = new CConexion();
            Connection connection = objetoCConexion.establecerconexion();

            String consulta = "SELECT nickname FROM participaciones ORDER BY puntuacion_final DESC, tiempo_final ASC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nicknamePuntuacionMasAlta = rs.getString("nickname");
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return nicknamePuntuacionMasAlta;
    }
    /**
     * ***************************************************************
     */
}
