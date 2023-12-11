package Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Esta clase permite verficar que el participante que va a ingresar este en el orden previsto
public class GestionTorneo {

    public boolean verificarNombre(String nombre) {
        List<String> nombres = obtenerNombres();

        //Verificar si el nombre está en la lista de nombres
        if (nombres.contains(nombre)) {
            eliminarNombre(nombre, nombres);
            return true;
        } else {
            return false;
        }
    }

    //Obtiene los nombres de los participantes del txt
    private List<String> obtenerNombres() {
        List<String> nombres = new ArrayList<>();
        String archivo = "orden.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                //Separa los nombres por coma y los agrega a la lista
                nombres.addAll(Arrays.asList(linea.split(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nombres;
    }

    //Cuando el partipante ya inicio sesion este es borrado de la lista y del txt
    private void eliminarNombre(String nombre, List<String> nombres) {
        //Elimina el nombre de la lista
        nombres.remove(nombre);

        //Escribir la nueva lista al archivo
        escribirNombres(nombres);
    }

    //Cuando el partipante es borrado, se escribe en el txt los nombres de los demas participantes
    private void escribirNombres(List<String> nombres) {
        String archivo = "orden.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            //Escribir la lista de nombres en una línea separada por comas
            bw.write(String.join(",", nombres));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
