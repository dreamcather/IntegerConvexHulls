package IntegerConvexHull.doubleDescriptionMethod;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.*;

public class DoubleDescriptionMethod {

    private int dimension = 3;
    private int inequalityCount;
    private List<Vector<Rational>> bList;
    private List<Vector<Rational>> uList;
    private List<Vector<Rational>> pList;

    public DoubleDescriptionMethod(Matrix<Rational> matrix) {
        bList = new LinkedList<>();
        uList = new LinkedList<>();
        for (int i = 1; i <= 4; i++)
            bList.add(matrix.getRow(i));

        Matrix<Rational> transpose = matrix.inverse();
        for (int i = 1; i <= 4; i++)
            uList.add(transpose.getCol(i));

    }

    private void initializePList() {
        pList = new LinkedList<>();
        for (Vector<Rational> v : uList) {
            Vector<Rational> current = v.divide(v.getEntry(dimension + 1));
            pList.add(current);
        }
    }

    public List<Vector<Rational>> getPoints() {
        initializePList();
        return pList;
    }

    Set<Integer> getZSet(Vector<Rational> u) {
        Set res = new HashSet();
        int k = bList.size();
        for (int i = 0; i < k; i++) {
            Vector<Rational> a = bList.get(i);
            if (a.multiply(u).isZero()) {
                res.add(i);
            }
        }
        return res;
    }

    boolean isAdjacent(Vector<Rational> v, Vector<Rational> u) {
        Set<Integer> a1 = getZSet(v);
        Set<Integer> a2 = getZSet(u);
        a1.retainAll(a2);
        if(a1.size()<=1){
            return false;
        }
        List<Vector<Rational>> adjancentList = new LinkedList<>();
        a1.forEach(i -> adjancentList.add(bList.get(i)));
        Matrix<Rational> rationalMatrix = MatrixHelper.matrixFromList(adjancentList);
        if (rationalMatrix.rank() == 2)
            return true;
        return false;
    }

    public void addInequality(Vector<Rational> vector) {
        List<Vector<Rational>> U_plus = new LinkedList<>();
        List<Vector<Rational>> U_minus = new LinkedList<>();
        List<Vector<Rational>> U_zero = new LinkedList<>();
        List<Vector<Rational>> U_supl = new LinkedList<>();
        uList.forEach(v -> {
            Rational r = vector.multiply(v);
            if (r.isZero()) {
                U_zero.add(v);
            } else {
                if (r.doubleValue() > 0) {
                    U_plus.add(v);
                } else {
                    U_minus.add(v);
                }
            }
        });
        U_plus.forEach(plus -> {
            U_minus.forEach(minus -> {
                if (isAdjacent(plus, minus)) {
                    Vector<Rational> vv = minus.multiply(vector.multiply(plus))
                            .subtract(plus.multiply(vector.multiply(minus)));
                    U_supl.add(vv);
                }
            });
        });
        uList = new LinkedList<>();
        U_plus.forEach(v -> uList.add(v));
        U_zero.forEach(v -> uList.add(v));
        U_supl.forEach(v -> uList.add(v));
        bList.add(vector);
        inequalityCount = bList.size();
        clear();
    }

    private boolean clearOne(Vector<Rational> vector) {
        int counter = 0;
        for (Vector<Rational> point : uList) {
            if (vector.multiply(point).equals(Rational.FACTORY.zero())) {
                counter++;
            }
        }
        if (counter < 3)
            return false;
        return true;

    }

    public Matrix<Rational> getCutMatrix(){
        Vector<Rational> right = isRight();
        if(right==null)
            return null;
        return findAdjancent(right);
    }

    public Matrix<Rational> findAdjancent(Vector<Rational> point) {
        int k = 0;
        Vector<Rational>[] vectors = new Vector[dimension];
        for (int i = 0; i < inequalityCount; i++) {
            Rational curentRes = bList.get(i).multiply(point);
            if (curentRes.isZero()) {
                vectors[k] = bList.get(i);
                k++;
                if (k == 3)
                    break;
            }
        }
        return new Matrix<>(vectors);
    }

    private boolean isInteger(Vector<Rational> vector) {
        for (int i = 1; i <= dimension + 1; i++) {
            if (vector.getEntry(i).getDenominator().intValue() != 1) {
                return false;
            }
        }
        return true;
    }

    public Vector<Rational> isRight() {
        initializePList();
        List<Vector<Rational>> noIntList = new LinkedList<>();
        for (Vector<Rational> v : pList) {
            if (!isInteger(v))
                noIntList.add(v);
        }
        if (noIntList.size() == 0)
            return null;
        int k = new Random().nextInt(noIntList.size());
        return noIntList.get(k);
    }

    private void clear() {
        boolean flag = false;
        do {
            flag = false;
            for (Vector<Rational> vector : bList) {
                if (!clearOne(vector)) {
                    flag = true;
                    bList.remove(vector);
                    inequalityCount--;
                    break;
                }
            }
        } while (flag);
    }


}
