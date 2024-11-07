package calculadora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingConstants;

public class InterfazCalculadora {
    private JFrame marco;
    private JPanel panelVisualizador;
    private JPanel panelBotones;
    private JLabel etiquetaOperaciones;
    private List<JButton> botones;
    private LogicaCalculadora logica;

    public InterfazCalculadora() {
        marco = new JFrame("Calculadora");
        panelVisualizador = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones = new JPanel(new GridLayout(5, 4));
        etiquetaOperaciones = new JLabel("0");
        botones = new ArrayList<>();
        logica = new LogicaCalculadora();

        inicializarBotones();
        estilizarLabel();
    }

    public void mostrarInterfaz() {
        marco.setSize(300, 500);
        panelVisualizador.setPreferredSize(new Dimension(300, 100));
        panelBotones.setPreferredSize(new Dimension(300, 400));
        panelVisualizador.setBackground(new Color(30, 30, 30));

        panelVisualizador.add(etiquetaOperaciones);

        marco.add(panelVisualizador, BorderLayout.NORTH);
        marco.add(panelBotones, BorderLayout.CENTER);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);
    }

    private void inicializarBotones() {
        for (int i = 0; i < 20; i++) {
            JButton boton = new JButton("" + i);
            estilizarBotonNumerico(boton);
            botones.add(boton);
        }

        for (JButton boton : botones) {
            panelBotones.add(boton);
        }

        configurarBotones();
    }

    private void configurarBotones() {
        configurarBotonesEspeciales();
        configurarBotonesNumericos();
    }

    private void configurarBotonesEspeciales() {
        configurarBoton("AC", 0, e -> actualizarEtiquetaOperaciones("0"));
        configurarBoton("Borrar", 1, e -> borrarUltimoCaracter());
        configurarBoton("Historial", 2, e -> mostrarHistorial());
        configurarBoton(".", 18, e -> agregarComa());
        configurarIgual("=", 19, e -> calcularResultado());
        configurarBoton("%", 16, e -> agregarOperacion("%"));
        configurarBoton("+", 15, e -> agregarOperacion("+"));
        configurarBoton("/", 3, e -> agregarOperacion("/"));
        configurarBoton("*", 7, e -> agregarOperacion("*"));
        configurarBoton("-", 11, e -> agregarOperacion("-"));
    }

    private void configurarIgual(String texto, int indice, ActionListener accion){
        JButton boton = botones.get(indice);
        boton.setText(texto);
        boton.addActionListener(accion);
        estilizarBotonIgual(boton);
    }
    private void configurarBoton(String texto, int indice, ActionListener accion) {
        JButton boton = botones.get(indice);
        boton.setText(texto);
        boton.addActionListener(accion);
        estilizarBotonEspecial(boton);
    }

    private void configurarBotonesNumericos() {
        int contadorNumeros = 1;
        for (int i = 4; i <= 14; i++) {
            int valorActual = contadorNumeros;
            botones.get(i).setText("" + contadorNumeros);
            botones.get(i).addActionListener(e -> agregarNumero(valorActual));
            if (i == 6 || i == 10) i++;
            contadorNumeros++;
        }

        botones.get(17).setText("0");
        botones.get(17).addActionListener(e -> agregarNumero(0));
    }

    private void agregarNumero(int numero) {
        String cadena = etiquetaOperaciones.getText();
        if (cadena.equals("0")) {
            actualizarEtiquetaOperaciones("" + numero);
        } else {
            actualizarEtiquetaOperaciones(cadena + numero);
        }
    }
    private void agregarComa() {
        String cadena = etiquetaOperaciones.getText();
        if (!cadena.contains(".") || cadena.lastIndexOf('.') < cadena.lastIndexOf("+-*/")) {
            actualizarEtiquetaOperaciones(cadena + ".");
        }
    }


    private void actualizarEtiquetaOperaciones(String texto) {
        etiquetaOperaciones.setText(texto);
        ajustarTamañoFuente();
    }

    private void borrarUltimoCaracter() {
        String textoActual = etiquetaOperaciones.getText();
        if (textoActual.length() > 1) {
            actualizarEtiquetaOperaciones(textoActual.substring(0, textoActual.length() - 1));
        } else {
            actualizarEtiquetaOperaciones("0");
        }
    }

    private void mostrarHistorial() {
        String historial = logica.obtenerHistorial();
        JOptionPane.showMessageDialog(null, historial);
    }

    private void calcularResultado() {
        String expresion = etiquetaOperaciones.getText();
        List<Object> elementos = logica.extraerNumerosYOperadores(expresion);
        double resultado = logica.calcularResultado(elementos);
        actualizarEtiquetaOperaciones(String.valueOf(resultado));
        logica.agregarAlHistorial(expresion, String.valueOf(resultado));
    }

    private void agregarOperacion(String operador) {
        String cadena = etiquetaOperaciones.getText();
        char ultimoCaracter = cadena.charAt(cadena.length() - 1);
        if ("*/+-".indexOf(ultimoCaracter) == -1) {
            actualizarEtiquetaOperaciones(cadena + operador);
        }
    }

    private void ajustarTamañoFuente() {
        int longitudTexto = etiquetaOperaciones.getText().length();
        int tamañoFuente = 30;
        if (longitudTexto > 10) tamañoFuente = 20;
        if (longitudTexto > 15) tamañoFuente = 16;
        if (longitudTexto > 20) tamañoFuente = 12;
        etiquetaOperaciones.setFont(new Font("Arial", Font.PLAIN, tamañoFuente));
    }

    private void estilizarLabel() {
        etiquetaOperaciones.setFont(new Font("Arial", Font.PLAIN, 30));
        etiquetaOperaciones.setForeground(Color.WHITE);
        etiquetaOperaciones.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    private void estilizarBotonNumerico(JButton boton) {
        boton.setBackground(new Color(55, 55, 55));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));

    }

    private void estilizarBotonEspecial(JButton boton) {
        boton.setBackground(new Color(63,70,89));
        boton.setForeground(new Color(191,209,255));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
    }

    public static void estilizarBotonIgual(JButton boton) {
        boton.setBackground(new Color(176,198,255));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
    }

}
