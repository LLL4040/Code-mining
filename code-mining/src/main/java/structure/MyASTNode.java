package structure;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyl
 */
public class MyASTNode {
    public ASTNode astNode;
    public MyASTNode parent;
    public List<MyASTNode> children;
    public int startLineNum;
    public int endLineNum;
    public int count;

    public MyASTNode() {
        this.astNode = null;
        this.parent = null;
        this.count = 1;
        this.children = new ArrayList<>();
    }
}
