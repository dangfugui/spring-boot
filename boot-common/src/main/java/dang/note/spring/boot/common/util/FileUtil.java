package dang.note.spring.boot.common.util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 文件处理 封装工具类
 */
public class FileUtil {

    public static File write(String path, List<String> contents, boolean append, String charsetName) throws
            IOException {
        File file = new File(path);
        if (!file.exists()) {
            file = createFile(file);
        } else if (!append) {
            file.delete();
            file.createNewFile();
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                charsetName)));
        for (String content : contents) {
            out.write(content);
            out.write("\n");
        }
        out.flush();
        out.close();
        return file;
    }

    public static File write(String path, InputStream inputStream) throws IOException {
        File outFile = new File(path);
        if (!outFile.exists()) {
            outFile = createFile(outFile);
        }
        OutputStream fos = new FileOutputStream(outFile);
        byte[] temp = new byte[1024];
        int i = inputStream.read(temp);
        while (i != -1) {
            fos.write(temp, 0, temp.length);
            fos.flush();
            i = inputStream.read(temp);
        }

        if (inputStream != null) {
            inputStream.close();

        }
        if (fos != null) {
            fos.close();
        }
        return outFile;
    }

    public static File write(String path, byte[] bytes) throws IOException {

        File outFile = new File(path);
        if (!outFile.exists()) {
            outFile = createFile(outFile);
        }
        RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
            randomFile = new RandomAccessFile(outFile, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile;
    }

    public static boolean move(File srcFile, String aimPath) throws IOException {
        // Destination directory
        File aimFile = new File(aimPath);
        if (!aimFile.getParentFile().exists()) {
            mkdir(aimFile.getParentFile());
        }
        // Move file to new directory
        boolean success = srcFile.renameTo(aimFile);
        return success;
    }

    public static String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return read(reader);
    }


    public static String read(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return read(reader);
    }

    public static String read(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return read(reader);
    }

    public static String read(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        int lineNumber = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((line = bufferedReader.readLine()) != null) {
            // 显示行号
            stringBuffer.append(line).append("\r\n");
            lineNumber++;
        }
        return stringBuffer.toString();
    }

    public static List<String> readAsList(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<String> strList = new ArrayList<>();
        String line = null;
        int lineNumber = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((line = reader.readLine()) != null) {
            strList.add(line);
            lineNumber++;
        }
        return strList;
    }

    private static File createFile(File file) throws IOException {

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent = mkdir(parent);
        }
        file.createNewFile();
        return file;
    }

    private static File mkdir(File file) throws IOException {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent = mkdir(parent);
        }
        file.mkdir();
        return file;
    }

    public static void delete(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                delete(f.getPath());
            }
            file.delete();
        }
    }
}
