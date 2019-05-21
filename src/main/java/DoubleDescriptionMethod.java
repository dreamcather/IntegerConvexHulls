
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.*;

public class DoubleDescriptionMethod {
    private final Matrix<Rational> aMatrix;
    private final Vector<Rational> bVector;
    private final int dimension;
    private int inequalityCount;
    private Matrix<Rational> expandedMatrix;
    Matrix<Rational> bMatrix;
    List<Vector<Rational>> uList;
    List<Vector<Rational>> pList;
    List<Vector<Rational>> bList;

    public DoubleDescriptionMethod(Matrix<Rational> aMatrix, Vector<Rational> bVector, int dimension) {
        this.aMatrix = aMatrix;
        this.bVector = bVector;
        inequalityCount = aMatrix.getRows();
        this.dimension = dimension;
        uList = new LinkedList<>();
        initializeExpandedMatrix();
        initializeBMatrix();
        initializePList();
        step();
        System.out.println(pList);
    }

    private void initializePList() {
        pList = new LinkedList<>();
        uList.forEach(v -> {
            Vector<Rational> current = v.divide(v.getEntry(dimension + 1));
            pList.add(current);
        });
    }

    Vector<Rational> isRight() {
        initializePList();
        for (Vector<Rational> v : pList) {
            if (!isInteger(v))
                return v;
        }
        return null;
    }

    public void step() {
        Vector<Rational> p = isRight();
        while (p != null) {
            System.out.println("Remove " + p);
            Vector<Rational> cut = (generateCut(findAdjancent(p),p));
            addInequality(cut);
            initializePList();
            p = isRight();
            System.out.println(pList);
            System.out.println("+++++++++++++");
        }
    }

    private void initializeExpandedMatrix() {
        Rational[][] expandedMatrixValues = new Rational[inequalityCount][dimension + 1];
        for (int i = 0; i < inequalityCount; i++) {
            for (int j = 0; j < dimension; j++) {
                expandedMatrixValues[i][j] = aMatrix.get(i + 1, j + 1).negate();
            }
            expandedMatrixValues[i][dimension] = bVector.getEntry(i + 1);
        }
        expandedMatrix = new Matrix(expandedMatrixValues);
    }

    private boolean isInteger(Vector<Rational> vector) {
        for (int i = 1; i <= dimension + 1; i++) {
            if (vector.getEntry(i).getDenominator().intValue() != 1) {
                return false;
            }
        }
        return true;
    }

    public Vector<Rational> generateCut(Matrix<Rational> m,Vector<Rational> p) {
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
        int sumplay = A.det().abs().getDenominator().intValue();
        A =A.multiply(Rational.FACTORY.get(sumplay));
        A = A.transpose().multiply(Rational.FACTORY.get(-1));
        Vector<Rational> B = new Vector<>(rationals1).multiply(Rational.FACTORY.get(sumplay));
        Rational d = A.det().abs();
        Matrix<Rational> Adj = A.inverse().multiply(A.det());
        Rational[] rationals2 = new Rational[] { Rational.FACTORY.zero(), Rational.FACTORY.zero(),Rational.FACTORY.zero() };
        Vector<Rational>[] tmp = new Vector[3];
        for(int k=0;k<3;k++) {
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

        Vector<Rational> tt =p.multiply(Rational.FACTORY.get(-1));
        tt.set(4,Rational.FACTORY.one());
        Rational max = tmp[0].multiply(tt);
        int number=0;
        for(int i=1;i<3;i++){
            Rational curent = tmp[i].multiply(tt);
            if(curent.doubleValue()<max.doubleValue()){
                max =curent;
                number =i;
            }
        }
        return tmp[number];
    }

    public Matrix<Rational> findAdjancent(Vector<Rational> point) {
        int k = 0;
        Vector<Rational>[] vectors = new Vector[dimension];
        for (int i = 0; i < inequalityCount; i++) {
            Rational curentRes = bList.get(i).multiply(point);
            if (curentRes.isZero()) {
                vectors[k] = bList.get(i);
                k++;
            }
        }
        return new Matrix<>(vectors);
    }

    public Rational floor(Rational tmp) {
        return Rational.FACTORY.get((int) Math.floor(tmp.doubleValue()), 1);
    }

    Set<Integer> getZ(Vector<Rational> u) {
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
        Set a1 = getZ(v);
        Set a2 = getZ(u);
        a1.removeAll(a2);
        if (a1.size() == 1)
            return true;
        return false;
    }

    private void addInequality(Vector<Rational> vector) {
        List<Vector<Rational>> U_plus = new LinkedList<>();
        List<Vector<Rational>> U_minus = new LinkedList<>();
        List<Vector<Rational>> U_zero = new LinkedList<>();
        List<Vector<Rational>> U_supl = new LinkedList<>();
//        for(int i=1;i<=dimension;i++){
//            vector.set(i,vector.getEntry(i).negate());
//        }
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
    }

    private void initializeBMatrix() {
        bMatrix = set3(inequalityCount, expandedMatrix);
        bList = new LinkedList<>();
        for (int i = 0; i <= dimension; i++) {
            bList.add(bMatrix.getRow(i + 1));
        }
        Matrix<Rational> uMatrix = bMatrix.inverse();
        for (int i = 1; i <= dimension + 1; i++) {
            uList.add(uMatrix.getCol(i));
        }
    }

    private Matrix<Rational> matrFromList(Vector<Rational>... vectors) {
        return new Matrix<>(vectors);
    }

    Matrix<Rational> removeRows(Matrix<Rational> matr, int... numbers) {
        List<Integer> nums = new LinkedList<>();
        List<Vector<Rational>> res = new LinkedList<>();
        for (int i = 0; i < numbers.length; i++) {
            nums.add(numbers[i]);
        }
        for (int i = 1; i <= matr.getRows(); i++) {
            if (nums.indexOf(i) == -1) {
                res.add(matr.getRow(i));
            }
        }
        int k = res.size();
        Rational[][] resMas = new Rational[k][4];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 4; j++) {
                resMas[i][j] = res.get(i).getEntry(j + 1);
            }
        }
        if (resMas.length == 0)
            return null;
        return new Matrix<>(resMas);
    }

    public Matrix<Rational> set3(int max, Matrix<Rational> A) {
        for (int i = 1; i <= max - 3; i++) {
            for (int j = i + 1; j <= max - 2; j++) {
                for (int p = j + 1; p <= max - 1; p++) {
                    for (int l = p + 1; l <= max; l++) {
                        Matrix currentB = matrFromList(A.getRow(i), A.getRow(j), A.getRow(p), A.getRow(l));
                        if (currentB.rank() == 4) {
                            expandedMatrix = removeRows(expandedMatrix, i, j, p, l);
                            return currentB;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Matrix<Rational> set2(int max, Matrix<Rational> A) {
        for (int j = 1; j <= max - 2; j++) {
            for (int p = j + 1; p <= max - 1; p++) {
                for (int l = p + 1; l <= max; l++) {
                    Matrix currentB = matrFromList(A.getRow(j), A.getRow(p), A.getRow(l));
                    if (currentB.rank() == 3) {
                        expandedMatrix = removeRows(expandedMatrix, j, p, l);
                        return currentB;
                    }
                }
            }
        }
        return null;
    }
}