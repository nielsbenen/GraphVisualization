import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.*;
import java.util.*;

public class DrawPanel extends JPanel {

    private int nodeSize;
    private Drawing d;
    private Map<Edge, Path2D> lines = new HashMap<>();
    private Map<Node, Ellipse2D> nodes = new HashMap<>();

    public DrawPanel() {

        setBackground(Color.WHITE);
        addMouseListener(new mouseListener());
        addMouseMotionListener(new mouseMotionListener());
    }

    public void setup(Drawing d) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        double w = dim.getWidth() * 0.7 - 100;
        double h = dim.getHeight() - 100;
        double factor = Math.min(w / d.getWidth(), h / d.getHeight());
        d.scale(factor);
        d.moveDown(10);
        //move to the middle
        double width = d.getWidth();
        d.moveRight((w - width) / 2 + 50);

        this.d = d;
        this.nodeSize = d.getNodeSize();
    }

    public Drawing getDrawing() {
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Map<String, Node> id2nodes = d.getNodes();
        //create nodes already so we can check
        id2nodes.values().forEach(n -> {
            n.setCollides(false);
            Ellipse2D.Double circle = new Ellipse2D.Double((int) n.getX() - nodeSize / 2, (int) n.getY()- nodeSize / 2, nodeSize, nodeSize);
            nodes.put(n, circle);
        });

        //draw edges
        d.getEdges().forEach(e -> {
            Path2D line = new Path2D.Double();
            double nodeFromX = id2nodes.get(e.getNodeFrom()).getX();
            double nodeFromY = id2nodes.get(e.getNodeFrom()).getY();
            double nodeToX = id2nodes.get(e.getNodeTo()).getX();
            double nodeToY = id2nodes.get(e.getNodeTo()).getY();

            line.moveTo(nodeFromX, nodeFromY);


            if (e.getControlPoint() == null) {
                //this only works for non bent lines sadly :(
                g2d.setColor(Color.BLACK);
                nodes.forEach((node, v) -> {
                    if (node.getId().equals(e.getNodeFrom()) || node.getId().equals(e.getNodeTo())) return;
                    double nodeX = node.getX();
                    double nodeY = node.getY();
                    Line2D l = new Line2D.Double(nodeFromX, nodeFromY, nodeToX, nodeToY);
                    if (l.ptSegDist(nodeX, nodeY) <= nodeSize / 2) {
                        node.setCollides(true);
                        g2d.setColor(Color.RED);
                    }
                });

                //arrow head
                if (d.isDirected()) {
                    Path2D.Double triangle = getArrowHead(d, nodeToX, nodeToY, nodeFromX, nodeFromY);
                    g2d.fill(triangle);
                }
                line.lineTo(nodeToX, nodeToY);
            } else {
                ControlPoint cp = e.getControlPoint();
                if (d.isDirected()) {
                    Path2D.Double triangle = getArrowHead(d, nodeToX, nodeToY, cp.getX(), cp.getY());
                    g2d.fill(triangle);
                }
                line.curveTo(nodeFromX, nodeFromY, cp.getX(), cp.getY(), nodeToX, nodeToY);

            }

            lines.put(e, line);
            g2d.draw(line);
        });

        //draw nodes
        id2nodes.values().forEach(n -> {
            Ellipse2D circle = nodes.get(n);
            g2d.setColor(Color.WHITE);
            g2d.fill(circle);
            g2d.setColor(n.collides() ? Color.RED : Color.BLACK);
            g2d.draw(circle);
            if (d.getShowLabels())
                g2d.drawString(n.getLabel(), (int) n.getX(), (int) n.getY());
        });
    }

    private Path2D.Double getArrowHead(Drawing d, double nodeToX, double nodeToY, double nodeFromX, double nodeFromY) {
        double diffx = nodeToX - nodeFromX;
        double diffy = nodeToY - nodeFromY;
        double length = Math.sqrt(Math.pow(diffx, 2) + Math.pow(diffy, 2));

        double xArrow = nodeToX - diffx * (d.getNodeSize() / 2 / length);
        double yArrow = nodeToY - diffy * (d.getNodeSize() / 2 / length);

        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0, 4);
        triangle.lineTo(-4, -4);
        triangle.lineTo(4, -4);
        triangle.closePath();

        //get the angle using trigonometry
        double angle = Math.atan2(yArrow - nodeFromY , xArrow - nodeFromX);
        AffineTransform tx = new AffineTransform();

        //move and rotate to the correct spot
        tx.setToIdentity();
        tx.translate(xArrow, yArrow);
        tx.rotate(angle-Math.PI/2d);
        triangle.transform(tx);
        return triangle;
    }

    Node clickedNode = null;
    Edge clickedLine = null;

    class mouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            nodes.forEach((node, circle) -> {
                if (circle.getBounds().contains(e.getPoint())) {
                    clickedNode = node;
                    return;
                }
            });

            lines.forEach((edge, path) -> {
                BasicStroke b = new BasicStroke();
                Shape boundary = b.createStrokedShape(path);
                PathIterator pi = boundary.getPathIterator(new AffineTransform());
                if (Path2D.intersects(pi, e.getX(), e.getY(), 20, 20)) {
                    clickedLine = edge;
                }
            });


        }

        @Override
        public void mouseReleased(MouseEvent e) {
            clickedLine = null;
            clickedNode = null;
        }

    }

    class mouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (clickedNode != null) {
                clickedNode.reposition(e.getX(), e.getY());
            } else if (clickedLine != null) {
                if (e.isShiftDown()) {
                    clickedLine.setControlPoint(null);
                } else {
                    clickedLine.setControlPoint(new ControlPoint(e.getX(), e.getY()));
                }
            }
            repaint();
        }
    }
}
