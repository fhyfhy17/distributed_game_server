package com.template.templates;

import com.annotation.Template;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Template(path = "level.xml")
public class LevelTemplate extends AbstractTemplate {
    private int level;
    private int exp;

    @Override
    void init() {

    }
}
