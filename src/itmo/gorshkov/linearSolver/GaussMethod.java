package itmo.gorshkov.linearSolver;

import itmo.gorshkov.linearSolver.exceptions.ZeroColumnException;

public class GaussMethod {
    private double[][] a;
    private int n;
    private double determinant;
    private double[] x;
    private double[] r;
    private boolean joint;

    public GaussMethod(Matrix matrix) {
        this.a = matrix.getA();
        this.n = matrix.getN();
        x = new double[n];
        r = new double[n];
        joint = true;
        this.comp();
    }

    private void comp() {
        int countChange = 0; //число перестановок рядов
        int leaderIndex = 0; //Столбец ведущего элемента

        //прямой ход
        for (int row = 0; row < n - 1; row++) {

            // перестановка строк, если ведущий элемент = 0
            while (a[row][leaderIndex] == 0) {
                try {
                    changeRows(row);
                    countChange++; //+1 перестановка рядов
                } catch (ZeroColumnException e) {
                    if (leaderIndex < n) leaderIndex++; //сдвиг ведущего элемента вправо
                    else break;
                }
            }

            for (int k = row + 1; k < n; k++) {
                double c = a[k][leaderIndex] / a[row][leaderIndex];
                a[k][leaderIndex] = 0;
                for (int j = leaderIndex + 1; j < n + 1; j++) {
                    a[k][j] = a[k][j] - c * a[row][j];
                }
            }
            if (leaderIndex < n) leaderIndex++;
            else break;
        }

        //вычисление определителя
        compDeterminantTriangle(countChange);

        if (determinant != 0)
            //обратный ход
            for (int i = n - 1; i > -1; i--) {
                double s = 0;
                int j;
                for (j = i + 1; j < n; j++) {
                    s += a[i][j] * x[j];
                }
                x[i] = (a[i][n] - s) / a[i][i];
                r[i] = a[i][i] * x[i] + s - a[i][n];
            }
        else if (a[n - 1][n] != 0) joint = false;
    }

    private void changeRows(int i) throws ZeroColumnException {
        try {
            int j = i;
            do {
                j++;
            } while (a[j][i] == 0);
            double[] t = a[j];
            a[j] = a[i];
            a[i] = t;
        } catch (IndexOutOfBoundsException e) {
            throw new ZeroColumnException();
        }
    }

    private void compDeterminantTriangle(int changeRows) {
        double res = 1;
        for (int i = 0; i < n; i++) {
            res *= a[i][i];
        }
        determinant = -1 * ~changeRows * res;
    }

    public double[][] getA() {
        return a;
    }

    public void setA(double[][] a) {
        this.a = a;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getDeterminant() {
        return determinant;
    }

    public void setDeterminant(double determinant) {
        this.determinant = determinant;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getR() {
        return r;
    }

    public void setR(double[] r) {
        this.r = r;
    }

    public boolean isJoint() {
        return joint;
    }

    public void setJoint(boolean joint) {
        this.joint = joint;
    }
}
