public class ControlPoint {

    private double x;
    private double y;

    public ControlPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "ControlPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
