package IntegerConvexHull.doubleDescriptionMethod;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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

    public static Matrix<Rational> matrixFromList(List<Vector<Rational>> vectorList){
        int rowCount = vectorList.size();
        Rational[][] mas = new Rational[rowCount][4];
        for(int i =0;i<rowCount;i++){
            for(int j=0;j<4;j++){
                mas[i][j] = vectorList.get(i).getEntry(j+1);
            }
        }
        return new Matrix<>(mas);
    }

    public static List<Vector<Rational>> sort(List<Vector<Rational>> tmp){
        Comparator<Vector<Rational>> vectorComparator = new Comparator<Vector<Rational>>() {
            @Override
            public int compare(Vector<Rational> o1, Vector<Rational> o2) {
                Vector<Rational> subtract = o1.subtract(o2);
                for(int i=1;i<=4;i++){
                    if(!subtract.getEntry(i).isZero()){
                        if(subtract.getEntry(i).doubleValue()<0)
                            return -1;
                        else
                            return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        Collections.sort(tmp,vectorComparator);
        return tmp;
    }

    public static List<Vector<Rational>> matrToList(Matrix<Rational> matr){
        int size = matr.getRows();
        LinkedList<Vector<Rational>> res = new LinkedList<>();
        for(int i =1;i<=size;i++){
            res.add(matr.getRow(i));
        }
        return res;
    }

    public static boolean isEqual(List<Vector<Rational>> a, List<Vector<Rational>> b){
        if(a.size()!=b.size())
            return false;
        List<Vector<Rational>> sortA = sort(a);
        List<Vector<Rational>> sortB = sort(b);
        for(int i=0;i<sortA.size();i++){
            if(!a.get(i).subtract(sortB.get(i)).isZero())
                return false;
        }
        return true;
    }
}
