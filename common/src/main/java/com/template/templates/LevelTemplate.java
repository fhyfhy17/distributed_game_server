package com.template.templates;

import com.annotation.Template;
import lombok.Data;

@Data
@Template(path = "level.xml")
public class LevelTemplate extends AbstractTemplate {
    private int level;
    private int exp;

    @Override
    void init() {

    }
}
