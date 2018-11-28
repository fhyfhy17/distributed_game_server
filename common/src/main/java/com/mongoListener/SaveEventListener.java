package com.mongoListener;

import com.annotation.IncKey;
import com.annotation.SeqClassName;
import com.entry.BaseEntry;
import com.entry.SeqEntry;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SaveEventListener extends AbstractMongoEventListener<BaseEntry> {

    @Autowired
    private MongoTemplate mongo;

    private ConcurrentHashMap<String, ArrayBlockingQueue<Long>> map = new ConcurrentHashMap<>();

    private final static int EVERY_GET_ID_NUM = 5;

    @PostConstruct
    public void init() {
        List<String> seqClassNames = ReflectionUtil.getSeqClassNames();
        for (String seqClassName : seqClassNames) {
            map.put(seqClassName, new ArrayBlockingQueue<>(EVERY_GET_ID_NUM));
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

    HashSet<Long> set = new HashSet<>();

    private Long getNextId(String name) {

//        ConcurrentLinkedQueue<Long> longs = map.computeIfPresent(name, (s, ids) -> {
//                    if (ids.isEmpty()) {
//                        Long nextMaxId = getNextIdFromMongo(name);
//
//                            System.out.println(nextMaxId);
//
//                        for (long i = (nextMaxId - EVERY_GET_ID_NUM + 1); i < nextMaxId + 1; i++) {
//                            ids.offer(i);
////                            System.out.println(i);
//                            set.add(i);
//                        }
//                    }
//                    return ids;
//                }
//        );
//        Long poll = longs.poll();
//        if(poll==null){
//            System.out.println();
//        }
//        return poll;

        synchronized (SaveEventListener.class) {
            ArrayBlockingQueue<Long> ids = map.get(name);
            if (ids.isEmpty()) {

                Long nextMaxId = getNextIdFromMongo(name);
                for (long i = (nextMaxId - EVERY_GET_ID_NUM + 1); i < nextMaxId + 1; i++) {
                    ids.offer(i);
                    System.out.println(i);
                    set.add(i);
                }
            }
            try {
                return ids.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public int getCount() {
        return set.size();
    }
}

