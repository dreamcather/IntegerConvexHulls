package IntegerConvexHull.convexHull;

public class TypeFace {

    int dimension;
    TypeEdge[] edge;
    TypeVertex vertex[];
    boolean visible;
    TypeFace next;
    TypeFace prev;
    int info;

    public TypeFace(int dimension) {
        this.dimension =dimension;
        edge = new TypeEdge[dimension];
        vertex = new TypeVertex[dimension];
        for (int i = 0; i < dimension; i++) {
            edge[i] = null;
            vertex[i] = null;
        }
        next = null;
        prev = null;
        visible = false;
        info = 0;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public TypeEdge getEdge(int number) {
        return edge[number];
    }

    public void setEdge(TypeEdge tmp_edge,int number) {
        edge[number] = tmp_edge;
    }

    public TypeVertex getVertex(int number) {
        return vertex[number];
    }

    public void setVertex(TypeVertex tmp_vertex,int number) {
        vertex[number] = tmp_vertex;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public TypeFace getNext() {
        return next;
    }

    public void setNext(TypeFace next) {
        this.next = next;
    }

    public TypeFace getPrev() {
        return prev;
    }

    public void setPrev(TypeFace prev) {
        this.prev = prev;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }
}
