package com.entry;

import com.annotation.IncKey;
import com.annotation.SeqClassName;
import lombok.AccessLevel;
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
    @IncKey
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long playerId;

    @Indexed
    private String name;
}
