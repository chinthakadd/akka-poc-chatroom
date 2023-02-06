package com.chinthakad.akka.behavior;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.TimerScheduler;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;
import com.chinthakad.akka.model.User;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;

public class UserActor extends AbstractBehavior<UserActor.UserCommand> {

    private final User user;

    private UserActor(ActorContext<UserCommand> context, String username, int messageCount,
                      ActorRef<ChatRoomManagerActor.ChatRoomCommand> chatRoomActorRef,
                      TimerScheduler<UserCommand> timer) {
        super(context);
        this.user = new User(username, 0, chatRoomActorRef);
        timer.startSingleTimer(new AutoChatCommand("Hello from: " + username), Duration.ofSeconds(5));
    }

    public static Behavior<UserCommand> create(String username, int messageCount,
                                               ActorRef<ChatRoomManagerActor.ChatRoomCommand> chatRoomActorRef) {
        return Behaviors.setup(new Function<ActorContext<UserCommand>, Behavior<UserCommand>>() {
            @Override
            public Behavior<UserCommand> apply(ActorContext<UserCommand> context) throws Exception, Exception {
                return Behaviors.withTimers(new Function<TimerScheduler<UserCommand>, Behavior<UserCommand>>() {
                    @Override
                    public Behavior<UserCommand> apply(TimerScheduler<UserCommand> timer) throws Exception, Exception {
                        return new UserActor(context, username, messageCount, chatRoomActorRef, timer);
                    }
                });
            }
        });
    }

    @Override
    public Receive<UserCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(AutoChatCommand.class, this::onAutoChatCommand)
                .build();
    }

    Behavior<UserCommand> onAutoChatCommand(AutoChatCommand command) {
        // send a message to the chat room.
        user.getChatRoomActorRef().tell(new ChatRoomManagerActor.SendMessageCommand(command.message, getContext().getSelf()));
        return create(user.getUsername(), user.getSentMessageCount() + 1, this.user.getChatRoomActorRef());
    }

    public interface UserCommand {
    }


    @Value
    public static class AutoChatCommand implements UserCommand {
        @NonNull String message;
    }
}
