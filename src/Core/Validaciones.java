package Core;

public class Validaciones {

    //Validacion para saber si el objeto es de tipo Int
    public boolean esInt(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Validacion para saber si el objeto es de tipo double
    public boolean esDouble(String x) {
        try {
            Double.parseDouble(x);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Validacion para saber si el objeto es de tipo float
    public boolean esFloat(String x) {
        try {
            Float.parseFloat(x);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
