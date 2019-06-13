package IntegerConvexHull;

import IntegerConvexHull.convexHull.ConvexHull;
import IntegerConvexHull.convexHull.TypeFace;
import IntegerConvexHull.convexHull.TypeVertex;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class RandomGenerator {
    private final int count;
    private TypeVertex[] vertices;
    private double rad;
    private ConvexHull convexHull;
    private double accuracy_d;
    private int accuracy_i;
    private Rational[][] Amas;
    private Rational[] Bvec;

    public RandomGenerator(int count, int rad) {
        this.count = count;
        accuracy_d = 10.0d;
        accuracy_i =10;
        vertices = new TypeVertex[count];
        this.rad = rad;
        generateVertexs();
        convexHull = new ConvexHull(Arrays.asList(vertices), 3);


    }

    private Rational doubleToRational(double tmp) {
        double up = tmp*accuracy_d;
        int numerator = (((Double) (up)).intValue());
        Rational rational = Rational.FACTORY.get(numerator,accuracy_i);
        return rational;
    }

    private TypeVertex generateVertex() {
        double phi = new Random().nextDouble() * Math.PI * 2;
        double alpha = new Random().nextDouble() * Math.PI * 2;
        double x_coord = Math.sin(phi) * Math.cos(alpha) * rad;
        double y_coord = Math.sin(phi) * Math.sin(alpha) * rad;
        double z_coord = Math.cos(phi) * rad;
        return new TypeVertex(3, doubleToRational(x_coord), doubleToRational(y_coord), doubleToRational(z_coord));
    }

    private void generateVertexs() {
        for (int i = 0; i < count; i++) {
            vertices[i] = generateVertex();
        }
    }

    private void writeString(String str, FileWriter writer) throws IOException {
        writer.write(str);
        writer.append('\n');

    }

    public Rational[][] getAmas() {
        return Amas;
    }

    public Rational[] getBvec() {
        return Bvec;
    }

    public void saveRestrictions(){
        TypeFace[] faces = convexHull.getFaces();
        try (FileWriter writer = new FileWriter("restriction.txt")) {
            int count = faces.length;
            Amas = new Rational[count][3];
            Bvec = new Rational[count];
            writeString(String.valueOf(faces.length), writer);
            writeString("", writer);

            for (int i = 0; i < faces.length; i++) {
                Vector<Rational> inequality = Geometry.getInequality(faces[i]);
                Amas[i][0] = inequality.getEntry(1);
                Amas[i][1] = inequality.getEntry(2);
                Amas[i][2] = inequality.getEntry(3);
                Bvec[i] = inequality.getEntry(4);
                String str = inequality.toString();
                writeString(str,writer);
            }

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    Vector<Rational>[] saveVertexMas(){
        Vector<Rational>[] vertex = convexHull.getVertex();
        Vector<Rational> [] res = new Vector[vertex.length];
        for(int i=0;i<vertex.length;i++){
            res[i] = new Vector<>(new Rational[]{
                    vertex[i].getEntry(1),vertex[i].getEntry(2),vertex[i].getEntry(3),Rational.FACTORY.one()
            });
        }
        return res;
    }

    public void saveVertex() {
        convexHull.save("output.txt");
        System.out.println(convexHull.vcol);
    }
}
