package structure;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * @author lyl
 */
public class MyMethodNode {
    public MethodDeclaration methodNode;
    public List<MyASTNode> nodeList;
    public List<int[]> mapping;

    public MyMethodNode() {
        this.methodNode = null;
        this.nodeList = new ArrayList<>();
        this.mapping = new ArrayList<>();
    }
}
