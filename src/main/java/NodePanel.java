import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;

public class NodePanel extends JPanel {
    Path2D linePath = new Path2D.Double();
    boolean line = true;
    int x;
    int y;
    int ppi = 0;

    public NodePanel(int ppi) {
        addMouseListener(new mouseListener());
        addMouseMotionListener(new mouseMotionListener());
        this.ppi = ppi;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        linePath.reset();
        linePath.moveTo(1.8472 * ppi, 0.25* ppi);
        if (line) {
            linePath.lineTo(1.0833* ppi,1.25* ppi);
        } else {
            Path2D p2 = new Path2D.Double();
            p2.reset();
            p2.moveTo(1.8472* ppi, .25* ppi);
            p2.lineTo(x,y);
            p2.lineTo(1.0833* ppi,1.25* ppi);
            g2d.draw(p2);
            linePath.curveTo(x, y, x, y, 1.0833* ppi,1.25* ppi);
            System.out.println("x: " + x + ", y: " + y);
        }
        g2d.draw(linePath);
    }

    class mouseListener extends MouseAdapter {

        boolean clicked = false;

        @Override
        public void mousePressed(MouseEvent e) {
            if (linePath.getBounds().contains(e.getPoint())) {
                System.out.println(clicked);
                clicked = true;
                System.out.println(clicked);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("released");
            clicked=false;
        }

    }

    class mouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            line = false;
            repaint();
        }
    }

}
