package com.entry;

import com.annotation.SeqClassName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@SeqClassName(name = "seq.PlayerEntry")
@ToString
public class PlayerEntry extends BaseEntry {
//    @Indexed
//    @IncKey
//    @Setter(AccessLevel.NONE)
//    private long playerId;

    public PlayerEntry(long id) {
        this.id = id;
    }

    private long uid;

    @Indexed
    private String name;

    private int level = 1;
    private int coin;

}
