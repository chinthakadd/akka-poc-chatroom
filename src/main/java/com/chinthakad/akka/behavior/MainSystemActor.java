package com.chinthakad.akka.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Main System is the main actor of this system and launches ChatRooms.
 * It keeps a track of Chat Rooms that are created.
 */
@Slf4j
public class MainSystemActor extends AbstractBehavior<MainSystemActor.SystemCommand> {

    private final Map<String, ActorRef> chatRooms = new HashMap<>();

    private MainSystemActor(ActorContext<SystemCommand> context) {
        super(context);
    }

    public static Behavior<SystemCommand> create() {
        return Behaviors.setup(MainSystemActor::new);
    }

    public Receive<SystemCommand> createReceive() {
        return newReceiveBuilder().onMessage(HealthCheckRequest.class, this::onHello)
                .onMessage(CreateChatRoom.class, this::onCreateChatRoom).build();
    }

    private Behavior<SystemCommand> onHello(HealthCheckRequest request) {
        log.info("Hello to you! ChatRoom Actor System is Active");
        return this;
    }

    Behavior<SystemCommand> onCreateChatRoom(final CreateChatRoom command) {
        ActorRef<ChatRoomManagerActor.ChatRoomCommand> chatRoomActorRef =
                getContext().spawn(ChatRoomManagerActor.create(command.name), "actor-" + command.name);

        // For Simulation Purposes, I will create a couple of users here.
        chatRoomActorRef.tell(new ChatRoomManagerActor.UserRequest(command.name + "-Chinthaka",
                ChatRoomManagerActor.UserRequestType.JOIN));

        chatRoomActorRef.tell(new ChatRoomManagerActor.UserRequest(command.name + "-Dilini",
                ChatRoomManagerActor.UserRequestType.JOIN));

        return this;
    }

    public interface SystemCommand {
    }

    @Value
    public static final class HealthCheckRequest implements SystemCommand {
    }

    /**
     * Marked as @Value to make it immutable.
     */
    @Value
    public static final class CreateChatRoom implements SystemCommand {
        @NonNull String name;
    }
}
