import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner entradaUsuario = new Scanner(System.in);
        while (true) {
            System.out.println("Ingrese la operación a resolver: (o 'terminar' para finalizar el programa)");
            System.out.print("> ");
            String expresion = entradaUsuario.nextLine();

            if (expresion.equalsIgnoreCase("terminar")) {
                break;
            }

            try {
                List<String> elementos = dividirEnTokens(expresion);
                List<String> expresionPostfijo = convertirAPostfijo(elementos);
                double resultadoFinal = calcularPostfijo(expresionPostfijo);

                System.out.println("Resultado: " + resultadoFinal);
            } catch (Exception e) {
                System.out.println("Error en la expresión: " + e.getMessage());
            }
        }
        entradaUsuario.close();
    }

    public static List<String> convertirAPostfijo(List<String> elementosInfijo) {
        Stack<String> pilaOperadores = new Stack<>();
        List<String> salidaPostfijo = new ArrayList<>();

        for (String elemento : elementosInfijo) {
            if (esNumero(elemento)) {
                salidaPostfijo.add(elemento);
            } else if (elemento.equals("(")) {
                pilaOperadores.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pilaOperadores.isEmpty() && !pilaOperadores.peek().equals("(")) {
                    salidaPostfijo.add(pilaOperadores.pop());
                }
                pilaOperadores.pop();
            } else if (esOperador(elemento)) {
                while (!pilaOperadores.isEmpty() && obtenerPrioridad(elemento) <= obtenerPrioridad(pilaOperadores.peek())) {
                    salidaPostfijo.add(pilaOperadores.pop());
                }
                pilaOperadores.push(elemento);
            }
        }

        while (!pilaOperadores.isEmpty()) {
            salidaPostfijo.add(pilaOperadores.pop());
        }

        return salidaPostfijo;
    }

    public static double calcularPostfijo(List<String> elementosPostfijo) {
        Stack<Double> pilaOperandos = new Stack<>();

        for (String elemento : elementosPostfijo) {
            if (esNumero(elemento)) {
                pilaOperandos.push(Double.parseDouble(elemento));
            } else if (esOperador(elemento)) {
                double valorB = pilaOperandos.pop();
                double valorA = pilaOperandos.pop();
                switch (elemento) {
                    case "+":
                        pilaOperandos.push(valorA + valorB);
                        break;
                    case "-":
                        pilaOperandos.push(valorA - valorB);
                        break;
                    case "*":
                        pilaOperandos.push(valorA * valorB);
                        break;
                    case "/":
                        pilaOperandos.push(valorA / valorB);
                        break;
                    case "^":
                        pilaOperandos.push(Math.pow(valorA, valorB));
                        break;
                }
            }
        }

        return pilaOperandos.pop();
    }

    public static boolean esOperador(String elemento) {
        return "+-*/^".contains(elemento);
    }

    public static boolean esNumero(String elemento) {
        try {
            Double.parseDouble(elemento);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int obtenerPrioridad(String operador) {
        switch (operador) {
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    public static List<String> dividirEnTokens(String expresion) {
        StringTokenizer tokenizador = new StringTokenizer(expresion, "()+-*/^ ", true);
        List<String> listaTokens = new ArrayList<>();

        while (tokenizador.hasMoreTokens()) {
            String token = tokenizador.nextToken().trim();
            if (!token.isEmpty()) {
                listaTokens.add(token);
            }
        }

        return listaTokens;
    }
}
