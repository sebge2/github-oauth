package be.sgerard.poc.githuboauth.service.event;

import be.sgerard.poc.githuboauth.model.security.user.UserEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonMap;

/**
 * @author Sebastien Gerard
 */
@Service
public class StompEventManager implements EventManager {

    public static final String ALL_TOPIC_EVENT = "/topic/event";

    public static final String USER_TOPIC_EVENT = "/queue/event";

    private final SimpMessageSendingOperations template;

    public StompEventManager(SimpMessageSendingOperations template) {
        this.template = template;
    }

    @Override
    public void broadcastEvent(Object event) {
        template.convertAndSend(ALL_TOPIC_EVENT, event);
    }

    @Override
    public void sendEventToUser(UserEntity user, Object event) {
        template.convertAndSendToUser(user.getId(), USER_TOPIC_EVENT, event);
    }

    @Scheduled(fixedDelay = 1000)
    public void sendHello(){
        broadcastEvent(singletonMap("hello", "world"));
    }

}