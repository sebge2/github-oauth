package be.sgerard.poc.githuboauth.service.event;

import be.sgerard.poc.githuboauth.model.event.Events;
import be.sgerard.poc.githuboauth.model.security.user.UserEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Gerard
 */
@Service
public class StompEventService implements EventService {

    private final SimpMessageSendingOperations template;

    public StompEventService(SimpMessageSendingOperations template) {
        this.template = template;
    }

    @Override
    public void broadcastEvent(String eventType, Object payload) {
        template.convertAndSend(Events.QUEUE_BROADCAST + "/" + eventType, payload);
    }

    @Override
    public void sendEventToUser(UserEntity user, String eventType, Object payload) {
        template.convertAndSendToUser(user.getId(), Events.QUEUE_USER + "/" + eventType, payload);
    }
}
