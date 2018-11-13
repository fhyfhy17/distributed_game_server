package com.mongoListener;

import com.annotation.IncKey;
import com.annotation.SeqClassName;
import com.entry.BaseEntry;
import com.entry.SeqEntry;
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

@Component
public class SaveEventListener extends AbstractMongoEventListener<BaseEntry> {

    @Autowired
    private MongoTemplate mongo;


    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntry> event) {
        BaseEntry source = event.getSource();
        String collectionName = event.getCollectionName();
        if (source != null) {
            if (exist(collectionName, source)) {
                return;
            }
            ReflectionUtils.doWithFields(source.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(IncKey.class)) {
                    field.set(source, getNextId(source.getClass().getAnnotation(SeqClassName.class).name(), source));
                }
            });
        }
    }


    private boolean exist(String collectionName, BaseEntry baseEntry) {
        Query exist = new Query(Criteria.where("_id").is(baseEntry.getId()));
        if (mongo.exists(exist, collectionName)) {
            return true;
        }
        return false;
    }

    private Long getNextId(String collName, BaseEntry baseEntry) {

        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqEntry seq = mongo.findAndModify(query, update, options, SeqEntry.class);
        return seq.getSeqId();
    }
}

