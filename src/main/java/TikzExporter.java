import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class TikzExporter {
    public static void createTikzPicture(Drawing d, File f) {
        NumberFormat df = NumberFormat.getNumberInstance(Locale.UK);
        df.setMaximumFractionDigits(2);

        try {
            f.createNewFile();
            FileWriter writer = new FileWriter(f);
            writer.write("\\begin{tikzpicture} [every node/.style={draw,circle,minimum size=" + df.format(d.getNodeSizeCM()) + "cm,inner sep=1}]\n");

            for (Map.Entry<String, Node> e : d.getNodes().entrySet()) {
                String id = e.getKey();
                Node n = e.getValue();
                String label = n.getLabel();
                writer.write("\\node (" + id + ") at  (" + df.format(n.getX()) + "," + df.format(n.getY()) +
                        ") {" + (d.getShowLabels() ? label : "") +"};\n");
            }

            for (Edge e : d.getEdges()) {
                System.out.println(e.getControlPoint());
                if (e.getControlPoint() != null) {
                    writer.write("\\draw" + (d.isDirected() ? "[-{Latex[length=2mm]}]" : "") + " (" + e.getNodeFrom() +
                            ") .. controls($(" + df.format(e.getControlPoint().getX()) + "," +
                            df.format(e.getControlPoint().getY()) + ")!1/3!(" + e.getNodeFrom() + ")$) and ($("
                            + df.format(e.getControlPoint().getX()) +
                            "," + df.format(e.getControlPoint().getY()) + ")!1/3!(" + e.getNodeTo() +
                            ")$) .. (" + e.getNodeTo() + ");\n");
                } else {
                    writer.write("\\draw" + (d.isDirected() ? "[-{Latex[length=2mm]}]" : "") +
                            "(" + e.getNodeFrom() + ") -- (" + e.getNodeTo() + ");\n");
                }
            }

            writer.write("\\end{tikzpicture}\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
