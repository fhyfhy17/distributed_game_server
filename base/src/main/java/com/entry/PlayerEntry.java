package com.entry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class PlayerEntry extends BaseEntry {

    @Indexed
    private String name;
}
