public class Edge {

    private String from;
    private String to;
    private ControlPoint cp;

    public Edge(String nodeFrom, String nodeTo) {
        this.from = nodeFrom;
        this.to = nodeTo;
        this.cp = null;
    }

    public Edge(String nodeFrom, String nodeTo, ControlPoint cp) {
        this.from = nodeFrom;
        this.to = nodeTo;
        this.cp = cp;
    }

    public String getNodeFrom() {
        return from;
    }

    public String getNodeTo() {
        return to;
    }

    public void setControlPoint(ControlPoint cp) {
        this.cp = cp;
        System.out.println("Control point set! " + cp);
    }

    public ControlPoint getControlPoint() {
        return cp;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
