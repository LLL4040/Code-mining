package app;

import structure.MyASTNode;
import structure.MyMethodNode;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author lyl
 */
public class Application {
    /**
     * 解析给定java文件并输出到指定目录
     * @param args 运行参数
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        useCodeParser();
//        useASTGenerator();
    }

    public static void useCodeParser() throws IOException {
        String filePath = "D:\\SJTU\\ShenLab\\akhq-dev\\src\\main\\java\\org\\akhq\\repositories";
//        String filePath = "D:\\SJTU\\ShenLab\\akhq-dev\\src\\main\\java\\org\\akhq\\configs";
//        String filePath = "D:\\SJTU\\ShenLab\\ChatRoom-JavaFX-master\\ChatServer-TXP";
//        String filePath = "D:\\SJTU\\ShenLab\\ChatRoom-JavaFX-master\\ChatClient";
//        String filePath = "D:\\SJTU\\ShenLab\\hackerrank_top_solutions_dataset-master\\";
//        String filePath = "D:\\SJTU\\ShenLab\\JavaFX-master\\";
//        String filePath = "D:\\SJTU\\ShenLab\\azkaban-master\\azkaban-web-server\\";
//        String filePath = "D:\\SJTU\\ShenLab\\spring-data-examples-master\\mongodb\\";
//        String filePath = "D:\\SJTU\\ShenLab\\spring-data-examples-master\\jpa\\example\\src\\";
//        String filePath = "D:/SJTU/ShenLab/code-mining/src/main/java/input/Test.java";
        String outputDir = "D:/SJTU/ShenLab/code-mining/src/main/java/output/akhq-dev/";
//        String outputDir = "D:/SJTU/ShenLab/code-mining/src/main/java/output/ChatRoom-JavaFX-master/";
//        String outputDir = "D:/SJTU/ShenLab/code-mining/src/main/java/output/";
//        File file = new File(filePath);
//        CodeParser codeParser = new CodeParser(file);
        CodeParser codeParser = new CodeParser(filePath);
        int n = codeParser.getAllFilesNodes().size();
        List<List<MyASTNode>> I = codeParser.extractIdioms(n);
        String rootOfI = "";
        for (List<MyASTNode> pair : I) {
            rootOfI += pair.get(0).astNode.toString() + "\n";
            rootOfI += "----------------------------------------------------------------------\n";
        }
        FileUtil.writeFile(outputDir + "repositories_rootOfI.txt", rootOfI);
//        FileUtil.writeFile(outputDir + "TestCode_rootOfI.txt", rootOfI);

        System.out.println("Done.");
    }

    public static void useASTGenerator() throws IOException {
//        String filePath = "D:\\SJTU\\ShenLab\\spring-data-examples-master\\jpa\\example\\src\\";
        String filePath = "D:/SJTU/ShenLab/code-mining/src/main/java/input/Test.java";
        String outputDir = "D:/SJTU/ShenLab/code-mining/src/main/java/output/";
        File file = new File(filePath);
        ASTGenerator astGenerator = new ASTGenerator(file);
        List<MyMethodNode> methodNodeList = astGenerator.getMethodNodeList();
//        for (MyMethodNode m : methodNodeList) {
//            String dotStr = ASTtoDOT.ASTtoDotParser(m);
//            FileUtil.writeFile(outputDir + file.getName() + "_" + m.methodNode.getName() + ".dot", dotStr);
//        }
        List<List<MyASTNode>> I = astGenerator.extractIdioms(5);
        String rootOfI = "";
        for (List<MyASTNode> pair : I) {
            rootOfI += pair.get(0).astNode.toString() + "\n";
            rootOfI += "----------------------------------------------------------------------\n";
        }
        FileUtil.writeFile(outputDir + "TestAST_rootOfI.txt", rootOfI);
        System.out.println("Done.");
    }
}
