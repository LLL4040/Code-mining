package app;

import org.eclipse.jdt.core.dom.*;
import structure.MyASTNode;
import structure.MyMethodNode;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyl
 */
public class ASTGenerator {
    private List<MyMethodNode> methodNodeList = new ArrayList<>();

    public ASTGenerator(File file) {
        parseFile(file);
    }
    public ASTGenerator(String dirPath) throws IOException{
        parseFilesInDir(dirPath);
    }

    public List<MyMethodNode> getMethodNodeList() {
        return methodNodeList;
    }

    public void parse(String srcStr) {
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        parser.setSource(srcStr.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // find the MethodDeclaration node, MethodNodeVisitor
        MethodNodeVisitor methodNodeVisitor = new MethodNodeVisitor();
        cu.accept(methodNodeVisitor);
        // traverse all child nodes, NodeVisitor
        for (MethodDeclaration m : methodNodeVisitor.getMethodDecs()) {
            MyMethodNode myMethodNode = new MyMethodNode();
            myMethodNode.methodNode = m;
            NodeVisitor nodeVisitor = new NodeVisitor();
            m.accept(nodeVisitor);
            List<ASTNode> astNodeList = nodeVisitor.getASTNodeList();
            for (ASTNode astNode : astNodeList) {
                MyASTNode myASTNode = new MyASTNode();
                myASTNode.astNode = astNode;
                myASTNode.startLineNum = cu.getLineNumber(astNode.getStartPosition());
                myASTNode.endLineNum = cu.getLineNumber(astNode.getStartPosition()+astNode.getLength());
                if (astNode.getParent() == null) {
                    myASTNode.parent = null;
                } else {
                    for (MyASTNode pre : myMethodNode.nodeList) {
                        if (pre.astNode.equals(astNode.getParent())) {
                            myASTNode.parent = pre;
                            pre.children.add(myASTNode);
                            break;
                        }
                    }
                }
                myMethodNode.nodeList.add(myASTNode);
                if(myASTNode.equals(m)) {
                    continue;
                }
                int pHashcode = astNode.getParent().hashCode();
                int hashcode = astNode.hashCode();
                int[] link = { pHashcode, hashcode };
                myMethodNode.mapping.add(link);
            }
            methodNodeList.add(myMethodNode);
        }
    }

    public void parseFile(File file) {
        String filePath = file.getAbsolutePath();
        if (file.isFile()) {
            try {
                parse(FileUtil.readFileToString(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not a file!");
        }
    }

    //loop directory to get file list
    public void parseFilesInDir(String dirPath) throws IOException {
        File root = new File(dirPath);
        File[] files = root.listFiles();
        String filePath = null;

        for (File f : files) {
            filePath = f.getAbsolutePath();
            if (f.isFile()) {
                if (filePath.endsWith(".java")) {
                    parse(FileUtil.readFileToString(filePath));
                }
            } else {
                //去掉测试文件
                if (!filePath.contains("\\test\\")) {
                    parseFilesInDir(filePath);
                }
            }
        }
    }

    public MyASTNode mostFrequent(MyMethodNode myMethodNode) {
        for (MyASTNode myASTNode : myMethodNode.nodeList) {
            if (myASTNode.parent == null) {
                continue;
            }
            ASTNode child = myASTNode.astNode;
            ASTNode parent = myASTNode.parent.astNode;
            // parent都是比较type
            // 不是叶子节点，只需要比较type
            if (child.getProperty("isTerminal").equals(0)) {
                for (MyASTNode myASTNode1 : myMethodNode.nodeList) {
                    if (myASTNode1.equals(myASTNode)) {
                        continue;
                    }
                    if (myASTNode1.astNode.getNodeType() == child.getNodeType() && myASTNode1.parent.astNode.getNodeType() == parent.getNodeType()) {
                        myASTNode.count++;
                    }
                }
            } else {
                // 是叶子节点要比较string
                for (MyASTNode myASTNode1 : myMethodNode.nodeList) {
                    if (myASTNode1.equals(myASTNode)) {
                        continue;
                    }
                    if (myASTNode1.astNode.toString().equals(child.toString()) && myASTNode1.parent.astNode.getNodeType() == parent.getNodeType()) {
                        myASTNode.count++;
                    }
                }
            }
        }
        int most = 0;
        MyASTNode res = null;
        for (MyASTNode myASTNode : myMethodNode.nodeList) {
            if (myASTNode.count > most) {
                most = myASTNode.count;
                res = myASTNode;
            }
        }
        return res;
    }

    public void collapse(MyASTNode myASTNode) {
        MyASTNode parent = myASTNode.parent;
        parent.children.remove(myASTNode);
        for (MyASTNode child : myASTNode.children) {
            child.parent = parent;
            parent.children.add(child);
        }
    }

    public void collapseSubtree(MyMethodNode myMethodNode, MyASTNode myASTNode) {
        ASTNode child = myASTNode.astNode;
        ASTNode parent = myASTNode.parent.astNode;
        List<MyASTNode> toDel = new ArrayList<>();
        // parent都是比较type
        // 不是叶子节点，只需要比较type
        if (child.getProperty("isTerminal").equals(0)) {
            for (MyASTNode node : myMethodNode.nodeList) {
                if (node.astNode.getNodeType() == child.getNodeType() && node.parent.astNode.getNodeType() == parent.getNodeType()) {
                    collapse(node);
                    toDel.add(node);
                }
            }
        } else {
            // 是叶子节点要比较string
            for (MyASTNode node : myMethodNode.nodeList) {
                if (node.astNode.toString().equals(child.toString()) && node.parent.astNode.getNodeType() == parent.getNodeType()) {
                    collapse(node);
                    toDel.add(node);
                }
            }
        }
        for (MyASTNode node : toDel) {
            myMethodNode.nodeList.remove(node);
        }
    }

    public List<List<MyASTNode>> extractIdioms(int n) {
        List<List<MyASTNode>> I = new ArrayList<>();
        while (I.size() < n) {
            for (MyMethodNode myMethodNode : methodNodeList) {
                MyASTNode mast = mostFrequent(myMethodNode);
                if(mast.parent == null) {
                    continue;
                }
                MyASTNode parent = new MyASTNode();
                parent.astNode = mast.parent.astNode;
                MyASTNode child = new MyASTNode();
                child.astNode = mast.astNode;
                child.parent = parent;
                parent.children.add(child);
                List<MyASTNode> tmp = new ArrayList<>();
                tmp.add(parent);
                tmp.add(child);
                I.add(tmp);
                collapseSubtree(myMethodNode, mast);
                if (I.size() == n) {
                    break;
                }
            }
        }
        return I;
    }
}
