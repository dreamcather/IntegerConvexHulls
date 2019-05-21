import org.jlinalg.Matrix;
import org.jlinalg.rational.Rational;

public class MatrixHelper {

    public static Matrix<Rational> eraseLeft(Matrix<Rational> matrix, int count){
        int rowsCount = matrix.getRows();
        Rational[][] resMatrixValues = new Rational[rowsCount][count];
        for(int i=1;i<=rowsCount;i++){
            for(int j=1;j<=count;j++){
                resMatrixValues[i-1][j-1]=matrix.getRow(i).getEntry(j);
            }
        }
        return new Matrix<>(resMatrixValues);
    }

    public static Matrix<Rational> eraseRight(Matrix<Rational> matrix, int count){
        int rowsCount = matrix.getRows();
        int columnCount = matrix.getCols();
        Rational[][] resMatrixValues = new Rational[rowsCount][count];
        for(int i=1;i<=rowsCount;i++){
            for(int j=0;j<count;j++){
                resMatrixValues[i-1][j]=matrix.getRow(i).getEntry(columnCount-j);
            }
        }
        return new Matrix<>(resMatrixValues);
    }
}
