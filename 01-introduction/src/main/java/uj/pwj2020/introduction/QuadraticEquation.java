package uj.pwj2020.introduction;

public class QuadraticEquation {

    public double[] findRoots(double a, double b, double c) {
        double discriminant = b * b - 4. * a * c;

        if (discriminant > 0.) {
            double discrSqRoot = Math.sqrt(discriminant);
            return new double[]{(-b - discrSqRoot) / (2. * a), (-b + discrSqRoot) / (2. * a)};
        } else if (discriminant == 0.) {
            return new double[]{-b / (2. * a)};
        } else {
            return new double[]{};
        }
    }
}