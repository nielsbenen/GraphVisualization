# GraphVisualization
A prototypical application which allows a user to create a graph visualization of any input graph

# Requirements
An installation of GraphViz is required alongside Java 8.

# How to use
Clone the github repository and run the included ResearchProject.jar

When the application has started, a .DOT file can be selected to be displayed. 
Set the appropriate parameters (lay-out, node size) and click 'Draw Graph'. The graph
is now shown on screen. The nodes can be moved around by dragging them. Edges can be
curved by clicking on them and moving the mouse. If you accidentally curve an edge,
you can return it to its straight-lined form by holding shift while clicking it. 

Once you are satisfied with the result, you click the 'Export to TikZ' button. 
Here, you first define the maximum width and height the graph may have in your document.
Then, you select the file to which the generated code should be exported. After you
exported it, you can open the file to find the generated code. This can be copy pasted
into your LaTeX document to get the same graph.

When placing the exported code in a LaTeX document, ensure that the following packages are present:

- \usepackage{tikz}
- \usetikzlibrary{calc}

