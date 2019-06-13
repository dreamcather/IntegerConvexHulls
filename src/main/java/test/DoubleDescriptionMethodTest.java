package test;

import IntegerConvexHull.doubleDescriptionMethod.DoubleDescriptionMethod;
import IntegerConvexHull.doubleDescriptionMethod.MatrixHelper;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DoubleDescriptionMethodTest {

    @Test
    public void setTest(){
        Set<Integer> a = new HashSet<>();
        Set<Integer> b = new HashSet<>();
        a.add(1);
        a.add(2);
        a.add(3);
        b.add(3);
        b.add(4);
        b.add(5);

        a.retainAll(b);

        Assert.assertEquals(a.size(),1);
    }

    @Test
    public void vectorSortTest(){
        Vector<Rational> rationalVector1 = new Vector<>(new Rational[]{Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(2), Rational.FACTORY.get(4)});
        Vector<Rational> rationalVector2 = new Vector<>(new Rational[]{Rational.FACTORY.get(2), Rational.FACTORY.get(3), Rational.FACTORY.get(7), Rational.FACTORY.get(1)});
        Vector<Rational> rationalVector3 = new Vector<>(new Rational[]{Rational.FACTORY.get(1), Rational.FACTORY.get(4), Rational.FACTORY.get(6), Rational.FACTORY.get(3)});
        Vector<Rational> rationalVector4 = new Vector<>(new Rational[]{Rational.FACTORY.get(2), Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(5)});

        LinkedList<Vector<Rational>> vectors = new LinkedList<>();
        vectors.add(rationalVector1);
        vectors.add(rationalVector2);
        vectors.add(rationalVector3);
        vectors.add(rationalVector4);

        List<Vector<Rational>> sort = MatrixHelper.sort(vectors);
        Assert.assertTrue(true);
    }

    @Test
    public void doubleDescriptionInizialisationTest(){
        Matrix<Rational> rationalMatrix = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(-2), Rational.FACTORY.get(-2), Rational.FACTORY.get(-1), Rational.FACTORY.get(2)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)}});

        DoubleDescriptionMethod doubleDescriptionMethod = new DoubleDescriptionMethod(rationalMatrix);
        List<Vector<Rational>> points = doubleDescriptionMethod.getPoints();
        Assert.assertTrue(true);
    }

    @Test
    public void doubleDescriptionAddInequalityTest1(){
        Matrix<Rational> rationalMatrix = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(-2), Rational.FACTORY.get(-2), Rational.FACTORY.get(-1), Rational.FACTORY.get(2)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)}});

        DoubleDescriptionMethod doubleDescriptionMethod = new DoubleDescriptionMethod(rationalMatrix);
        doubleDescriptionMethod.addInequality(new Vector<>(new Rational[]{Rational.FACTORY.get(0),Rational.FACTORY.get(0),Rational.FACTORY.get(-1),Rational.FACTORY.get(1)}));
        doubleDescriptionMethod.addInequality(new Vector<>(new Rational[]{Rational.FACTORY.get(0),Rational.FACTORY.get(0),Rational.FACTORY.get(1),Rational.FACTORY.get(-1,2)}));
        List<Vector<Rational>> points = doubleDescriptionMethod.getPoints();
        Assert.assertTrue(true);
    }
}