package IntegerConvexHull;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GommoryCut {
    int dimension =3;
    private static int gcd(int a, int b) {
        if (a >= b) {
            if (b == 0)
                return Math.abs(a);
            return gcd(b, a % b);
        } else {
            if (a == 0)
                return Math.abs(b);
            return gcd(a, b % a);
        }
    }

    private static int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(b, a % b);
    }

    private int lcmList(List<Integer> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        int res = lcm(list.get(0), list.get(1));
        for (int i = 2; i < list.size(); i++) {
            res = lcm(res, list.get(i));
        }
        return res;
    }


    private int getDenominator(Matrix<Rational> matrix) {
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i <= matrix.getRows(); i++) {
            for (int j = 1; j <= matrix.getCols(); j++) {
                set.add(matrix.get(i, j).getDenominator().intValue());
            }
        }
        List<Integer> intList = new LinkedList<>();
        intList.addAll(set);

        return lcmList(intList);

    }

    public Rational floor(Rational tmp) {
        return Rational.FACTORY.get((int) Math.floor(tmp.doubleValue()), 1);
    }

    public Vector<Rational> generateCut(Matrix<Rational> m, Vector<Rational> p) {
        Rational[][] rationals = new Rational[dimension][dimension];
        Rational[] rationals1 = new Rational[dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                rationals[i][j] = m.get(i + 1, j + 1);
            }
        }
        for (int i = 0; i < dimension; i++) {
            rationals1[i] = m.get(i + 1, dimension + 1);
        }
        Matrix<Rational> A = new Matrix<>(rationals);
        int sumplay = getDenominator(A);
        A = A.multiply(Rational.FACTORY.get(sumplay));
        A = A.transpose().multiply(Rational.FACTORY.get(-1));
        Vector<Rational> B = new Vector<>(rationals1).multiply(Rational.FACTORY.get(sumplay));
        Rational d = A.det().abs();
        Matrix<Rational> Adj = A.inverse().multiply(A.det());
        Rational[] rationals2 = new Rational[]{
                Rational.FACTORY.zero(),
                Rational.FACTORY.zero(),
                Rational.FACTORY.zero()};
        Vector<Rational>[] tmp = new Vector[3];
        for (int k = 0; k < 3; k++) {
            rationals2[k] = Rational.FACTORY.one();
            Vector<Rational> e = new Vector<>(rationals2);
            Vector<Rational> t = Adj.multiply(e);
            for (int i = 0; i < dimension; i++) {
                while (t.getEntry(i + 1).doubleValue() < 0) {
                    t.set(i + 1, t.getEntry(i + 1).add(d));
                }
                while (t.getEntry(i + 1).doubleValue() >= d.doubleValue()) {
                    t.set(i + 1, t.getEntry(i + 1).subtract(d));
                }
            }

            Vector<Rational> multiply = A.multiply(t);
            Vector<Rational> a = multiply.divide(d);
            Rational b = B.multiply(t).divide(d);
            Rational[] res = new Rational[dimension + 1];
            for (int i = 0; i < dimension; i++) {
                res[i] = a.getEntry(i + 1);
            }
            res[dimension] = floor(b);
            tmp[k] = new Vector<>(res);
        }

        Vector<Rational> tt = p.multiply(Rational.FACTORY.get(-1));
        tt.set(4, Rational.FACTORY.one());
        Rational max = tmp[0].multiply(tt);
        int number = 0;
        for (int i = 1; i < 3; i++) {

            Rational curent = tmp[i].multiply(tt);
            if (curent.doubleValue() < max.doubleValue()) {
                max = curent;
                number = i;
            }
        }
        tmp[number].set(4, tmp[number].getEntry(4).negate());

        return tmp[number].multiply(Rational.FACTORY.get(-1));
    }
}
