package app;

import org.eclipse.jdt.core.dom.ASTNode;
import structure.MyASTNode;
import structure.MyMethodNode;

/**
 * @author lyl
 */
public class ASTtoDOT {
    /**
     * Convert a method node to .dot string
     * @param m
     * @return
     */
    public static String ASTtoDotParser(MyMethodNode m) {
        String str = "digraph \"DirectedGraph\" {\n";
        // name
        str += ("graph [label = \"" + m.methodNode.getName() + "\", labelloc=t, concentrate = true];\n");
        for (MyASTNode mn : m.nodeList) {
            ASTNode astNode = mn.astNode;
            int hashcode = astNode.hashCode();
            int nodeType = astNode.getNodeType();
            str += ("\"" + hashcode + "\" [ label=\""+buildLabel(mn)+"\" type=" + nodeType
                    + " startLineNumber=" + mn.startLineNum +" endLineNumber=" + mn.endLineNum + " ]\n");
        }
        for (int[] k : m.mapping) {
            int pHashcode = k[0];
            int hashcode = k[1];
            str += ("\"" + pHashcode + "\" -> \"" + hashcode + "\"\n");
        }
        str += "}\n";
        return str;
    }

    /**
     * Configure the label, i.e., what you want to display in the visualization
     * @param node
     * @return
     */
    public static String buildLabel(MyASTNode node) {
        String contentString=node.astNode.toString().replace("\n", " ").replace("\"", "\\\"").replace("  ", " ");
        String nodeType= ASTNode.nodeClassForType(node.astNode.getNodeType()).getName().replace("org.eclipse.jdt.core.dom.", "");
        return "("+contentString+","+nodeType+","+node.startLineNum+","+node.endLineNum+")";
    }
}
