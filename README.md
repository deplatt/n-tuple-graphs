# n-tuple-graphs

## Description
This project is a visualizion tool for n-tuple graphs. It is built with the GraphStream 1.3 API for graph visualization, and Java Swing for the UI elements. FlatLaf IntelliJ is uses for the UI's look and feel. It was created by Deven Platt, who can be contacted through devenplatt@gmail.com.

## Installation
The project requires an installation of the Java 17 JDK and GraphStream 1.3. Once installed, pull the source code and run Client.java. If you are on Mac OS, run with the following VM argument:

-Dsun.java2d.uiScale=100%

## Usage
### Graphs
The purpose of this program is to illistrate a graph and it's n-tuple counterpart. Of the two large graph panels, the left is the base graph, and the right is its n-tuple graph. The base graph can be modified in a number of ways described below. In general, any change to the base graph will dynamically update the n-tuple graph. The clear button on the bottom left will clear both of the graphs. Additionally, the slider on the bottom of the window represents n. Changing the value on the slider will update the n-tuple graph accordingly.

### Generators
This program has several built-in generators for common types of graphs. To use this feature, click the generators drop-down menu on the top left of the screen. From top to bottom, there is a generator for paths, cycles, complete graphs, star graphs, and complete bipartite graphs. Click the desired graph type, and then use the spinner below to adjust the order of the graph. In the case of a complete bipartite graph, a second spinner will appear so that the order of both independent sets can be adjusted.

### Select Mode
This program contains three mutually exclusive modes for interacting with the graph. Click the icon on the left side of the screen to select the desired mode. From top to bottom: highlight, pathing, and create mode.

With highlight mode selected, right click a node on either graph to select or delect it. Any number of nodes can be selected at once, and right clicking off of a node will deselect them all.

With pathing mode selected, right click on two nodes in order to see the maximum number of internally-disjoint paths between them. This is particularly useful for examining the connectivity of an n-tuple graph.

With create mode selected, right click on a node to select it. Right clicking another node will either create or delete an edge between them. In addition, this mode unlocks the plus icon and trash icon buttons below. These add a node to the graph, and delete the selected node, respectively. Create mode can be used to build a graph from scratch, or to build off of a pre-generated graph.

### Graph Controls
Each graph has a control panel below it, allowing for customization of the look and layout of each graph individually, as well as displaying some information. By default, each graph will use the GraphStream algorithm to position each node. By unchecking the box next to "Auto-Layout," you can click and drag on the nodes to position them in any way. The window automatically resizes so that no nodes can be offscreen. Additionally, unchecking the "Show labels" will shrink the nodes considerably and remove their labels. This is particularly helpful with larger graphs to see all the nodes, or for high values of n because the labels on the n-tuple graph can get large.

Next to those checkboxes is a button for the connectivity of the graph. Clicking this buttom will calculate the vertex connectivity. This is a very computationally-complex task, so it may take a moment to resolve and is not reccomended for large n-tuple graphs.

To the right of that button is the circulize button. This will disable the auto-layout, and then arrange all of the nodes into a graph into a circle, so that all edges are displayed within the circle. 

The other layout control is the bipartize button. When clicked, the graph to checked to see if it is bipartite. If the graph is not bipartite, nothing happens. If it is, auto-layout is disabled, and the graph is displayed such that the nodes in each independent set are grouped together, and each set is displayed across the frame from the other.

Finally, the refresh button on the right will reset the graph and these settings (except for "Show Labels"). This is particularly helpful when viewing the graph's auto-layout, as it will sometimes appear more natural after refreshing.

## Known Bugs
* The pathing mode will occasionally not show the maximum number of internally-disjoint paths between two nodes. It find one path that invalidates two or more other paths of the same length.
* After deleting multiple modes in a row, the add node button will add that many nodes back, instead of just one.
