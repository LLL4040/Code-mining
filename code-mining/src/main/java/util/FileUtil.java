package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author lyl
 */
public class FileUtil {
    /**
     * 写文件
     * @param filePath 文件路径
     * @param str 写入的内容
     */
    public static void writeFile(String filePath, String str) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件并转换成字符串
     * @param filePath 文件路径
     * @return 文件内容字符串
     * @throws IOException
     */
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("package") || line.startsWith("import")) {
                continue;
            }
            String s = analyze(line);
            if (!s.endsWith("\n")) {
                s += "\n";
            }
            fileData.append(s);
        }

        reader.close();
        return fileData.toString();
    }

    public static String analyze(String line) {
        Boolean quotation = false, star = false;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                quotation = !quotation;
                buf.append(line.charAt(i));
            } else if (line.charAt(i) == '/') {
                if (!quotation) {
                    // 如果不在引号里并且是//就直接删掉后面的部分
                    if (i+1 < line.length() && line.charAt(i+1) == '/') {
                        buf.append('\n');
                        return buf.toString();
                    }
                    // 如果不在引号里并且是/*或者/**就找后面有没有*/
                    if (i+1 < line.length() && line.charAt(i+1) == '*') {
                        star = true;
                        i++;
                    }
                } else {
                    buf.append(line.charAt(i));
                }
            } else if (line.charAt(i) == '*') {
                if (!quotation) {
                    if (!star) {
                        buf.append('\n');
                        return buf.toString();
                    } else {
                        if (i+1 < line.length() && line.charAt(i+1) == '/') {
                            star = false;
                            i++;
                        }
                    }
                } else {
                    buf.append(line.charAt(i));
                }
            } else {
                buf.append(line.charAt(i));
            }
        }
        return buf.toString();
    }
}
