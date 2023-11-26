package JuegoSnake;

import database.Clogin;
import javax.swing.JFrame;

public class FrameJuego extends JFrame {

    PanelDelJuego obj;

    FrameJuego(Clogin login) {
        obj = new PanelDelJuego(login);

        this.add(obj);
        this.setTitle("Juego Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
