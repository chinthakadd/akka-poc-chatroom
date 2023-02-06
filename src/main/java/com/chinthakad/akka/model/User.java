package com.chinthakad.akka.model;

import akka.actor.typed.ActorRef;
import com.chinthakad.akka.behavior.ChatRoomManagerActor;
import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    @NonNull
    private String username;
    @NonNull
    private Integer sentMessageCount;

    @NonNull
    private ActorRef<ChatRoomManagerActor.ChatRoomCommand> chatRoomActorRef;
}
