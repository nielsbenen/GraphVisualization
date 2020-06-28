import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {

    private DrawPanel d;
    private JSplitPane splitPane = null;

    public GUI() {
        super("Graph Drawer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //left pane
        JPanel left = new JPanel();

        JLabel layoutLabel = new JLabel("Select Lay Out Algorithm:");
        JComboBox<String> layoutAlgorithm = new JComboBox<>(new String[] {"CIRCO", "NEATO", "DOT", "FDP", "OSAGE", "TWOPI" });

        JLabel nodeSizeLabel = new JLabel("Select node size (pt):");
        JSlider nodeSize = new JSlider(JSlider.HORIZONTAL, 20, 50, 35);
        nodeSize.setMinorTickSpacing(1);
        nodeSize.setMajorTickSpacing(5);
        nodeSize.setPaintTicks(true);
        nodeSize.setPaintLabels(true);

        JCheckBox showLabels = new JCheckBox("Show labels", true);


        JLabel fileLabel = new JLabel("Select DOT file:");


        JButton fileChooserSelect = new JButton("Select DOT File");
        JFileChooser fileChooser = new JFileChooser();

        //Open file chooser when select button is clicked
        fileChooserSelect.addActionListener(e -> {
            int r = fileChooser.showDialog(null, "Select");

            if (r == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected directory
                ((JLabel) ((JButton) e.getSource()).getParent().getComponent(5))
                        .setText("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        //button to click when you want to draw a graph
        JButton button = new JButton("Draw Graph");
        button.addActionListener(e -> {
            if (fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(new JFrame(), "No file selected!", "Error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            File f = fileChooser.getSelectedFile();
            try {
                d = new DrawPanel();
                LayOutMethod lom = LayOutMethod.valueOf(layoutAlgorithm.getSelectedItem().toString());
                int nsize = nodeSize.getValue();
                boolean labels = showLabels.isSelected();
                d.setup(GraphParser.parse(f, lom, nsize, labels));
                splitPane.setRightComponent(d);
                splitPane.setDividerLocation(0.2);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        JButton export = new JButton("Export to TikZ");
        export.setAlignmentX(Component.CENTER_ALIGNMENT);
        export.addActionListener(e -> {
            if (fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(new JFrame(), "No file selected!", "Error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JPanel newMenu = new JPanel();
            JLabel widthLabel = new JLabel("Select maximal graph width (cm):");
            JSlider width = new JSlider(JSlider.HORIZONTAL, 2, 12, 10);
            JLabel heightLabel = new JLabel("Select maximal graph height (cm):");
            JSlider height = new JSlider(JSlider.HORIZONTAL, 2, 20, 14);
            JButton openSelectionButton = new JButton("Save TikZ code to file");
            JFileChooser selectSaveFile = new JFileChooser();

            //Open file chooser when select button is clicked
            openSelectionButton.addActionListener(ev -> {
                int r = selectSaveFile.showDialog(null, "Save");

                if (r == JFileChooser.APPROVE_OPTION) {
                    if (fileChooser.getSelectedFile() == null) {
                        JOptionPane.showMessageDialog(new JFrame(), "No file selected!", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Drawing draw = d.getDrawing().clone();
                    if (draw.getWidth() > width.getValue() || draw.getHeight() > height.getValue()) {
                        double factor = Math.min(width.getValue() / draw.getWidth(), height.getValue() / draw.getHeight());
                        draw.scale(factor);
                        draw.inverse();
                        draw.setNodeSizeCM(draw.getNodeSize() * factor);
                    }
                    TikzExporter.createTikzPicture(draw, selectSaveFile.getSelectedFile());
                }
            });

            width.setMinorTickSpacing(1);
            width.setMajorTickSpacing(2);
            width.setPaintTicks(true);
            width.setPaintLabels(true);
            height.setMinorTickSpacing(1);
            height.setMajorTickSpacing(2);
            height.setPaintTicks(true);
            height.setPaintLabels(true);

            newMenu.add(widthLabel);
            newMenu.add(width);
            newMenu.add(heightLabel);
            newMenu.add(height);
            newMenu.add(openSelectionButton);
            newMenu.setLayout(new GridLayout(3, 2));

            JDialog dialog = new JDialog();
            dialog.setTitle("Export to TikZ");
            dialog.setSize(800, 800);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.add(newMenu);
            dialog.pack();
            dialog.setVisible(true);
        });

        layoutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        layoutAlgorithm.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(layoutLabel);
        left.add(layoutAlgorithm);

        nodeSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nodeSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(nodeSizeLabel);
        left.add(nodeSize);

        showLabels.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(showLabels);

        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileChooserSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(fileLabel);
        left.add(fileChooserSelect);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        export.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(button);
        left.add(export);
        left.setLayout(new GridLayout(10, 1));

        //right pane

        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(left);
        splitPane.setRightComponent(Box.createHorizontalStrut(0));
        splitPane.setDividerLocation(0.3);
        getContentPane().add(splitPane);
        setVisible(true);
    }
}

