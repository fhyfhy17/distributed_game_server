package com.template.templates;

import com.annotation.Template;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Component
@Template(path = "test.xlsx_level.xml")
public class LevelTemplate extends AbstractTemplate {

	private int level;
	private List<String> strs;
	private List<Integer> ints;
	private float length2;
	private List<Float> floats;

}