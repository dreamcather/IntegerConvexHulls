package IntegerConvexHull;

import IntegerConvexHull.doubleDescriptionMethod.DoubleDescriptionMethod;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.LinkedList;
import java.util.List;

public class IntegerConvexHull {
    private final int inequalityCount;
    private int dimension=3;
    private final Matrix<Rational> aMatr;
    private final Vector<Rational> bVector;
    private Matrix<Rational> expandedMatrix;
    private List<Vector<Rational>> aList;
    private DoubleDescriptionMethod doubleDescriptionMethod;
    private GommoryCut gommoryCut = new GommoryCut();

    public IntegerConvexHull(int inequalityCount, Matrix<Rational> aMatr, Vector<Rational> bVector) {
        this.inequalityCount = inequalityCount;
        this.aMatr = aMatr;
        this.bVector = bVector;
        initializeExpandedMatrix();
        initializeBMatrix();
        step();
    }


    private void initializeExpandedMatrix() {
        Rational[][] expandedMatrixValues = new Rational[inequalityCount][dimension + 1];
        for (int i = 0; i < inequalityCount; i++) {
            for (int j = 0; j < dimension; j++) {
                expandedMatrixValues[i][j] = aMatr.get(i + 1, j + 1).negate();
            }
            expandedMatrixValues[i][dimension] = bVector.getEntry(i + 1);
        }
        expandedMatrix = new Matrix(expandedMatrixValues);
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

    private void initializeBMatrix() {
        Matrix<Rational> set3 = set3(inequalityCount, expandedMatrix);
        doubleDescriptionMethod = new DoubleDescriptionMethod(set3);
        aList = new LinkedList<>();
        for (int i = 0; i < expandedMatrix.getRows(); i++) {
            aList.add(expandedMatrix.getRow(i + 1));
        }
    }

    private void step(){
        while(aList.size()!=0){
            Vector<Rational> vector = aList.remove(0);
            doubleDescriptionMethod.addInequality(vector);
        }

        Vector<Rational> p = doubleDescriptionMethod.isRight();
        while (p != null) {
            Matrix<Rational> adjancent = doubleDescriptionMethod.findAdjancent(p);
            Vector<Rational> generateCut = gommoryCut.generateCut(adjancent, p);
            doubleDescriptionMethod.addInequality(generateCut);
            p=doubleDescriptionMethod.isRight();
        }

    }

    public List<Vector<Rational>> getPoints(){
        return doubleDescriptionMethod.getPoints();
    }
}
