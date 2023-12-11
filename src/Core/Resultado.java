
package Core;

/**
*
* @authors Fernando Delgado & Daniel Brenes
*/
public class Resultado {

    private String nickname;
    private int puntuacion;
    private int tiempof;

    public Resultado(String nickname, int puntuacion, int tiempo) {
        this.nickname = nickname;
        this.puntuacion = puntuacion;
        this.tiempof = tiempo;
    }

    //Metodos para obtener y establecer los valores de los campos
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getTiempo() {
        return tiempof;
    }

    public void setNickname(int tiempo) {
        this.tiempof = tiempo;
    }
}
