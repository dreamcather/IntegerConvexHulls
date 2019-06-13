package IntegerConvexHull;

import IntegerConvexHull.doubleDescriptionMethod.MatrixHelper;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    private final String path;
    int dimension;
    int count;
    Matrix<Rational> aMatrix;
    Vector<Rational> bVector;

    public Input(String path) {
        this.path = path;
        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            dimension = Integer.valueOf(br.readLine());
            count = Integer.valueOf(br.readLine());
            Rational[][] resValues = new Rational[count][dimension + 1];
            int k=0;
            while ((strLine = br.readLine()) != null) {
                resValues[k] = vectorFromString(strLine);
                k++;
            }
            Matrix<Rational> resultMatrix = new Matrix<>(resValues);
            aMatrix = MatrixHelper.eraseLeft(resultMatrix,dimension);
            bVector =MatrixHelper.eraseRight(resultMatrix,1).getCol(1);
        } catch (IOException e) {
            System.out.println("Ошибка");
        }
    }

    Rational rationalFromString(String str) {
        String[] strMas = str.split("/");
        int numerator = Integer.valueOf(strMas[0]);
        int denumerator = Integer.valueOf(strMas[1]);
        return Rational.FACTORY.get(numerator, denumerator);
    }

    Rational[] vectorFromString(String str) {
        String[] strMas = str.split(" ");
        Rational[] res = new Rational[dimension + 1];
        for (int i = 0; i <= dimension; i++) {
            res[i] = rationalFromString(strMas[i]);
        }
        return res;
    }

    public Matrix<Rational> getaMatrix() {
        return aMatrix;
    }

    public Vector<Rational> getbVector() {
        return bVector;
    }

    public int getDimension() {
        return dimension;
    }
}
