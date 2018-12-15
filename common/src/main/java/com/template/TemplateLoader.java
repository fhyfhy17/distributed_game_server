package com.template;

import com.template.templates.AbstractTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class TemplateLoader {

    public  <T extends AbstractTemplate> List<T> loadTemplate(InputStream is, Class<T> clazz, String name) {

        List<T> ts = new ArrayList<>();

        try {
            Document doc = new SAXBuilder().build(is);
            Element root = doc.getRootElement();
            Iterator<Element> it = root.getChildren().<Element>iterator();

            while (it.hasNext()) {
                T t = clazz.newInstance();
                for (Object elem : it.next().getAttributes()) {
                    Attribute attr = (Attribute) elem;
                    String value = (null == attr.getValue()) ? "" : attr.getValue().trim();
                    setProperties(t, attr.getName(), value);
                }
                ts.add(t);
            }

        } catch (Exception e) {
            log.error("加载 XML 资源文件 {} ",name, e);
        }

        if (ts.isEmpty()) {
            log.error("警告：XML 资源文件加载为空：{}", name);
        }

        return ts;
    }

    private static <T> void setProperties(T object, String fieldName, String fieldValue) {
        PropertyDescriptor prop = null;
        try {
            prop = new PropertyDescriptor(fieldName, object.getClass());
        } catch (Exception ex) {
            return;
        }

        Class<?> fieldClass = prop.getPropertyType();
        String field = fieldClass.getSimpleName();
        try {

            Method m = prop.getWriteMethod();
            boolean emptyVal = "".equals(fieldValue);
            Object val;

            if (field.equals("String")) {
                val = fieldValue;
            } else if (field.equals("int")) {
                val = emptyVal ? 0 : Integer.parseInt(fieldValue);
            } else if (field.equals("long")) {
                val = emptyVal ? 0 : Long.parseLong(fieldValue);
            } else if (field.equals("float")) {
                val = emptyVal ? 0 : Float.parseFloat(fieldValue);
            } else if (field.equals("double")) {
                val = emptyVal ? 0 : Double.parseDouble(fieldValue);
            } else if (field.equals("byte")) {
                val = emptyVal ? 0 : Byte.parseByte(fieldValue);
            } else if (field.equals("short")) {
                val = emptyVal ? 0 : Short.parseShort(fieldValue);
            } else if (field.equals("Date")) {
                val = emptyVal ? new Date() : new Date(fieldValue);
            } else if (field.equals("boolean")) {
                if (fieldValue.equals("1")) {
                    val = true;
                } else if (fieldValue.equals("0")) {
                    val = false;
                } else {
                    val = !emptyVal && Boolean.parseBoolean(fieldValue);
                }
            } else {
                return;
            }

            m.invoke(object, val);

        } catch (Exception e) {
            log.error(
                    "Config namespace error {}: class={} colName={} type={} namespace={}",
                    e.getMessage(), object.getClass().getSimpleName(), fieldName, field, fieldValue);
        }
    }
}
