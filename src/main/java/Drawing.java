import java.util.*;

public class Drawing {

    private double width;
    private double height;
    private Map<String, Node> nodes;
    private List<Edge> edges;
    private boolean directed;
    private boolean showLabels;
    private int nodeSize;
    private double nodeSizeCM;

    public Drawing(double width, double height, boolean directed, boolean labels) {
        this.width = width;
        this.height = height;
        this.directed = directed;
        this.showLabels = labels;
        nodes = new HashMap<>();
        edges = new ArrayList<>();
    }

    public void addNode(double x, double y, String id, String label) {
        Node n = new Node(x, y, id, label);
        nodes.put(id, n);
    }

    public void addEdge(String from, String to) {
        Edge e = new Edge(from, to);
        edges.add(e);
    }

    public void addEdge(String from, String to, ControlPoint cp) {
        Edge e = new Edge(from, to, cp);
        edges.add(e);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public boolean isDirected() {
        return directed;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    public double getNodeSizeCM() {
        return nodeSizeCM;
    }

    public void setNodeSizeCM(double nodeSizeCM) {
        this.nodeSizeCM = nodeSizeCM;
    }

    public boolean getShowLabels() {
        return showLabels;
    }

    public void scale(double factor) {
        this.width = width * factor;
        this.height = height * factor;
        nodes.values().stream().forEach(n -> n.scale(factor));
        edges.forEach(e -> {
            if (e.getControlPoint() != null) {
                ControlPoint cp = e.getControlPoint();
                e.setControlPoint(new ControlPoint(cp.getX() * factor, cp.getY() * factor));
            }
        });
    }

    public void moveDown(double amount) {
        nodes.values().stream().forEach(n -> n.reposition(n.getX(), n.getY() + amount));
        edges.forEach(e -> {
            if (e.getControlPoint() != null) {
                ControlPoint cp = e.getControlPoint();
                e.setControlPoint(new ControlPoint(cp.getX(), cp.getY() + amount));
            }
        });
    }

    public void moveRight(double amount) {
        nodes.values().stream().forEach(n -> n.reposition(n.getX() + amount, n.getY()));
        edges.forEach(e -> {
            if (e.getControlPoint() != null) {
                ControlPoint cp = e.getControlPoint();
                e.setControlPoint(new ControlPoint(cp.getX() + amount, cp.getY()));
            }
        });
    }

    public void inverse() {
        nodes.values().forEach(n -> {
            n.reposition(n.getX(), height - n.getY());
        });
        edges.forEach(e -> {
            if (e.getControlPoint() != null) {
                ControlPoint cp = e.getControlPoint();
                e.setControlPoint(new ControlPoint(cp.getX(), height - cp.getY()));
            }
        });
    }

    public Drawing clone() {
        Drawing d = new Drawing(width, height, directed, showLabels);
        nodes.forEach((id, node) -> {
            d.addNode(node.getX(), node.getY(), id, node.getLabel());
        });
        edges.forEach(e -> {
            ControlPoint cp = null;
            if (e.getControlPoint() != null) {
                cp = new ControlPoint(e.getControlPoint().getX(), e.getControlPoint().getY());
            }
            d.addEdge(e.getNodeFrom(), e.getNodeTo(), cp);
        });
        d.setNodeSize(this.nodeSize);
        return d;
    }

    @Override
    public String toString() {
        return "Drawing{" +
                "width=" + width +
                ", height=" + height +
                ", nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
