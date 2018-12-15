package com.template;

import com.annotation.Template;
import com.template.templates.AbstractTemplate;
import com.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

@Component
@Slf4j
public class TemplateManager {
    @Autowired
    private TemplateLoader loader;

    /**
     * 加载模板数据的过程.
     */
    public void load() {
        Map<String, Object> templates = SpringUtils.getBeansWithAnnotation(Template.class);
        for (Map.Entry<String, Object> t : templates.entrySet()) {
            String className = t.getKey();
            Object o = t.getValue();

            if (!(o instanceof AbstractTemplate)) {
                log.error("非模板类 = {}", className);
                continue;
            }
            Template templateAnno = o.getClass().getAnnotation(Template.class);


            try (InputStream is = Files.newInputStream(Paths.get("templates/", templateAnno.path()), StandardOpenOption.READ)) {
                loader.loadTemplate(is,((AbstractTemplate)o).getClass(),templateAnno.path());
            } catch (IOException e) {
                log.error("", e);
            }

        }
        for (Object o : templates.values()) {
            if (!(o instanceof AbstractTemplate)) {
                log.error("非模板类");
                continue;
            }
//            try (InputStream is = Files.newInputStream(Paths.get("templates/", file.value()), StandardOpenOption.READ)) {
//
//            }
        }

    }

    public void check() {

    }
}
