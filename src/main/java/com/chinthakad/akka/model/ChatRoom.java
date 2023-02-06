package com.chinthakad.akka.model;

import akka.actor.typed.ActorRef;
import com.chinthakad.akka.behavior.UserActor;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
@Data
public class ChatRoom {

    @NonNull private String name;
    private Map<String, ActorRef<UserActor.UserCommand>> onlineUsers = new HashMap<>();

}
