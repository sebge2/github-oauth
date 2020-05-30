package be.sgerard.i18n.service;

import be.sgerard.i18n.model.support.LocalizedMessageHolder;

/**
 * Exception thrown when the caller made a bad request.
 *
 * @author Sebastien Gerard
 */
public class BadRequestException extends RuntimeException implements LocalizedMessageHolder {

    public static BadRequestException actionNotSupportedException(String action) {
        return new BadRequestException("The action [" + action + "] is not supported.", "BadRequestException.action-not-supported.message", action);
    }

    public static BadRequestException idRequestNotMatchIdBodyException(String idRequest, String idBody) {
        return new BadRequestException(
                String.format("The id %s does not match the id in the body %s.", idRequest, idBody),
                "BadRequestException.id-request-id-body-not-match.message",
                idRequest, idBody
        );
    }

    private final String messageKey;
    private final Object[] parameters;

    public BadRequestException(String message, String messageKey, Object... parameters) {
        super(message);

        this.messageKey = messageKey;
        this.parameters = parameters;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public Object[] getMessageParameters() {
        return parameters;
    }
}