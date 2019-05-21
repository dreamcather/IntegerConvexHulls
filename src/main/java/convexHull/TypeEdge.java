package convexHull;

public class TypeEdge {

    int dimension;
    Object[] adjface;
    TypeVertex[] endpts;
    Object newface;
    boolean deleted;
    TypeEdge next;
    TypeEdge prev;

    public TypeEdge(int dimension) {
        this.dimension = dimension;
        adjface = new Object[2];
        adjface[0] = null;
        adjface[1] = null;
        newface = null;
        endpts = new TypeVertex[this.dimension - 1];
        for (int i = 0; i < this.dimension - 1; i++) {
            endpts[i] = null;
        }
        deleted = false;
    }

    public TypeEdge(TypeEdge tmp) {
        dimension = tmp.dimension;
        adjface[0] = tmp.adjface[0];
        adjface[1] = tmp.adjface[1];
        newface = tmp.newface;
        endpts = new TypeVertex[dimension - 1];
        for (int i = 0; i < dimension - 1; i++) {
            endpts[i] = tmp.endpts[i];
        }
        deleted = tmp.deleted;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Object getAdjface(int number) {
        return adjface[number];
    }

    public void setAdjface(Object tmp_adjface,int number) {
        adjface[number] = tmp_adjface;
    }

    public TypeVertex getEndpts(int number) {
        return endpts[number];
    }

    public void setEndpts(TypeVertex endpt,int number) {
        endpts[number] = endpt;
    }

    public Object getNewface() {
        return newface;
    }

    public void setNewface(Object newface) {
        this.newface = newface;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public TypeEdge getNext() {
        return next;
    }

    public void setNext(TypeEdge next) {
        this.next = next;
    }

    public TypeEdge getPrev() {
        return prev;
    }

    public void setPrev(TypeEdge prev) {
        this.prev = prev;
    }
}
