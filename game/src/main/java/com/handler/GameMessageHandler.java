package com.handler;

import com.hanlder.MessageThreadHandler;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameMessageHandler extends MessageThreadHandler {
    final static io.vertx.core.logging.Logger log = LoggerFactory.getLogger(GameMessageHandler.class);

}
