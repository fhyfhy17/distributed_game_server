package com.entry;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "players")
@Data
public class PlayerEntry {
    @Id
    private String id;
    @Indexed
    private String name;
}
