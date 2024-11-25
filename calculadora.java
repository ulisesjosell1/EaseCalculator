import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Scanner;

// Clase principal que define una calculadora científica utilizando JFrame
public class calculadora extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField display; // Campo de texto para mostrar los cálculos
    private String operador = ""; // Operador seleccionado (+, -, *, /, ^)
    private double numero1 = 0; // Primer operando
    private boolean nuevaOperacion = true; // Bandera para limpiar el display al iniciar una nueva operación

    private String funcionUsuario = "sin"; // Función matemática por defecto

    // Botones que estarán disponibles en la calculadora
    private String[] botones = {
            "7", "8", "9", "+", "cos", "^", "C",
            "4", "5", "6", "-", "sen", "(", "±",
            "1", "2", "3", "*", "tan", ")", "∫",
            "=", "0", ".", "/", "x", "se", "dx"
    };

    static double h = 0.0001;  // Valor pequeño para aproximación numérica

    // Constructor que inicializa la calculadora
    public calculadora() {
        setTitle("Calculadora Científica"); // Título de la ventana
        setSize(350, 450); // Tamaño de la ventana
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuración del display de la calculadora
        display = new JTextField();
        display.setEditable(false); // No permite que el usuario escriba directamente
        display.setFont(new Font("SansSerif", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT); // Texto alineado a la derecha
        display.setBackground(new Color(245, 245, 245));
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // Configuración de los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 7, 5, 5)); // Disposición de botones en 4 filas y 7 columnas
        panelBotones.setBackground(new Color(255, 255, 255));

        // Crear y agregar los botones al panel
        for (String text : botones) {
            JButton button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.PLAIN, 18));
            button.setFocusPainted(false);
            button.setBackground(new Color(220, 220, 220));
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            button.addActionListener(this); // Agrega un ActionListener a cada botón
            panelBotones.add(button);
        }

        add(panelBotones, BorderLayout.CENTER); // Agregar panel de botones al centro
    }

    // Manejo de eventos cuando un botón es presionado
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand(); // Obtener el texto del botón presionado

        // Manejar botón "C" para limpiar la pantalla
        if (comando.equals("C")) {
            display.setText("");
            operador = "";
            numero1 = 0;
            nuevaOperacion = true;
        }
        // Manejar botón "±" para cambiar el signo del número en pantalla
        else if (comando.equals("±")) {
            if (!display.getText().isEmpty()) {
                try {
                    double valor = Double.parseDouble(display.getText());
                    valor *= -1;
                    display.setText(String.valueOf(valor));
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }
        }
        // Manejar el botón "=" para realizar cálculos
        else if (comando.equals("=")) {
            calcular();
            nuevaOperacion = true;
        }
        // Manejar operadores matemáticos básicos (+, -, *, /)
        else if (comando.equals("+") || comando.equals("-") || comando.equals("*") || comando.equals("/")) {
            if (!display.getText().isEmpty()) {
                try {
                    numero1 = Double.parseDouble(display.getText());
                    operador = comando;
                    nuevaOperacion = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }
        }
        // Manejar el operador de potencia "^"
        else if (comando.equals("^")) {
            if (!display.getText().isEmpty()) {
                try {
                    numero1 = Double.parseDouble(display.getText());
                    operador = "^";
                    nuevaOperacion = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }
        }
        // Manejar funciones trigonométricas
        else if (comando.equals("cos") || comando.equals("sen") || comando.equals("tan")) {
            aplicarFuncionTrigonometrica(comando);
        }
        // Manejar cálculo de integrales
        else if (comando.equals("∫")) {
            calcularIntegral();
        }
        // Manejar cálculo de derivadas
        else if (comando.equals("dx")) {
            calcularDerivada();
        }
        // Manejar solución de sistemas de ecuaciones
        else if (comando.equals("se")) {
            resolverSistemaEcuaciones();
        }
        // Manejar entrada de números y otros caracteres
        else {
            if (nuevaOperacion) {
                display.setText("");
                nuevaOperacion = false;
            }
            if (comando.equals(".") && display.getText().contains(".")) {
                return; // Evitar múltiples puntos decimales
            }
            display.setText(display.getText() + comando);
        }
    }

    // Método para realizar cálculos básicos de la calculadora
private void calcular() {
    // Verificar si hay un valor en el display y un operador seleccionado
    if (!display.getText().isEmpty() && !operador.isEmpty()) {
        try {
            // Convertir el texto del display a un número
            double numero2 = Double.parseDouble(display.getText());
            double resultado = 0;

            // Operar según el operador seleccionado
            switch (operador) {
                case "+":
                    resultado = numero1 + numero2; // Suma
                    break;
                case "-":
                    resultado = numero1 - numero2; // Resta
                    break;
                case "*":
                    resultado = numero1 * numero2; // Multiplicación
                    break;
                case "/":
                    // Verificar división por cero
                    if (numero2 != 0) {
                        resultado = numero1 / numero2; // División
                    } else {
                        // Mostrar error si se intenta dividir entre cero
                        display.setText("Error: División por cero");
                        return;
                    }
                    break;
                case "^":
                    resultado = Math.pow(numero1, numero2); // Potencia
                    break;
            }
            // Mostrar el resultado en el display
            display.setText(String.valueOf(resultado));
            operador = ""; // Reiniciar el operador
        } catch (NumberFormatException ex) {
            // Manejar errores en caso de entrada no válida
            display.setText("Error");
        }
    }
}

// Método para aplicar funciones trigonométricas
private void aplicarFuncionTrigonometrica(String funcion) {
    // Verificar si hay un valor en el display
    if (!display.getText().isEmpty()) {
        try {
            // Convertir el texto del display a un número
            double valor = Double.parseDouble(display.getText());
            double resultado = 0;

            // Aplicar la función trigonométrica según la opción seleccionada
            switch (funcion) {
                case "cos":
                    resultado = Math.cos(Math.toRadians(valor)); // Coseno
                    break;
                case "sen":
                    resultado = Math.sin(Math.toRadians(valor)); // Seno
                    break;
                case "tan":
                    resultado = Math.tan(Math.toRadians(valor)); // Tangente
                    break;
            }
            // Mostrar el resultado en el display
            display.setText(String.valueOf(resultado));
        } catch (NumberFormatException ex) {
            // Manejar errores en caso de entrada no válida
            display.setText("Error");
        }
    }
}

// Método para calcular integrales usando el método trapezoidal
private void calcularIntegral() {
    try {
        // Solicitar al usuario la función, límites de integración y número de intervalos
        funcionUsuario = JOptionPane.showInputDialog(this, "Ingresa la función (Ejemplo: 2*x^2):");
        double a = Double.parseDouble(JOptionPane.showInputDialog(this, "Ingresa el límite inferior:"));
        double b = Double.parseDouble(JOptionPane.showInputDialog(this, "Ingresa el límite superior:"));
        int n = 1000; // Número de intervalos predeterminado

        // Calcular la integral con el método trapezoidal
        double resultado = trapezoidalRule(a, b, n);
        // Mostrar el resultado en el display
        display.setText(String.format("∫ ≈ %.6f", resultado));
    } catch (NumberFormatException ex) {
        // Manejar errores en caso de entrada no válida
        display.setText("Syntax_Error");
    }
}

// Método auxiliar para evaluar una función polinómica simple
private double f(double x) {
    // Llamar a evaluar la función definida por el usuario
    return evaluarFuncion(funcionUsuario, x);
}

// Evaluar una función ingresada por el usuario en un valor x
private double evaluarFuncion(String funcion, double x) {
    try {
        // Reemplazar las ocurrencias de "x" con el valor dado
        funcion = funcion.replace("x", String.valueOf(x));

        // Evaluar la función utilizando un método personalizado
        return eval(funcion);
    } catch (Exception e) {
        // Manejar errores en caso de evaluaciones no válidas
        throw new RuntimeException("Error al evaluar la función: " + e.getMessage());
    }
}

// Método trapezoidal para calcular integrales
private double trapezoidalRule(double a, double b, int n) {
    double h = (b - a) / n; // Paso entre intervalos
    double sum = 0.5 * (f(a) + f(b)); // Suma inicial

    // Iterar a través de los puntos intermedios
    for (int i = 1; i < n; i++) {
        double x = a + i * h;
        sum += f(x); // Sumar valores de la función en los puntos intermedios
    }

    return sum * h; // Multiplicar la suma por el paso
}

// Método para calcular la derivada de una función
private void calcularDerivada() {
    // Solicitar información al usuario sobre la función a derivar
    String opcionStr = JOptionPane.showInputDialog(this, 
            "Elige una función de las siguientes opciones a derivar:\n" +
            "1. Ingresar una función con x de forma manual (ejemplo: 2*x^2 + 3*x + 1)\n" +
            "2. f(x) = sin(x)\n" +
            "3. f(x) = cos(x)\n" +
            "4. f(x) = exp(x) (e^x)\n" +
            "Introduce el número de la función:");
    
    int opcion = Integer.parseInt(opcionStr); // Opción de función seleccionada
    String funcion = "";

    if (opcion == 1) {
        // Si la opción es 1, pedir al usuario que ingrese la función
        funcion = JOptionPane.showInputDialog(this, "Ingrese la función de f(x):");
    }

    // Solicitar el valor de x donde se desea calcular la derivada
    String xStr = JOptionPane.showInputDialog(this, "Ingrese el valor de x en el cual quiere derivar:");
    double x = Double.parseDouble(xStr);

    // Calcular la derivada utilizando diferencias finitas centradas
    DecimalFormat df = new DecimalFormat("#.#####");
    double derivada = derivadaNewton(x, opcion, funcion);
    // Mostrar el resultado en el display
    display.setText("La derivada de f(x) en x = " + df.format(derivada));
    }

    // Método para calcular la derivada usando diferencias finitas centradas (Newton)
    public static double derivadaNewton(double x, int opcion, String funcion) {
        double f_xh = f(x + h, opcion, funcion);
        double f_xmh = f(x - h, opcion, funcion);
        return (f_xh - f_xmh) / (2 * h);
    }

    // Función f(x) con diferentes opciones para el usuario
    public static double f(double x, int opcion, String funcion) {
        switch (opcion) {
            case 1:
                // Aquí se evalúa la función personalizada que el usuario ingresó
                return evaluarFuncionPersonalizada(funcion, x);
            case 2:
                // f(x) = sin(x)
                return Math.sin(x);
            case 3:
                // f(x) = cos(x)
                return Math.cos(x);
            case 4:
                // f(x) = exp(x)
                return Math.exp(x);
            default:
                System.out.println("Opción no válida.");
                return 0;
        }
    }

    // Método para evaluar la función personalizada ingresada por el usuario
    public static double evaluarFuncionPersonalizada(String funcion, double x) {
        // Reemplazar "x" por el valor numérico ingresado en la función
        String funcionEvaluada = funcion.replace("x", Double.toString(x));

        // Evaluar la expresión utilizando el evaluador de expresiones
        try {
            return eval(funcionEvaluada);
        } catch (Exception e) {
            System.out.println("Error al evaluar la función personalizada.");
            return 0;
        }
    }

    // Evaluador simple de expresiones matemáticas con soporte para potencias
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Carácter inesperado: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // suma
                    else if (eat('-')) x -= parseTerm(); // resta
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplicación
                    else if (eat('/')) x /= parseFactor(); // división
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) { 
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { 
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch == '^') {
                    eat('^');
                    x = Math.pow(parseFactor(), 2);  // Elevar al cuadrado
                } else if (ch == -1) {  // Fin de la cadena
                    return 1;
                } else {
                    throw new RuntimeException("Carácter inesperado: " + (char) ch);
                }

                // Evaluar potencias (como x^2)
                while (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }.parse();
    }

    private void resolverSistemaEcuaciones() {
        Scanner sc = new Scanner(System.in);

        // Leer el número de ecuaciones
        String nStr = JOptionPane.showInputDialog(this, "Ingrese el número de ecuaciones:");
        int n = Integer.parseInt(nStr);

        // Matriz aumentada del sistema de ecuaciones
        double[][] augmentedMatrix = new double[n][n + 1];

        // Leer los coeficientes de las ecuaciones
        StringBuilder coeficientes = new StringBuilder("Ingrese los coeficientes de la matriz aumentada (incluyendo términos independientes):\n");
        for (int i = 0; i < n; i++) {
            String filaStr = JOptionPane.showInputDialog(this, coeficientes.toString() + "Ecuación " + (i + 1) + ":");
            String[] elementos = filaStr.split(" ");
            for (int j = 0; j <= n; j++) {
                augmentedMatrix[i][j] = Double.parseDouble(elementos[j]);
            }
        }

        // Aplicar eliminación de Gauss
        for (int i = 0; i < n; i++) {
            // Verificar si el pivote es cero
            if (augmentedMatrix[i][i] == 0) {
                JOptionPane.showMessageDialog(this, "El sistema no tiene solución única.");
                return;
            }

            // Hacer el pivote igual a 1
            double pivot = augmentedMatrix[i][i];
            for (int j = 0; j <= n; j++) {
                augmentedMatrix[i][j] /= pivot;
            }

            // Hacer ceros en la columna del pivote para las filas restantes
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j <= n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        // Extraer las soluciones
        double[] solution = new double[n];
        for (int i = 0; i < n; i++) {
            solution[i] = augmentedMatrix[i][n];
        }

        // Mostrar las soluciones
        StringBuilder solucionesStr = new StringBuilder("Las soluciones son:\n");
        for (int i = 0; i < n; i++) {
            solucionesStr.append("x").append(i + 1).append(" = ").append(solution[i]).append("\n");
        }
        JOptionPane.showMessageDialog(this, solucionesStr.toString());
    }

    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            calculadora calculadora = new calculadora();
            calculadora.setVisible(true);
        });
    }
}