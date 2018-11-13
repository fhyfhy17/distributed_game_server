package com.entry;

import com.annotation.SeqClassName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@SeqClassName(name = "seq.PlayerEntry")
public class PlayerEntry extends BaseEntry {

    @Indexed
    private String name;
}
