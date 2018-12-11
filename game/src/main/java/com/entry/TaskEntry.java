package com.entry;

import com.entry.po.PhasePo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Getter
@Setter
public class TaskEntry extends BaseEntry {

    public TaskEntry(long id) {
        super(id);
    }

    private int type;
    private int status;
    private int currNum;

    private Date startTime = new Date();
    private Date statusTime = new Date();

    private List<PhasePo> phaseList = new ArrayList<>();

}

