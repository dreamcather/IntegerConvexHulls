package IntegerConvexHull;

import IntegerConvexHull.convexHull.ConvexHull;
import IntegerConvexHull.convexHull.TypeFace;
import IntegerConvexHull.convexHull.TypeVertex;
import IntegerConvexHull.doubleDescriptionMethod.MatrixHelper;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.*;

public class DoubleDescriptionMethod2 {
    private Matrix<Rational> aMatrix;
    private Vector<Rational> bVector;
    private int dimension;
    private int inequalityCount;
    private Matrix<Rational> expandedMatrix;
    private ConvexHull convexHull;
    Matrix<Rational> bMatrix;
    List<Vector<Rational>> uList;
    List<Vector<Rational>> pList;
    List<Vector<Rational>> bList;
    List<Vector<Rational>> aList;
    Vector<Rational>[] vertices;

    public DoubleDescriptionMethod2(Matrix<Rational> aMatrix, Vector<Rational> bVector, int dimension, Vector<Rational>[] vertices) {
        this.aMatrix = aMatrix;
        this.bVector = bVector;
        inequalityCount = aMatrix.getRows();
        this.dimension = dimension;
        this.vertices = vertices;
        uList = new LinkedList<>();
        initializeExpandedMatrix();
        //check();
        initializeBMatrix();
        addInequality();
        initializePList();
        System.out.println(pList.size());
        uList = Arrays.asList(vertices);
        step();
        initializePList();

    }

    private void check() {
        for (int i = 0; i < expandedMatrix.getRows(); i++) {
            for (int j = 0; j < vertices.length; j++) {
                System.out.print(expandedMatrix.getRow(i + 1).multiply(vertices[j]) + "   ");
            }
            System.out.println();
        }
    }

    public TypeFace[] VertexConverter() {
        List<TypeVertex> list = new LinkedList<>();
        pList.forEach(v -> {
            list.add(transfer(v));
        });
        convexHull = new ConvexHull(list, 3);
        TypeFace[] faces = convexHull.getFaces();
        return faces;

    }

    private void addInequality() {
        while (aList.size() != 0) {
            Vector<Rational> rationalVector = aList.get(0);
            addInequality(rationalVector);
            aList.remove(0);
            initializePList();
        }
    }


    private TypeVertex transfer(Vector<Rational> r) {
        return new TypeVertex(3, r.getEntry(1), r.getEntry(2), r.getEntry(3));
    }

    private void initializePList() {
        pList = new LinkedList<>();
        for (Vector<Rational> v : uList) {
            if (v.getEntry(dimension + 1).equals(Rational.FACTORY.zero())) {
                break;
            }
            Vector<Rational> current = v.divide(v.getEntry(dimension + 1));
            pList.add(current);
        }
    }


    Vector<Rational> isRight() {
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

    public void step() {
        Vector<Rational> p = isRight();
        while (p != null) {
            Matrix<Rational> adjancent = findAdjancent(p);
            Vector<Rational> cut = generateCut(adjancent, p);
            addInequality(cut);
            initializePList();
            System.out.println(pList);
            p = isRight();
            clear();

        }
    }

    private void printMatrix(Matrix<Rational> a) {
        System.out.println(a);
        System.out.println("**************************");
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

    private boolean clearOne(Vector<Rational> vector) {
        int counter = 0;
        for (Vector<Rational> point : pList) {
            if (vector.multiply(point).equals(Rational.FACTORY.zero())) {
                counter++;
            }
        }
        if (counter < 3)
            return false;
        return true;

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

    private boolean isInteger(Vector<Rational> vector) {
        for (int i = 1; i <= dimension + 1; i++) {
            if (vector.getEntry(i).getDenominator().intValue() != 1) {
                return false;
            }
        }
        return true;
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
        Set<Integer> a1 = getZ(v);
        Set<Integer> a2 = getZ(u);
        a1.retainAll(a2);
        List<Vector<Rational>> adjancentList = new LinkedList<>();
        a1.forEach(i -> adjancentList.add(bList.get(i)));
        Matrix<Rational> rationalMatrix = MatrixHelper.matrixFromList(adjancentList);
        if (rationalMatrix.rank() == 1)
            return true;
        return false;
    }

    private void addInequality(Vector<Rational> vector) {
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
    }

    private void initializeBMatrix() {
        bMatrix = set3(inequalityCount, expandedMatrix);
        bList = new LinkedList<>();
        aList = new LinkedList<>();
        for (int i = 0; i < expandedMatrix.getRows(); i++) {
            aList.add(expandedMatrix.getRow(i + 1));
        }
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
