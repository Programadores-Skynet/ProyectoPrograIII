package JuegoSnake;

import UI.Login;
import database.Clogin;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/**
 * @author Daniel Brenes & Fernando Delgado
 */
public class PanelDelJuego extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
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

    PanelDelJuego(Clogin login) {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        inicioJuego();
        iniciarContadorTiempo();
    }

    public void inicioJuego() {
        ManzanaN();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dibujo(g);
    }

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
            finJuego(g);
        }
    }

    public void ManzanaN() {
        manzanasX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        manzanasY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void movimientos() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
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

    public void checkManzanas() {
        if ((x[0] == manzanasX) && (y[0] == manzanasY)) {
            bodyParts++;
            manzanasComidas++;
            ManzanaN();
        }
    }

    public void checkChoques() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                pausarJuego();
            }
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
            pausarJuego();
        }
    }

    public void pausarJuego() {
        timer.stop(); // Detener el temporizador cuando hay una colisión
        finJuego(getGraphics()); // Llamar al método para finalizar el juego
    }

    /*  Estos metodos son Para tratar de pasar la puntuacion a la base de datos*/
    public void pasarManzanas(int manzanas) {
        this.puntu = manzanas;
        Clogin clogin = new Clogin();
        clogin.puntuacion(puntu);
    }

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
        Clogin clogin = new Clogin();
        pasarManzanas(manzanasComidas);
        clogin.tiempoFinal(tiempoTranscurrido);
        clogin.impresion();

        // Cierra la ventana al finalizar el juego
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
            Login login = new Login();
            login.setVisible(true);
        } else {
            //Forza un cierre del juego
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            movimientos();
            checkManzanas();
            checkChoques();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    public void iniciarContadorTiempo() {
        tiempoJuego = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoTranscurrido++;
                // Aquí puedes realizar acciones con el tiempo, como mostrarlo en pantalla o usarlo en tu juego
                System.out.println("Tiempo transcurrido: " + tiempoTranscurrido + " segundos");
            }
        });
        tiempoJuego.start();
    }

    public void detenerContadorTiempo() {
        if (tiempoJuego != null) {
            tiempoJuego.stop();
        }
    }
}
