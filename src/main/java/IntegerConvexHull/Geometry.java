package IntegerConvexHull;

import IntegerConvexHull.convexHull.TypeFace;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.Arrays;
import java.util.List;

public class Geometry {

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
        return Math.abs(a * b) / gcd(a, b);
    }

    private static int lcmList(List<Integer> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        int res = lcm(list.get(0), list.get(1));
        for (int i = 2; i < list.size(); i++) {
            res = lcm(res, list.get(i));
        }
        return res;
    }

    static Vector<Rational> getInequality(Vector<Rational> first,
                                          Vector<Rational> second,
                                          Vector<Rational> third,
                                          Vector<Rational> control) {
        Rational A_1 = first.getEntry(2).multiply(second.getEntry(3).subtract(third.getEntry(3)));
        Rational A_2 = second.getEntry(2).multiply(third.getEntry(3).subtract(first.getEntry(3)));
        Rational A_3 = third.getEntry(2).multiply(first.getEntry(3).subtract(second.getEntry(3)));
        Rational A = A_1.add(A_2.add(A_3));

        Rational B_1 = first.getEntry(3).multiply(second.getEntry(1).subtract(third.getEntry(1)));
        Rational B_2 = second.getEntry(3).multiply(third.getEntry(1).subtract(first.getEntry(1)));
        Rational B_3 = third.getEntry(3).multiply(first.getEntry(1).subtract(second.getEntry(1)));
        Rational B = B_1.add(B_2.add(B_3));

        Rational C_1 = first.getEntry(1).multiply(second.getEntry(2).subtract(third.getEntry(2)));
        Rational C_2 = second.getEntry(1).multiply(third.getEntry(2).subtract(first.getEntry(2)));
        Rational C_3 = third.getEntry(1).multiply(first.getEntry(2).subtract(second.getEntry(2)));
        Rational C = C_1.add(C_2.add(C_3));

        Rational D_1 = second.getEntry(2)
                .multiply(third.getEntry(3))
                .subtract(third.getEntry(2).multiply(second.getEntry(3)));
        Rational D_2 = third.getEntry(2)
                .multiply(first.getEntry(3))
                .subtract(first.getEntry(2).multiply(third.getEntry(3)));
        Rational D_3 = first.getEntry(2)
                .multiply(second.getEntry(3))
                .subtract(second.getEntry(2).multiply(first.getEntry(3)));

        D_1 = D_1.multiply(first.getEntry(1));
        D_2 = D_2.multiply(second.getEntry(1));
        D_3 = D_3.multiply(third.getEntry(1));
        Rational D = D_1.add(D_2.add(D_3));

        int d = gcd(A.getNumerator().intValue(),
                gcd(B.getNumerator().intValue(), gcd(C.getNumerator().intValue(), D.getNumerator().intValue())));

        Vector<Rational> res = new Vector<>(new Rational[]{A, B, C, D.negate()});
        res = res.divide(Rational.FACTORY.get(d));

        Vector<Rational> expandedVector = new Vector<>(new Rational[]{control.getEntry(1), control.getEntry(2), control.getEntry(3), Rational.FACTORY.one()});

        Rational orientation = res.multiply(expandedVector);
        if (orientation.getNumerator().intValue() < 0) {
            res = res.multiply(Rational.FACTORY.get(-1));
        }
        res.set(4, res.getEntry(4).negate());
        res = res.multiply(Rational.FACTORY.get(-1));

        Integer lcm = lcmList(Arrays.asList(res.getEntry(1).getDenominator().intValue(), res.getEntry(2).getDenominator().intValue(), res.getEntry(3).getDenominator().intValue(), res.getEntry(4).getDenominator().intValue()));

        res = res.multiply(Rational.FACTORY.get(lcm));

        return res;
    }

    static Vector<Rational> getInequality(TypeFace face) {
        Vector<Rational> first = face.getVertex(0).toRational();
        Vector<Rational> second = face.getVertex(1).toRational();
        Vector<Rational> third = face.getVertex(2).toRational();
        Rational A_1 = first.getEntry(2).multiply(second.getEntry(3).subtract(third.getEntry(3)));
        Rational A_2 = second.getEntry(2).multiply(third.getEntry(3).subtract(first.getEntry(3)));
        Rational A_3 = third.getEntry(2).multiply(first.getEntry(3).subtract(second.getEntry(3)));
        Rational A = A_1.add(A_2.add(A_3));

        Rational B_1 = first.getEntry(3).multiply(second.getEntry(1).subtract(third.getEntry(1)));
        Rational B_2 = second.getEntry(3).multiply(third.getEntry(1).subtract(first.getEntry(1)));
        Rational B_3 = third.getEntry(3).multiply(first.getEntry(1).subtract(second.getEntry(1)));
        Rational B = B_1.add(B_2.add(B_3));

        Rational C_1 = first.getEntry(1).multiply(second.getEntry(2).subtract(third.getEntry(2)));
        Rational C_2 = second.getEntry(1).multiply(third.getEntry(2).subtract(first.getEntry(2)));
        Rational C_3 = third.getEntry(1).multiply(first.getEntry(2).subtract(second.getEntry(2)));
        Rational C = C_1.add(C_2.add(C_3));

        Rational D_1 = second.getEntry(2)
                .multiply(third.getEntry(3))
                .subtract(third.getEntry(2).multiply(second.getEntry(3)));
        Rational D_2 = third.getEntry(2)
                .multiply(first.getEntry(3))
                .subtract(first.getEntry(2).multiply(third.getEntry(3)));
        Rational D_3 = first.getEntry(2)
                .multiply(second.getEntry(3))
                .subtract(second.getEntry(2).multiply(first.getEntry(3)));

        D_1 = D_1.multiply(first.getEntry(1));
        D_2 = D_2.multiply(second.getEntry(1));
        D_3 = D_3.multiply(third.getEntry(1));
        Rational D = D_1.add(D_2.add(D_3));

        int d = gcd(Math.abs(A.getNumerator().intValue()),
                gcd(Math.abs(B.getNumerator().intValue()), gcd(Math.abs(C.getNumerator().intValue()), Math.abs(D.getNumerator().intValue()))));

        Vector<Rational> res = new Vector<>(new Rational[]{A, B, C, D.negate()});
        res = res.divide(Rational.FACTORY.get(d));
        Integer lcm = lcmList(Arrays.asList(Math.abs(res.getEntry(1).getDenominator().intValue()), Math.abs(res.getEntry(2).getDenominator().intValue()), Math.abs(res.getEntry(3).getDenominator().intValue()), Math.abs(res.getEntry(4).getDenominator().intValue())));

        res = res.multiply(Rational.FACTORY.get(-lcm));
        res.set(4,res.getEntry(4).negate());

        return res;
    }
}
