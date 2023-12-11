package JuegoSnake;

import database.Ccontrol;
import javax.swing.JFrame;

public class FrameJuego extends JFrame {

    PanelDelJuego obj;

    //Constructor de la clase FrameJuego que recibe un objeto control como parámetro
    FrameJuego(Ccontrol control) {
        obj = new PanelDelJuego(control);

        //Configuración de la ventana JFrame
        this.add(obj); //Se añade el objeto PanelDelJuego a la ventana JFrame
        this.setTitle("Juego Snake"); //Se establece el título de la ventana como "Juego Snake"
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Se configura la acción al cerrar la ventana
        this.setResizable(false); //Se establece que la ventana no sea redimensionable
        this.pack(); //Se ajusta el tamaño de la ventana automáticamente para que se ajuste a sus componentes
        this.setVisible(true); //Se hace visible la ventana
        this.setLocationRelativeTo(null); //Se establece la ubicación de la ventana al centro de la pantalla
    }
}
