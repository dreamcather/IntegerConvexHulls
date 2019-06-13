package IntegerConvexHull.convexHull;

import org.jlinalg.Matrix;
import org.jlinalg.Vector;
import org.jlinalg.rational.Rational;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ConvexHull {
    private final int dimension;
    TypeVertex vstart = null;
    TypeEdge estart = null;
    TypeFace fstart = null;
    public int vcol = 0;
    int ecol = 0;
    int fcol = 0;

    private TypeVertex MakeNullVertex() {
        TypeVertex v = new TypeVertex(dimension);
        if (vcol == 0) {
            vstart = v;
            v.setVnum(1);
            v.setNext(v);
            v.setPrev(v);
            vcol = 1;
        } else {
            TypeVertex tmp = vstart.getPrev();
            tmp.setNext(v);
            v.setPrev(tmp);
            v.setNext(vstart);
            vstart.setPrev(v);
            vcol++;
            v.setVnum(vcol);
        }
        return v;
    }

    boolean Collinear3D(TypeVertex a, TypeVertex b, TypeVertex c) {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;

        Rational i_1_1 = b.getCoordinates()[1].subtract(a.getCoordinates()[1]);
        Rational i_1_2 = b.getCoordinates()[2].subtract(a.getCoordinates()[2]);
        Rational i_2_1 = c.getCoordinates()[1].subtract(a.getCoordinates()[1]);
        Rational i_2_2 = c.getCoordinates()[2].subtract(a.getCoordinates()[2]);
        Rational i_res = i_1_1.multiply(i_2_2).subtract(i_1_2.multiply(i_2_1));
        if (i_res.isZero()) {
            flag1 = true;
        }

        Rational j_1_1 = b.getCoordinates()[0].subtract(a.getCoordinates()[0]);
        Rational j_1_2 = b.getCoordinates()[2].subtract(a.getCoordinates()[2]);
        Rational j_2_1 = c.getCoordinates()[0].subtract(a.getCoordinates()[0]);
        Rational j_2_2 = c.getCoordinates()[2].subtract(a.getCoordinates()[2]);
        Rational j_res = j_1_1.multiply(j_2_2).subtract(j_1_2.multiply(j_2_1));
        if (j_res.isZero()) {
            flag2 = true;
        }

        Rational k_1_1 = b.getCoordinates()[0].subtract(a.getCoordinates()[0]);
        Rational k_1_2 = b.getCoordinates()[1].subtract(a.getCoordinates()[1]);
        Rational k_2_1 = c.getCoordinates()[0].subtract(a.getCoordinates()[0]);
        Rational k_2_2 = c.getCoordinates()[1].subtract(a.getCoordinates()[1]);
        Rational k_res = k_1_1.multiply(k_2_2).subtract(k_1_2.multiply(k_2_1));
        if (k_res.isZero()) {
            flag3 = true;
        }

        return flag1 && flag2 && flag3;
    }

    boolean Collinear2D(TypeVertex a, TypeVertex b) {
        boolean flag1 = false;
        boolean flag2 = false;

        if (b.getCoordinates()[1].subtract(a.getCoordinates()[1]).isZero())
            flag1 = true;

        if (b.getCoordinates()[0].subtract(a.getCoordinates()[0]).isZero())
            flag2 = true;

        return flag1 && flag2;
    }

    TypeEdge MakeNullEdge() {
        TypeEdge e = new TypeEdge(dimension);
        if (ecol == 0) {
            estart = e;
            e.setNext(e);
            e.setPrev(e);
            ecol = 1;
        } else {
            TypeEdge tmp = estart.getPrev();
            estart.setPrev(e);
            e.setPrev(tmp);
            e.setNext(estart);
            tmp.setNext(e);
            ecol++;
        }
        return e;
    }

    TypeFace MakeNullFace() {
        TypeFace f = new TypeFace(dimension);
        if (fcol == 0) {
            fstart = f;
            f.setNext(f);
            f.setPrev(f);
            fcol = 1;
        } else {
            TypeFace tmp = fstart.getPrev();
            tmp.setNext(f);
            f.setPrev(tmp);
            f.setNext(fstart);
            fstart.setPrev(f);
            fcol++;
        }
        return f;
    }

    TypeFace MakeFace(TypeVertex[] vertices, TypeFace fold) {
        TypeFace f;
        TypeEdge[] edges = new TypeEdge[dimension];
        if (fold == null) {
            for (int i = 0; i < dimension; i++)
                edges[i] = MakeNullEdge();
        } else {
            for (int i = 0; i < dimension; i++)
                edges[i] = fold.getEdge(dimension - 1);
        }
        if (dimension == 3) {
            edges[0].setEndpts(vertices[0], 0);
            edges[1].setEndpts(vertices[1], 0);
            edges[2].setEndpts(vertices[2], 0);
            edges[0].setEndpts(vertices[1], 1);
            edges[1].setEndpts(vertices[2], 1);
            edges[2].setEndpts(vertices[0], 1);
        } else {
            edges[0].setEndpts(vertices[0], 0);
            edges[1].setEndpts(vertices[1], 0);
        }

        f = MakeNullFace();
        for (int i = 0; i < dimension; i++) {
            f.setEdge(edges[i], i);
            f.setVertex(vertices[i], i);
        }
        for (int i = 0; i < dimension; i++) {
            edges[i].setAdjface(f, 0);
        }
        return f;

    }

    boolean VolumeSign(TypeFace f, TypeVertex p) {
        Rational[][] mas = new Rational[dimension + 1][dimension + 1];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                mas[i][j] = f.getVertex(i).getCoordinates()[j];
            }
            mas[i][dimension] = Rational.FACTORY.one();

        }

        for (int j = 0; j < dimension; j++) {
            mas[dimension][j] = p.getCoordinates()[j];
        }
        mas[dimension][dimension] = Rational.FACTORY.one();

        double vol = new Matrix<Rational>(mas).det().doubleValue();
        if (vol > 0.0005)
            return true;
        else return !(vol < -0.0005);
    }

    void Swap(TypeEdge x, TypeEdge y) {
        TypeEdge t = null;
        t = x;
        x = y;
        y = t;
    }

    void DoubleTriangle() {
        TypeVertex[] vertices = new TypeVertex[dimension + 1];
        TypeVertex t;
        TypeFace f0 = null;
        TypeFace f1 = null;
        vertices[0] = vstart;
        if (dimension == 3) {
            while (Collinear3D(vertices[0], vertices[0].getNext(), vertices[0].getNext().getNext())) {
                if ((vertices[0] = vertices[0].getNext()) == vstart) {
                    System.out.println("Error \n");
                    break;
                }
            }
        } else {
            while (Collinear2D(vertices[0], vertices[0].getNext())) {
                if ((vertices[0] = vertices[0].getNext()) == vstart) {
                    System.out.println("Error \n");
                    break;
                }
            }
        }
        for (int i = 1; i < dimension; i++) {
            vertices[i] = vertices[i - 1].getNext();
        }
        for (int i = 0; i < dimension; i++) {
            vertices[i].setProcessed(true);
        }
        TypeVertex[] up_vertices = new TypeVertex[dimension];
        TypeVertex[] down_vertices = new TypeVertex[dimension];
        for (int i = 0; i < dimension; i++) {
            up_vertices[i] = vertices[i];
            down_vertices[dimension - 1 - i] = vertices[i];
        }
        f0 = MakeFace(up_vertices, f1);
        f1 = MakeFace(down_vertices, f0);

        for (int i = 0; i < dimension; i++) {
            f0.getEdge(i).setAdjface(f1, 1);
            f1.getEdge(i).setAdjface(f0, 1);
        }
        vertices[dimension] = vertices[dimension - 1].getNext();
        boolean vol = VolumeSign(f1, vertices[dimension]);
        while (!vol) {
            if ((vertices[dimension] = vertices[dimension].getNext()) == vertices[0])
                System.out.println("Error \n");
            vol = VolumeSign(f1, vertices[dimension]);
        }
        vstart = vertices[dimension];

    }

    void MakeCcw(TypeFace f, TypeEdge e, TypeVertex p) {
        TypeFace fv;
        TypeFace adj0 = ((TypeFace) e.getAdjface(0));
        TypeFace adj1 = ((TypeFace) e.getAdjface(1));
        if (adj0.getVisible()) {
            fv = (TypeFace) e.getAdjface(0);
        } else {
            fv = (TypeFace) e.getAdjface(1);
        }
        int j = 0;
        while (fv.getVertex(j) != e.getEndpts(0)) {
            j++;
        }
        if (fv.getVertex((j + 1) % dimension) != e.getEndpts(1)) {
            f.setVertex(e.getEndpts(1), 0);
            f.setVertex(e.getEndpts(0), 1);
        } else {
            f.setVertex(e.getEndpts(0), 0);
            f.setVertex(e.getEndpts(1), 1);
            Swap(f.getEdge(1), f.getEdge(2));
        }
        f.setVertex(p, dimension - 1);
    }

    TypeFace MakeConeFace(TypeEdge e, TypeVertex p) {
        TypeEdge[] new_edge = new TypeEdge[2];
        TypeFace new_face;
        for (int i = 0; i < 2; i++) {
            if ((new_edge[i] = (TypeEdge) (e.getEndpts(i).getDuplicate())) == null) {
                new_edge[i] = MakeNullEdge();
                new_edge[i].setEndpts(e.getEndpts(i), 0);
                new_edge[i].setEndpts(p, 1);
                e.getEndpts(i).setDuplicate(new_edge[i]);
            }
        }
        new_face = MakeNullFace();
        new_face.setEdge(e, 0);
        new_face.setEdge(new_edge[0], 1);
        new_face.setEdge(new_edge[1], 2);
        MakeCcw(new_face, e, p);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (new_edge[i].getAdjface(j) == null) {
                    new_edge[i].setAdjface(new_face, j);
                    break;
                }
            }
        }
        return new_face;
    }

    boolean AddOne(TypeVertex p) {
        TypeFace f;
        TypeEdge e;
        TypeEdge temp;
        boolean vis = false;
        f = fstart;
        do {
            if (VolumeSign(f, p)) {
                f.setVisible(true);
                vis = true;
            }
            f = f.getNext();
        } while (f != fstart);
        if (!vis) {
            p.setOnhull(false);
            return false;
        }
        e = estart;
        do {
            if ((((TypeFace) e.getAdjface(0)).getVisible()) && (((TypeFace) e.getAdjface(1)).getVisible())) {
                e.setDeleted(true);
            } else {
                if ((((TypeFace) e.getAdjface(0)).getVisible()) || (((TypeFace) e.getAdjface(1)).getVisible())) {
                    e.setNewface(MakeConeFace(e, p));
                }
            }
            e = e.getNext();
            while ((e.getEndpts(0) == p) || (e.getEndpts(1) == p)) {
                e = e.getNext();
            }
        } while (e != estart);
        return true;
    }

    void DeleteVertex(TypeVertex v) {
        TypeVertex next = v.getNext();
        TypeVertex prev = v.getPrev();
        prev.setNext(next);
        next.setPrev(prev);
        if (v == vstart) {
            vstart = next;
        }
        vcol--;
    }

    void DeleteEdge(TypeEdge e) {
        TypeEdge next = e.getNext();
        TypeEdge prev = e.getPrev();
        prev.setNext(next);
        next.setPrev(prev);
        if (e == estart) {
            estart = next;
        }
        ecol--;
    }

    void DeleteFace(TypeFace f) {
        TypeFace next = f.getNext();
        TypeFace prev = f.getPrev();
        prev.setNext(next);
        next.setPrev(prev);
        if (f == fstart) {
            fstart = next;
        }
        fcol--;
    }

    void CleanFaces() {
        TypeFace f;
        TypeFace t;
        while (fstart.getVisible()) {
            DeleteFace(fstart);
        }
        f = fstart;
        do {
            if (f.getVisible()) {
                t = f;
                f = f.getNext();
                DeleteFace(t);
            } else {
                f = f.getNext();
            }
        } while (f != fstart);
    }

    void CleanEdges() {
        TypeEdge e;
        TypeEdge t;
        e = estart;
        do {
            if (e.getNewface() != null) {
                if (((TypeFace) e.getAdjface(0)).getVisible()) {
                    e.setAdjface(e.getNewface(), 0);
                } else {
                    e.setAdjface(e.getNewface(), 1);
                }
                e.setNewface(null);
            }
            e = e.getNext();
        } while (e != estart);
        while (estart.isDeleted()) {
            DeleteEdge(estart);
        }
        e = estart;
        do {
            if (e.isDeleted()) {
                t = e;
                e = e.getNext();
                DeleteEdge(t);
            } else {
                e = e.getNext();
            }
        } while (e != estart);
    }

    void CleanVertices() {
        TypeEdge e;
        TypeVertex v;
        TypeVertex t;
        e = estart;
        do {
            e.getEndpts(0).setOnhull(true);
            e.getEndpts(1).setOnhull(true);
            e = e.getNext();
        } while (e != estart);
        while ((vstart.getMark()) && (!vstart.isOnhull())) {
            DeleteVertex(vstart);
        }
        v = vstart;
        do {
            if ((v.getMark()) && (!v.isOnhull())) {
                t = v;
                v = v.getNext();
                DeleteVertex(t);
            } else {
                v = v.getNext();
            }
        } while (v != vstart);
        v = vstart;
        do {
            v.setDuplicate(null);
            v.setOnhull(false);
            v = v.getNext();
        } while (v != vstart);
    }

    void CleanUp() {
        CleanEdges();
        CleanFaces();
        CleanVertices();
    }

    void ConstructHull() {
        TypeVertex v;
        v = vstart;
        do {
            if (!v.getMark()) {
                v.setProcessed(true);
                AddOne(v);
                CleanUp();
                v = vstart;
            }
            v = v.getNext();
        } while (v != vstart);
    }

    void SavePr() {
        try (FileWriter writer = new FileWriter("notes3.stl", false)) {
            TypeFace f = fstart;
            writer.append('\n');
            do {
                writer.write("facet normal 0 0 0");
                writer.append('\n');
                writer.write("outer loop");
                writer.append('\n');
                writer.write("vertex " + f.getVertex(0).getCoordinates()[0].doubleValue() + " "
                        + f.getVertex(0).getCoordinates()[1].doubleValue() + " "
                        + f.getVertex(0).getCoordinates()[2].doubleValue());
                writer.append('\n');
                writer.write("vertex " + f.getVertex(1).getCoordinates()[0].doubleValue() + " "
                        + f.getVertex(1).getCoordinates()[1].doubleValue() + " "
                        + f.getVertex(1).getCoordinates()[2].doubleValue());
                writer.append('\n');
                writer.write("vertex " + f.getVertex(2).getCoordinates()[0].doubleValue() + " "
                        + f.getVertex(2).getCoordinates()[1].doubleValue() + " "
                        + f.getVertex(2).getCoordinates()[2].doubleValue());
                writer.append('\n');
                writer.write("endloop");
                writer.append('\n');
                writer.write("endfacet");
                writer.append('\n');
                f = f.next;
            } while (f != fstart);

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public TypeFace[] getFaces() {
        TypeFace f = fstart;
        TypeFace[] typeFaces = new TypeFace[fcol];
        int k = 0;
        do {
            typeFaces[k] = f;
            k++;
            f = f.next;
        } while (f != fstart);
        return typeFaces;
    }

    public Vector<Rational>[] getVertex() {
        TypeVertex v = vstart;
        Vector<Rational>[] typeVertex = new Vector[vcol];
        int k = 0;
        do {
            typeVertex[k] = v.toRational();
            k++;
            v = v.getNext();
        } while (v != vstart);
        return typeVertex;
    }

    private void writeString(String str, FileWriter writer) throws IOException {
        writer.write(str);
        writer.append('\n');

    }

    private void writeVertex(TypeVertex vertex, FileWriter writer) throws IOException {
        String res = vertex.getCoordinates()[0].doubleValue() + " " + vertex.getCoordinates()[1].doubleValue() +
                " " + vertex.getCoordinates()[2].doubleValue();
        writeString(res, writer);
    }

    private void writeFace(TypeFace face, FileWriter writer) throws IOException {
        writeVertex(face.vertex[0], writer);
        writeVertex(face.vertex[1], writer);
        writeVertex(face.vertex[2], writer);
        writeString("", writer);
    }

    public void save(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writeString(String.valueOf(fcol), writer);
            writeString("", writer);

            TypeFace f = fstart;

            do {
                writeFace(f, writer);
                f = f.next;
            } while (f != fstart);

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public ConvexHull(List<TypeVertex> vertices, int dimension) {

        this.dimension = dimension;
        vertices.forEach(v -> {
            TypeVertex vertex = MakeNullVertex();
            for (int i = 0; i < dimension; i++)
                vertex.setCoordinates(v.getCoordinates()[i], i);
        });
        DoubleTriangle();
        ConstructHull();
        SavePr();
    }

}
