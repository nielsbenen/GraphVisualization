import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizV8Engine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;

public class GraphParser {

    public static Drawing parse(File f, LayOutMethod layout, int nodeSize, boolean labels) throws IOException {
        InputStream dot = new FileInputStream(f);
        MutableGraph g = new Parser().read(dot);

        Graphviz.useEngine(new GraphvizV8Engine());
        Graphviz viz = Graphviz.fromGraph(g);

        viz = viz.engine(Engine.valueOf(layout.toString()));
        //parse render
        Drawing d = null;
        for (String s : viz.render(Format.PLAIN).toString().split("\n")) {
            //split into segments
            String[] split = s.split(" ");
            switch (split[0]) {
                case "graph":
                    d = new Drawing(Double.parseDouble(split[2]), Double.parseDouble(split[3]), g.isDirected(), labels);
                    break;
                case "node":
                    d.addNode(Double.parseDouble(split[2]), Double.parseDouble(split[3]), split[1], split[6]);
                    break;
                case "edge":
                    d.addEdge(split[1], split[2]);
                    break;
            }
        }
        d.setNodeSize(nodeSize);
        return d;
    }
}
