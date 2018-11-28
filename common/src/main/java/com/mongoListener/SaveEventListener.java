package com.mongoListener;

import com.annotation.EventListener;
import com.annotation.IncKey;
import com.annotation.SeqClassName;
import com.entry.BaseEntry;
import com.entry.SeqEntry;
import com.util.Pair;
import com.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
@EventListener
public class SaveEventListener extends AbstractMongoEventListener<BaseEntry> {

    @Autowired
    private MongoTemplate mongo;

    private ConcurrentHashMap<String, Pair<Long, Long>> map = new ConcurrentHashMap<>();

    private final static int EVERY_GET_ID_NUM = 50;

    @PostConstruct
    public void init() {
        List<String> seqClassNames = ReflectionUtil.getSeqClassNames();
        for (String seqClassName : seqClassNames) {
            map.put(seqClassName, new Pair<>(-1L, -1L));
        }
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntry> event) {
        BaseEntry source = event.getSource();

        if (source != null) {
            if (!StringUtils.isEmpty(source.getId())) {
                return;
            }

            ReflectionUtils.doWithFields(source.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(IncKey.class)) {
                    String name = source.getClass().getAnnotation(SeqClassName.class).name();

                    field.set(source, getNextId(name));
                }
            });
        }
    }

    private Long getNextIdFromMongo(String collName) {

        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", EVERY_GET_ID_NUM);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqEntry seq = mongo.findAndModify(query, update, options, SeqEntry.class);
        return seq.getSeqId();
    }


    private Long getNextId(String name) {

        Pair<Long, Long> longLongPair = map.computeIfPresent(name, (s, idPair) -> {

            if (idPair.getKey() == -1) {
                Long nextMaxId = getNextIdFromMongo(name);
                return new Pair<>(nextMaxId - EVERY_GET_ID_NUM + 1, nextMaxId);
            }
            if (idPair.getKey().equals(idPair.getValue())) {
                Long nextMaxId = getNextIdFromMongo(name);
                return new Pair<>(nextMaxId - EVERY_GET_ID_NUM + 1, nextMaxId);
            } else {
                idPair.setKey(idPair.getKey() + 1);
                return idPair;
            }
        });
        System.out.println(longLongPair.getKey());
        return longLongPair.getKey();

    }

}

