package com.util;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenTemplatesUtil {


    public static void main(String[] args) throws IOException {
        URL path = GenTemplatesUtil.class.getResource("/templates/");
        System.out.println(path.getPath());

        List<String> fileList = FileUtil.getFiles(path.getPath().substring(1, path.getPath().length()), ".xml");
        convert(fileList);
    }

    public static void convert(List<String> fileList) {
        for (String path : fileList) {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("文件不存在, path=" + path);
                return;
            }
            convert(file);
        }

    }

    public static void convert(File file) {
//		if (file.isFile())
        try {
            writeToModel(file);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
//		else {
//			File[] fileArr = file.listFiles();
//			for (File f : fileArr) {
//				if (!f.exists())
//					continue;
//				writeToModel(f);
//			}
//		}
    }

    private static void writeToModel(File file) throws JDOMException, IOException {

        Document doc = new SAXBuilder().build(file);
        Element root = doc.getRootElement();
        Iterator<Element> it = root.getChildren().<Element>iterator();
        List<Attribute> attrList = new ArrayList<>();
        if (it.hasNext()) {
            it.next().getAttributes().forEach(x ->
            {
                Attribute attr = (Attribute) x;
                attrList.add(attr);
            });
        }
        String templateFileName = "";
        String xmlName = file.getName().split("_")[1];
        templateFileName = xmlName.substring(0, xmlName.indexOf("."));
        writeToModel(templateFileName, attrList, file.getName());
//

    }

    //
    private static void writeToModel(String fileName, List<Attribute> attrList, String fullPathName) {
        System.out.println("---------------------------");
        String className = fileName + "Template";
        StringBuilder buff = new StringBuilder();
        buff.append("package com.template.templates;\n\n")
                .append("import com.annotation.Template;\n")
                .append("import lombok.Data;\n")
                .append("import java.util.*;\n")
                .append("import org.springframework.stereotype.Component;\n");


        buff.append("@Data\n");
        buff.append("@Component\n");
        buff.append("@Template(path = \"").append(fullPathName).append("\")\n");


        buff.append("public class ").append(toUpperFirstLetter(className)).append(" extends AbstractTemplate {\n");


        for (Attribute attr : attrList) {
            String key = attr.getName();
            String value = attr.getValue();
            if ("id".equals(key)) {
                continue;
            }
            buff.append("\n\tprivate ").append(getType(value)).append(" ").append(key).append(";");
        }
        buff.append("\n");

//        for (Attribute attr : attrList) {
//            String key = attr.getName();
//            String value = attr.getValue();
//            if ("id".equals(key)) {
//                continue;
//            }
//            buff.append("\n\tpublic ").append(getType(value)).append(" get").append(toUpperFirstLetter(key)).append("() {").append("\n\t\treturn this.").append(key).append(";").append("\n\t}\n");
//            buff.append("\n\tpublic void set").append(toUpperFirstLetter(key)).append("(").append(getType(value)).append(" ").append(key).append(") {").append("\n\t\tthis.").append(key)
//                    .append(" = ").append(key).append(";").append("\n\t}\n");
//        }


        buff.append("\n}");
        System.out.println(buff.toString());

        writeStringToFile(new File("").getAbsolutePath()
                + "\\common\\src\\main\\java\\com\\template"
                + "\\templates\\" + toUpperFirstLetter(className) + ".java", buff.toString());

    }

    public static String getType(String value) {

        if (value.contains("array")) {
            value = value.substring(0, value.indexOf("array"));
            value = "List<" + toUpperFirstLetter(value) + ">";
            return value;
        } else {
            return value;
        }
    }

    public static String toUpperFirstLetter(String letter) {
        if (letter.equals("int")) {
            letter = "integer";
        }
        char[] arr = letter.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return String.valueOf(arr);
    }

    public static void writeStringToFile(String path, String content) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
