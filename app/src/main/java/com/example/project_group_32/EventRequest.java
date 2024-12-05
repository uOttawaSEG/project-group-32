package com.example.project_group_32;

public class EventRequest {
    private final long id, event_id, user_id;
    private final Request.Status status;

    EventRequest(long id, long event_id, long user_id, Request.Status status) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public long getEventId() {
        return event_id;
    }

    public long getUserId() {
        return user_id;
    }

    public Request.Status getStatus() {
        return status;
    }
}
