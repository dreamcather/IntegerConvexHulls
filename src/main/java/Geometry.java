import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

public class Geometry {

    private static int gcd(int a, int b) {
        if (b == 0)
            return Math.abs(a);
        return gcd(b, a % b);
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

        Vector<Rational> res = new Vector<>(new Rational[] { A, B, C, D.negate() });
        res = res.divide(Rational.FACTORY.get(d));

        Vector<Rational> expandVector = new Vector<>(new Rational[]{res.getEntry(1),res.getEntry(2),res.getEntry(3),Rational.FACTORY.one()});

        Rational orientation = res.multiply(expandVector);
        if (orientation.getNumerator().intValue() < 0) {
            res=res.multiply(Rational.FACTORY.get(-1));
        }

        return res;
    }
}
