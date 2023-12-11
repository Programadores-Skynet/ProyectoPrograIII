package JuegoSnake;

import UI.Login;
import database.Ccontrol;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/**
 * @authors Daniel Brenes & Fernando Delgado
 */
public class PanelDelJuego extends JPanel implements ActionListener {

    //Definicion de constantes para el tamaño de la pantalla y las unidades del juego
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;
    //Arreglos para almacenar la posicion de la serpiente
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //Variables para el cuerpo de la serpiente, manzanas comidas, posicion de manzanas, direccion, estado de ejecucion y tiempo
    int bodyParts = 3;
    int manzanasComidas;
    int manzanasX;
    int manzanasY;
    char direction = 'R';
    boolean running = false;
    public String nombreJugador;
    public int puntu;
    private int tiempoTranscurrido = 0;
    private Timer tiempoJuego;
    Timer timer;
    Random random;

    //Constructor que recibe un objeto ccontrol
    PanelDelJuego(Ccontrol control) {
        //Inicializa el generador de numeros aleatorios
        random = new Random();
        //Establece las dimensiones preferidas del panel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        //Establece el color de fondo del panel
        this.setBackground(Color.DARK_GRAY);
        //Permite que el panel sea enfocable para eventos de teclado
        this.setFocusable(true);
        //Agrega un adaptador de teclado al pane
        this.addKeyListener(new MyKeyAdapter());
        //Metodo para iniciar el juego
        inicioJuego();
        //Metodo para iniciar el contador de tiempo del juego
        iniciarContadorTiempo();
    }

    //Metodo para iniciar el juego
    public void inicioJuego() {
        ManzanaN();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Metodo para pintar componentes en el panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dibujo(g);
    }

    //Metodo para dibujar los elementos del juego
    public void Dibujo(Graphics g) {
        if (running) {
            g.setColor(Color.white);
            g.fillOval(manzanasX, manzanasY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("RVV", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Puntaje:" + manzanasComidas, (SCREEN_WIDTH - metrics.stringWidth("Puntaje: " + manzanasComidas)) / 2, g.getFont().getSize());
            // Dibujar el tiempo transcurrido en la pantalla
            g.setColor(Color.yellow);
            g.setFont(new Font("RVV", Font.BOLD, 30));
            g.drawString("Tiempo: " + tiempoTranscurrido + " segundos", 10, 30);

        } else {
            //Llama al metodo finJuego para representar el final del juego
            finJuego(g);
        }
    }

    //Metodo para dibujar los elementos del juego
    public void ManzanaN() {
        //Genera posiciones aleatorias para la manzana dentro del área del juego
        manzanasX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        manzanasY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    //Metodo para los movimientos de la serpiente
    public void movimientos() {
        for (int i = bodyParts; i > 0; i--) {
            //Actualiza la posicion de cada parte del cuerpo de la serpiente basandose en su parte anterior
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        //Actualiza la posicion de la cabeza de la serpiente basandose en la dirección actual
        switch (direction) {
            case 'U' ->
                y[0] = y[0] - UNIT_SIZE;
            case 'D' ->
                y[0] = y[0] + UNIT_SIZE;
            case 'L' ->
                x[0] = x[0] - UNIT_SIZE;
            case 'R' ->
                x[0] = x[0] + UNIT_SIZE;
        }
    }

    //Metodo para verificar si la serpiente ha comido una manzana
    public void checkManzanas() {
        //Comprueba si la cabeza de la serpiente ha alcanzado la posicion de la manzana
        if ((x[0] == manzanasX) && (y[0] == manzanasY)) {
            //Aumenta la longitud de la serpiente al comer la manzana
            bodyParts++;
            //Incrementa el contador de manzanas comidas
            manzanasComidas++;
            //Genera una nueva posición para una manzana en el juego
            ManzanaN();
        }
    }

    //Metodo para verificar colisiones de la serpiente con si misma o los limites
    public void checkChoques() {
        //Verifica si la cabeza de la serpiente choca con alguna parte de su cuerpo
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                // Detiene el juego si la cabeza colisiona con el cuerpo
                running = false;
                // Llama al metodo para pausar el juego
                pausarJuego();
            }
        }
        //Verifica si la cabeza de la serpiente choca con los limites del area de juego
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            //Detiene el juego 
            running = false;
            pausarJuego();
        }
    }

    //Metodo para pausar el juego en caso de colision
    public void pausarJuego() {
        timer.stop(); //Detener el temporizador cuando hay una colision
        finJuego(getGraphics()); //Llamar al metodo para finalizar el juego
    }

    //Este metodo pasa la puntuacion de manzanas a la clase de base de datos
    public void pasarManzanas(int manzanas) {
        this.puntu = manzanas;
        Ccontrol clogin = new Ccontrol();
        clogin.puntuacion(puntu);
    }

    //Metodo para finalizar el juego
    public void finJuego(Graphics g) {
        //Puntos totales
        g.setColor(Color.white);
        g.setFont(new Font("RVV", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Puntaje: " + manzanasComidas, (SCREEN_WIDTH - metrics1.stringWidth("Puntaje: " + manzanasComidas)) / 2, g.getFont().getSize());
        //Cuando la persona pierde entonces:
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Su puntuacion es de: " + manzanasComidas, (SCREEN_WIDTH - metrics2.stringWidth("Usted ha perdido:-(")) / 2, SCREEN_HEIGHT / 2);

        detenerContadorTiempo();
        Ccontrol control = new Ccontrol();
        pasarManzanas(manzanasComidas);
        control.tiempoFinal(tiempoTranscurrido);
        control.impresion();

        //Cierra la ventana al finalizar el juego
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
            Login login = new Login();
            login.setVisible(true);
            login.cierreinscripcion();
        } else {
            //Forza un cierre del juego
            System.exit(0);
        }
    }

    //Metodo que se ejecuta con cada iteración del temporizador
    @Override
    public void actionPerformed(ActionEvent e) {
        //Verifica si el juego está en ejecucion
        if (running) {
            //Actualiza los movimientos de la serpiente
            movimientos();
            //Verifica si se ha comido una manzana
            checkManzanas();
            //Verifica colisiones con si misma o los límites 
            checkChoques();
        }
        //Vuelve a pintar el componente, lo que reflejará los cambios en la interfaz grafica
        repaint();
    }

    //Clase interna para manejar eventos del teclado
    public class MyKeyAdapter extends KeyAdapter {

        //Controla las acciones cuando se presionan teclas especificas
        public void keyPressed(KeyEvent e) {
            //Verifica cual tecla se ha presionado
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    //Si la direccion actual no es hacia la derecha, establece la direccion hacia la izquierda
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    //Si la direccion actual no es hacia la izquierda, establece la direccion hacia la derecha
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    //Si la direccion actual no es hacia abajo, establece la direccion hacia arriba
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    //Si la direccion actual no es hacia arriba, establece la direccion hacia abajo
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    //Metodo para controlar el tiempo transcurrido durante el juego
    public void iniciarContadorTiempo() {
        //Crea un nuevo temporizador que se ejecuta cada segundo
        tiempoJuego = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Incrementa el tiempo transcurrido en segundos
                tiempoTranscurrido++;
            }
        });
        // Inicia el temporizador para empezar a contar el tiempo
        tiempoJuego.start();
    }

    //Metodo para detener el contador de tiempo
    public void detenerContadorTiempo() {
        //Detiene el temporizador del tiempo transcurrido
        if (tiempoJuego != null) {
            //Detiene el temporizador
            tiempoJuego.stop();
        }
    }
}
