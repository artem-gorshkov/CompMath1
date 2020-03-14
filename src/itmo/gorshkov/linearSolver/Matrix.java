package itmo.gorshkov.linearSolver;

public class Matrix {
    private double[][] a;
    private int n;

    public Matrix() {
    }

    public Matrix(int n, double[][] a) {
        this.a = a;
        this.n = n;
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
}
