public class Node {

    private double x;
    private double y;
    private String id;
    private String label;
    private boolean collides;

    public Node(double x, double y, String id, String label) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.label = label;
        this.collides = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getLabel() {
        return label;
    }

    public String getId() {
        return id;
    }

    public void scale(double factor) {
        this.x = x * factor;
        this.y = y * factor;
    }

    public void reposition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean collides() {
        return collides;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", label=" + label +
                '}';
    }
}
