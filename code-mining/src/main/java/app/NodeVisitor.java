package app;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyl
 */
public class NodeVisitor extends ASTVisitor {
    public List<ASTNode> nodeList = new ArrayList<>();

    @Override
    public void preVisit(ASTNode node) {
        nodeList.add(node);
        List list = node.structuralPropertiesForType();
        node.setProperty("isTerminal", 1);
        for (Object o : list) {
            if (o.getClass().getName().contains("Child")) {
                node.setProperty("isTerminal", 0);
                break;
            }
        }
    }

    public List<ASTNode> getASTNodeList() {
        return nodeList;
    }
}
