package calculadora;

import java.util.ArrayList;
import java.util.List;

public class LogicaCalculadora {
    private List<String> historial;

    public LogicaCalculadora() {
        historial = new ArrayList<>();
    }

    public void agregarAlHistorial(String expresion, String resultado) {
        historial.add(expresion + " = " + resultado);
    }

    public String obtenerHistorial() {
        StringBuilder cadenaHistorial = new StringBuilder();
        for (String operacion : historial) {
            cadenaHistorial.append(operacion).append("\n");
        }
        return cadenaHistorial.toString();
    }

    public List<Object> extraerNumerosYOperadores(String expresion) {
        String numero = "";
        List<Object> numerosYOperadores = new ArrayList<>();
        historial.add(expresion);

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (esOperadorCompleto(c) || c == '%') {
                if (!numero.isEmpty()) {
                    numerosYOperadores.add(Double.parseDouble(numero));
                    numero = "";
                }
                numerosYOperadores.add(c);
            } else {
                numero += c;
            }
        }

        if (!numero.isEmpty()) {
            numerosYOperadores.add(Double.parseDouble(numero));
        }

        return numerosYOperadores;
    }

    public double calcularResultado(List<Object> elementos) {
        elementos = calcularPorcentajes(elementos);
        elementos = calcularMultiplicacionYDivision(elementos);
        double resultado = calcularSumaYResta(elementos);
        return resultado;
    }

    private boolean esOperadorCompleto(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private List<Object> calcularPorcentajes(List<Object> elementos) {
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i) instanceof Character && (char) elementos.get(i) == '%') {
                double porcentaje = (double) elementos.get(i - 1);
                elementos.set(i - 1, porcentaje / 100);
                elementos.remove(i);
                i--;
            }
        }
        return elementos;
    }

    private List<Object> calcularMultiplicacionYDivision(List<Object> elementos) {
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i) instanceof Character) {
                char operador = (char) elementos.get(i);
                if (operador == '*' || operador == '/') {
                    double anterior = (double) elementos.get(i - 1);
                    double siguiente = (double) elementos.get(i + 1);
                    double resultado = operador == '*' ? anterior * siguiente : anterior / siguiente;
                    elementos.set(i - 1, resultado);
                    elementos.remove(i);
                    elementos.remove(i);
                    i--;
                }
            }
        }
        return elementos;
    }

    private double calcularSumaYResta(List<Object> elementos) {
        double resultado = (double) elementos.get(0);
        for (int i = 1; i < elementos.size(); i += 2) {
            char operador = (char) elementos.get(i);
            double siguiente = (double) elementos.get(i + 1);
            if (operador == '+') {
                resultado += siguiente;
            } else if (operador == '-') {
                resultado -= siguiente;
            }
        }
        return resultado;
    }
}
