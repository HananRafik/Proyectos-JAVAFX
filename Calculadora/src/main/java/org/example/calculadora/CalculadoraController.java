package org.example.calculadora;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculadoraController implements Initializable {

    @FXML
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    @FXML
    private Button btnC, btnCambioSigno, btnCientifico, btnCos, btnDecimal, btnDivicion;
    @FXML
    private Button btnIgual, btnLog, btnMultiplicacion, btnParenteces, btnPorcentaje;
    @FXML
    private Button btnRegistro, btnResta, btnSin, btnSuma, btnTan;
    @FXML
    private BorderPane layoutGeneral;

    @FXML
    private TextArea registroOperacione;
    @FXML
    private HBox panelBotones;
    @FXML
    private FlowPane panelDerecho, panelIzquierdo;
    @FXML
    private TextField textoOperaciones;

    private String operadorActual = "";
    private String operadorPrevio = "";
    private String ultimaOperacion = "";
    private boolean nuevoNumero = true;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        panelIzquierdo.setVisible(false);
        btnCientifico.setOnAction(event -> onButtonCientificaClick());

        panelDerecho.setVisible(false);
        btnRegistro.setOnAction(event -> onButtonRegistroClick());

        registroOperacione.clear();
    }






    @FXML
    public void handleButtonClick(Button button) {
        String textoBoton = button.getText();

        if ("C".equals(textoBoton)) {

            textoOperaciones.clear();
            operadorActual = "";
            operadorPrevio = "";
            ultimaOperacion = "";
            nuevoNumero = true;
        } else if ("=".equals(textoBoton)) {

            calcular();
        } else {

            if (esNumero(textoBoton)) {
                if (nuevoNumero) {
                    operadorActual = textoBoton;
                    nuevoNumero = false;
                } else {
                    operadorActual += textoBoton;
                }
            } else {
                if (!operadorActual.isEmpty()) {
                    if (!operadorPrevio.isEmpty() && !ultimaOperacion.isEmpty()) {
                        calcular();
                    }
                    operadorPrevio = operadorActual;
                    operadorActual = "";
                    ultimaOperacion = textoBoton;
                }
            }
            textoOperaciones.setText(operadorPrevio + " " + ultimaOperacion + " " + operadorActual);
        }
    }








    private boolean esNumero(String texto) {
        return texto.matches("[0-9]");
    }







    private void calcular() {
        if (ultimaOperacion.isEmpty()) {
            return;
        }

        double resultado;

        if (isOperacionUnaria(ultimaOperacion)) {

            double num = Double.parseDouble(operadorPrevio);
            resultado = realizarOperaciones(num);
            operadorActual = String.valueOf(resultado);

            String operacionCompleta = ultimaOperacion + "(" + operadorPrevio + ") = " + operadorActual;
            registroOperacione.appendText(operacionCompleta + "\n");

            operadorPrevio = operadorActual;
        } else {

            if (operadorPrevio.isEmpty() || operadorActual.isEmpty()) {
                return;
            }

            double num1 = Double.parseDouble(operadorPrevio);
            double num2 = Double.parseDouble(operadorActual);

            String operadorTemporal = ultimaOperacion;
            switch (ultimaOperacion) {
                case "+":
                    resultado = num1 + num2;
                    break;
                case "-":
                    resultado = num1 - num2;
                    break;
                case "X":
                    resultado = num1 * num2;
                    break;
                case "/":
                    if (num2 != 0) {
                        resultado = num1 / num2;
                    } else {
                        textoOperaciones.setText("Error: División por cero");
                        return;
                    }
                    break;
                default:
                    return;
            }

            operadorPrevio = String.valueOf(resultado);
            operadorActual = "";
            ultimaOperacion = "";


            textoOperaciones.setText(operadorPrevio);

            registroOperacione.appendText(num1 + " " + operadorTemporal + " " + num2 + " = " + resultado + "\n");
            nuevoNumero = true;
        }





        ultimaOperacion = "";
        nuevoNumero = true;
        textoOperaciones.setText(operadorPrevio);


    }





    private boolean isOperacionUnaria(String operacion) {
        return operacion.equals("sin") || operacion.equals("cos") || operacion.equals("tan") || operacion.equals("log") || operacion.equals("%");
    }





    private double realizarOperaciones(double num) {
        switch (ultimaOperacion) {
            case "sin":
                return Math.sin(Math.toRadians(num));
            case "cos":
                return Math.cos(Math.toRadians(num));
            case "tan":
                return Math.tan(Math.toRadians(num));
            case "log":
                return Math.log(num);
            case "%":
                return num / 100;
            default:
                return num;
        }
    }











    @FXML
    public void onButton1Click() {
        handleButtonClick(btn1);
    }

    @FXML
    public void onButton2Click() {
        handleButtonClick(btn2);
    }

    @FXML
    public void onButton3Click() {
        handleButtonClick(btn3);
    }

    @FXML
    public void onButton4Click() {
        handleButtonClick(btn4);
    }

    @FXML
    public void onButton5Click() {
        handleButtonClick(btn5);
    }

    @FXML
    public void onButton6Click() {
        handleButtonClick(btn6);
    }

    @FXML
    public void onButton7Click() {
        handleButtonClick(btn7);
    }

    @FXML
    public void onButton8Click() {
        handleButtonClick(btn8);
    }

    @FXML
    public void onButton9Click() {
        handleButtonClick(btn9);
    }

    @FXML
    public void onButton0Click() {
        handleButtonClick(btn0);
    }

    @FXML
    public void onButtonSumaClick() {
        handleButtonClick(btnSuma);
    }

    @FXML
    public void onButtonRestaClick() {
        handleButtonClick(btnResta);
    }

    @FXML
    public void onButtonMultiplicacionClick() {
        handleButtonClick(btnMultiplicacion);
    }

    @FXML
    public void onButtonDivicionClick() {
        handleButtonClick(btnDivicion);
    }

    @FXML
    public void onButtonIgualClick() {
        handleButtonClick(btnIgual);
    }

    @FXML
    public void onButtonCClick() {
        handleButtonClick(btnC);
    }
    public void onButtonDecimalClick() {
        if (nuevoNumero){
            operadorActual = "0.";
            nuevoNumero = false;

        }else {
            operadorActual += ".";
        }
        textoOperaciones.setText(operadorPrevio+ " " + ultimaOperacion + " " + operadorActual);
    }
    public void onButtonCambioSignoClick() {
        handleButtonClick(btnCambioSigno);
    }
    public void onButtonParentecesClick() {
        handleButtonClick(btnParenteces);
    }
    public void onButtonPorcentajeClick() {
        operadorPrevio = operadorActual;
        operadorActual = "";
        ultimaOperacion = "%";
        textoOperaciones.setText(operadorPrevio + "%");

    }


    public void onButtonSinClick() {
       operadorPrevio = operadorActual;
       operadorActual = "";
       ultimaOperacion = "sin";
       textoOperaciones.setText("sin(" + operadorPrevio + ")");
    }

    public void onButtonCosClick() {
        operadorPrevio = operadorActual;
        operadorActual = "";
        ultimaOperacion = "cos";
        textoOperaciones.setText("cos(" + operadorPrevio + ")");
    }

    public void onButtonTanClick() {
        operadorPrevio = operadorActual;
        operadorActual = "";
        ultimaOperacion = "tan";
        textoOperaciones.setText("tan(" + operadorPrevio + ")");
    }


    public void onButtonLogClick() {
        operadorPrevio = operadorActual;
        operadorActual = "";
        ultimaOperacion = "log";
        textoOperaciones.setText("log(" + operadorPrevio + ")");
    }


    public void onButtonCientificaClick() {
        panelIzquierdo.setVisible(!panelIzquierdo.isVisible());
    }

    public void onButtonRegistroClick() {
        panelDerecho.setVisible(!panelDerecho.isVisible());
    }


}