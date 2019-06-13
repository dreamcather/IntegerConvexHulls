package test;

import IntegerConvexHull.IntegerConvexHull;
import IntegerConvexHull.doubleDescriptionMethod.MatrixHelper;
import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegerConvexHullTest {

    @Test
    public void integerConvexHullInizializationTest1() {
        Matrix<Rational> aMatrix = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(-1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(-1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(-1)},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(1)}});
        Vector<Rational> bVector = new Vector<>(new Rational[]{Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(6), Rational.FACTORY.get(6), Rational.FACTORY.get(6)});


        IntegerConvexHull integerConvexHull = new IntegerConvexHull(6,aMatrix, bVector);

        List<Vector<Rational>> points = integerConvexHull.getPoints();
        Assert.assertTrue(true);


    }

    @Test
    public void integerConvexHullInizializationTest2() {
        Matrix<Rational> aMatrix = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(-1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(-1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(-1)},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(1)},
                {Rational.FACTORY.get(-1),Rational.FACTORY.get(-1),Rational.FACTORY.get(1)}});

        Vector<Rational> bVector = new Vector<>(new Rational[]{Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(6), Rational.FACTORY.get(6), Rational.FACTORY.get(6),Rational.FACTORY.get(4)});

        IntegerConvexHull integerConvexHull = new IntegerConvexHull(7,aMatrix, bVector);

        List<Vector<Rational>> points = integerConvexHull.getPoints();
        Assert.assertTrue(true);


    }

    @Test
    public void integerConvexHullTest1() {
        Matrix<Rational> aMatrix = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(-1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(-1), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(-1)},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(0), Rational.FACTORY.get(0)},
                {Rational.FACTORY.get(2),Rational.FACTORY.get(4),Rational.FACTORY.get(5)}});

        Vector<Rational> bVector = new Vector<>(new Rational[]{Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(2),Rational.FACTORY.get(6)});
        Matrix<Rational> res = new Matrix<>(new Rational[][]{{Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(0),Rational.FACTORY.one()},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(0), Rational.FACTORY.get(1),Rational.FACTORY.one()},
                {Rational.FACTORY.get(0), Rational.FACTORY.get(1), Rational.FACTORY.get(0),Rational.FACTORY.one()},
                {Rational.FACTORY.get(1), Rational.FACTORY.get(1), Rational.FACTORY.get(0),Rational.FACTORY.one()},
                {Rational.FACTORY.get(2), Rational.FACTORY.get(0), Rational.FACTORY.get(0),Rational.FACTORY.one()}});

        List<Vector<Rational>> resList = MatrixHelper.matrToList(res);

        IntegerConvexHull integerConvexHull = new IntegerConvexHull(5,aMatrix, bVector);

        List<Vector<Rational>> points = integerConvexHull.getPoints();
        boolean equal = MatrixHelper.isEqual(points, resList);
        Assert.assertTrue(equal);


    }
}
