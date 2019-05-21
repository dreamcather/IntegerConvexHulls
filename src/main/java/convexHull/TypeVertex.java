package convexHull;

import org.jlinalg.rational.Rational;

public class TypeVertex {

    private int dimension;
    private Rational coordinates[];
    private int vnum;
    private boolean mark;
    private TypeVertex next;
    private TypeVertex prev;
    private boolean onhull;
    private Object duplicate;

    public TypeVertex(int dimension)
    {
        this.dimension = dimension;
        duplicate = null;
        onhull = false;
        mark = false;
        coordinates = new Rational[dimension];
    }

    public TypeVertex(TypeVertex tmp)
    {
        dimension =tmp.dimension;
        duplicate = tmp.duplicate;
        onhull = tmp.onhull;
        mark = tmp.mark;
        vnum = tmp.vnum;
        for (int i = 0; i < dimension; i++)
        {
            coordinates[i] = tmp.coordinates[i];
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Rational[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Rational coordinates,int number) {
        this.coordinates[number] = coordinates;
    }

    public int getVnum() {
        return vnum;
    }

    public void setVnum(int vnum) {
        this.vnum = vnum;
    }

    public boolean getMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public TypeVertex getNext() {
        return next;
    }

    public void setNext(TypeVertex next) {
        this.next = next;
    }

    public TypeVertex getPrev() {
        return prev;
    }

    public void setPrev(TypeVertex prev) {
        this.prev = prev;
    }

    public boolean isOnhull() {
        return onhull;
    }

    public void setOnhull(boolean onhull) {
        this.onhull = onhull;
    }

    public Object getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Object duplicate) {
        this.duplicate = duplicate;
    }

    void setProcessed(boolean tmp)
    {
        mark = tmp;
    }
}
