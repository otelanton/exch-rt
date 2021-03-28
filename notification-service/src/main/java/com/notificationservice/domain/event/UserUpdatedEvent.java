package com.notificationservice.domain.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@ToString
@Setter
public class UserUpdatedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userUpdatedEventID;
    private long userID;
    private String username;
    private String email;


    public String getUsername() {
        return username != null ? username : "";
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public long getUserID() {
        return userID;
    }
}
