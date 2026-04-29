package com.diplom.messenger.dto.websocket;

public class EventType {
    public static final String MESSAGE_NEW = "MESSAGE_NEW";
    public static final String MESSAGE_EDITED = "MESSAGE_EDITED";
    public static final String MESSAGE_DELETED = "MESSAGE_DELETED";
    public static final String MESSAGE_READ = "MESSAGE_READ";
    public static final String TYPING_START = "TYPING_START";
    public static final String TYPING_STOP = "TYPING_STOP";
    public static final String USER_ONLINE = "USER_ONLINE";
    public static final String USER_OFFLINE = "USER_OFFLINE";
    public static final String CALL_INCOMING = "CALL_INCOMING";
    public static final String CALL_ENDED = "CALL_ENDED";
}