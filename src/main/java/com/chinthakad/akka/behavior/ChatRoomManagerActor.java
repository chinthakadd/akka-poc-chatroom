package com.chinthakad.akka.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.chinthakad.akka.model.ChatRoom;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatRoomManagerActor extends AbstractBehavior<ChatRoomManagerActor.ChatRoomCommand> {

    private ChatRoom chatRoom;

    public ChatRoomManagerActor(ActorContext<ChatRoomCommand> context, String name) {
        super(context);
        chatRoom = new ChatRoom(name);
    }

    public static Behavior<ChatRoomCommand> create(String name) {
        return Behaviors.setup(context -> new ChatRoomManagerActor(context, name));
    }

    @Override
    public Receive<ChatRoomCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(UserRequest.class, this::onUserRequest)
                .onMessage(SendMessageCommand.class, this::onSendMessage)
                .build();
    }

    private Behavior<ChatRoomCommand> onUserRequest(UserRequest userRequest) {
        getContext().spawn(UserActor.create(userRequest.username, 0, this.getContext().getSelf()),
                "actor-" + userRequest.username);
        return this;
    }

    private Behavior<ChatRoomCommand> onSendMessage(SendMessageCommand messageCommand){
        log.info("Send Message: {}", messageCommand.messsage);
        return this;
    }
    public interface ChatRoomCommand {
    }

    @Value
    public static class SendMessageCommand implements ChatRoomCommand {
        @NonNull String messsage;
        @NonNull ActorRef<UserActor.UserCommand> sentBy;
    }

    @Value
    public static class UserRequest implements ChatRoomCommand {
        @NonNull String username;
        @NonNull UserRequestType type;
    }

    public enum UserRequestType {
        JOIN, LEAVE
    }

}
