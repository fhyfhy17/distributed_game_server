package com.template.templates;

import com.annotation.Template;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Template(path = "Task.xlsx_task.xml")
public class TaskTemplate extends AbstractTemplate {

    private int limit;

}