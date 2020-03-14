package itmo.gorshkov;

import itmo.gorshkov.linearSolver.GaussMethod;
import itmo.gorshkov.linearSolver.Matrix;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

    private static final String instruction = "Используйте: 1. Для ввода из файла: -f FILE_PATH [-n размерность]\n" +
            "2. Для ввода из консоли: -m\n" +
            "3. Для генерации случайной матрицы: -r [размерность]";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(instruction);
            System.exit(0);
        }
        try {
            Matrix matrix;
            switch (args[0]) {
                case "-f":
                    matrix = readFromFile(args);
                    break;
                case "-r":
                    matrix = generateMatrix(args);
                    break;
                case "-m":
                    matrix = enterMatrix();
                    break;
                default:
                    System.out.println(instruction);
                    System.exit(1);
                    matrix = null;
            }
            System.out.println("Исходная матрица:");
            outMatrix(matrix.getA());
            GaussMethod gauss = new GaussMethod(matrix);
            showResult(gauss);
        } catch (IllegalArgumentException e) {
            System.err.println("Матрица введена неверно");
            System.exit(1);
        } catch (IOException ee) {
            System.err.println("Ошибка чтения файла");
            System.exit(2);
        } catch (Exception eee) {
            System.err.println("Неизвестная ошибка");
            System.exit(3);
        }
    }

    private static Matrix enterMatrix() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность матрицы:");
        int n = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите коэффициенты матрицы через пробел, разделяя строки клавишей Enter:");
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lines.add(scanner.nextLine());
        }
        return validateAndFormat(n, lines);
    }

    private static Matrix readFromFile(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args[1]), StandardCharsets.UTF_8);
        int n;
        try {
            n = Integer.parseInt(args[3]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            n = Integer.parseInt(lines.get(0));
            lines.remove(0);
        }
        return validateAndFormat(n, lines);
    }

    private static Matrix validateAndFormat(int n, List<String> lines) throws IllegalArgumentException {
        try {
            double[][] a = new double[n][n + 1];
            for (int i = 0; i < n; i++) {
                Scanner scan = new Scanner(lines.get(i));
                for (int j = 0; j < n + 1; j++) {
                    String token = scan.next();
                    a[i][j] = Double.parseDouble(token);
                }
            }
            return new Matrix(n, a);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new IllegalArgumentException();
        }
    }

    private static Matrix generateMatrix(String[] arg) {
        int n;
        Random random = new Random();
        try {
            n = Integer.parseInt(arg[1]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            n = random.nextInt(18) + 3;
        }
        double[][] a = new double[n][n + 1];
        for (int i = 0; i < n; i++)
            for (int j = 0; j <= n; j++)
                a[i][j] = ((double) (Math.round((random.nextDouble() * 40d - 20d) * 1000))) / 1000;
        return new Matrix(n, a);
    }

    private static void showResult(GaussMethod gauss) {
        DecimalFormat format = new DecimalFormat("#.#####");
        System.out.println("\nОпределитель: d = " + format.format(gauss.getDeterminant()));
        System.out.println("\nТреугольная матрица: \n");
        outMatrix(gauss.getA());
        if (gauss.getDeterminant() != 0) {
            System.out.println("\nВектор неизвестных:");
            outArray(gauss.getX());
            System.out.println("\n\nВектор невязок:");
            for (double v: gauss.getR()) {
                System.out.print(v + "\t");
            }
            System.out.println();
        } else if (gauss.isJoint()) System.out.println("\nБесконечное множество решений\n");
        else System.out.println("\nСистема несовместна\n");

    }

    private static void outMatrix(double[][] a) {
        for (double[] doubles : a) {
            outArray(doubles);
            System.out.println();
        }
    }

    private static void outArray(double[] a) {
        for (double v : a) {
            DecimalFormat format = new DecimalFormat();
            format.setDecimalSeparatorAlwaysShown(false);
            System.out.printf("%10s", format.format(v));
        }
    }
}
