package com.entry;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class BaseEntry {

    @Id
    protected String id;
}
