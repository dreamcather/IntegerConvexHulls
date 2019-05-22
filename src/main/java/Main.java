import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;


public class Main {

    public static void main(String[] args) {


        Vector<Rational> first = new Vector<>(new Rational[] {
                Rational.FACTORY.get(-5, 1),
                Rational.FACTORY.get(1, 1),
                Rational.FACTORY.zero() });
        Vector<Rational> second = new Vector<>(new Rational[] {
                Rational.FACTORY.get(5, 1),
                Rational.FACTORY.get(1, 1),
                Rational.FACTORY.zero() });
        Vector<Rational> third = new Vector<>(new Rational[] {
                Rational.FACTORY.get(0, 1),
                Rational.FACTORY.get(-4, 1),
                Rational.FACTORY.zero() });
        Vector<Rational> four = new Vector<>(new Rational[] {
                Rational.FACTORY.get(0, 1),
                Rational.FACTORY.get(0, 1),
                Rational.FACTORY.get(4, 3) });

        Vector<Rational>[] a = new Vector[4];
        a[0] = (Geometry.getInequality(first, second, third, four));
        a[1] = (Geometry.getInequality(first, third, four, second));
        a[2] = (Geometry.getInequality(first, second, four, third));
        a[3] = (Geometry.getInequality(second, third, four,first));
        
        Rational[][] AMas = new Rational[4][3];
        Rational[] bVector = new Rational[4];
        for(int i=0;i<4;i++){
            for(int j=0;j<3;j++){
                AMas[i][j] = a[i].getEntry(j+1);
            }
            bVector[i] = a[i].getEntry(4);
        }
        DoubleDescriptionMethod doubleDescriptionMethod = new DoubleDescriptionMethod(new Matrix<>(AMas),
                                                                                      new Vector<>(bVector),3);
        

        // String path= "/Users/evginiy/Documents/IntegerConvexHull/input.txt";
        // Input input = new Input(path);
        // DoubleDescriptionMethod doubleDescriptionMethod = new DoubleDescriptionMethod(input.aMatrix, input.bVector,
        // input.dimension);

    }
}
