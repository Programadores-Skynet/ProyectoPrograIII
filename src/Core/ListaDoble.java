package Core;

import java.io.*;

public class ListaDoble {

    private Nodo primero;
    private Nodo ultimo;

    public ListaDoble() {
        this.primero = null;
        this.ultimo = null;
        cargarDesdeArchivo();
    }

    //Agrega el nombre a la lista y luego lo guarda en el txt
    public void agregar(String nickname) {
        Nodo nuevo = new Nodo(nickname);

        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            ultimo = nuevo;
        }
        guardarEnArchivo();
    }

    //Guardar datos en el txt después de agregar un nuevo nickname
    private void guardarEnArchivo() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("orden.txt"));
            Nodo actual = primero;
            while (actual != null) {
                writer.write(actual.nickname);
                actual = actual.siguiente;
                if (actual != null) {
                    writer.write(",");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Cargar datos si existen en el txt 
    private void cargarDesdeArchivo() {
        try {
            File file = new File("orden.txt");

            if (file.exists() && file.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String linea = reader.readLine();
                reader.close();

                if (linea != null && !linea.isEmpty()) {
                    String[] nicknames = linea.split(",");
                    for (String nickname : nicknames) {
                        agregar(nickname.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Nodo {

        String nickname;
        Nodo siguiente;
        Nodo anterior;

        public Nodo(String nickname) {
            this.nickname = nickname;
            this.siguiente = null;
            this.anterior = null;
        }
    }
}
