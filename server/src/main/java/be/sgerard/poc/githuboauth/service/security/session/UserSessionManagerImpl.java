package be.sgerard.poc.githuboauth.service.security.session;

import be.sgerard.poc.githuboauth.model.security.session.ConnectedUserSessionEvent;
import be.sgerard.poc.githuboauth.model.security.session.DisconnectedUserSessionEvent;
import be.sgerard.poc.githuboauth.model.security.session.UserSessionDto;
import be.sgerard.poc.githuboauth.model.security.session.UserSessionEntity;
import be.sgerard.poc.githuboauth.model.security.user.UserEntity;
import be.sgerard.poc.githuboauth.service.event.EventService;
import be.sgerard.poc.githuboauth.service.security.auth.AuthenticationManager;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;
import java.util.Collection;

/**
 * @author Sebastien Gerard
 */
@Service
public class UserSessionManagerImpl implements UserSessionManager {

    private final AuthenticationManager authenticationManager;
    private final UserSessionManagerRepository repository;
    private final EventService eventService;

    public UserSessionManagerImpl(AuthenticationManager authenticationManager,
                                  UserSessionManagerRepository repository,
                                  EventService eventService) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.eventService = eventService;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserSessionEntity> getCurrentSessions() {
        return repository.findByLogoutTimeIsNull();
    }

    @EventListener
    @Transactional
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        final UserEntity user = authenticationManager.getUserFromPrincipal(event.getUser());

        final UserSessionEntity sessionEntity = new UserSessionEntity(user, getSessionId(event), Instant.now());

        repository.save(sessionEntity);

        eventService.broadcastEvent(new ConnectedUserSessionEvent(UserSessionDto.userSessionDto(sessionEntity).build()));
    }

    @EventListener
    @Transactional
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        final String sessionId = getSessionId(event);

        final UserSessionEntity sessionEntity = repository.findBySimpSessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("There is no session with id [" + sessionId + "]."));


        if (sessionEntity.getLogoutTime() == null) {
            sessionEntity.setLogoutTime(Instant.now());

            eventService.broadcastEvent(new DisconnectedUserSessionEvent(UserSessionDto.userSessionDto(sessionEntity).build()));
        }
    }

    private String getSessionId(AbstractSubProtocolEvent event) {
        final MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);

        if (accessor == null) {
            throw new IllegalStateException("Cannot extract session from null accessor.");
        }

        return SimpMessageHeaderAccessor.getSessionId(accessor.getMessageHeaders());
    }
}
