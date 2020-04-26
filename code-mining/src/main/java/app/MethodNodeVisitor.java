package app;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyl
 */
public class MethodNodeVisitor extends ASTVisitor {
    List<MethodDeclaration> methodDeclarationList = new ArrayList<>();

    public List<MethodDeclaration> getMethodDecs() {
        return methodDeclarationList;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        methodDeclarationList.add(node);
        return true;
    }
}
