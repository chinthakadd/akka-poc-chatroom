package com.chinthakad.akka;

import akka.actor.typed.ActorSystem;
import com.chinthakad.akka.behavior.MainSystemActor;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // Launch actor system
        final ActorSystem<MainSystemActor.SystemCommand> chatSystem = ActorSystem.create(
                MainSystemActor.create(), "akka-poc-chatroom"
        );

        // Check Health
        chatSystem.tell(new MainSystemActor.HealthCheckRequest());

        // Create Chat Rooms
        chatSystem.tell(new MainSystemActor.CreateChatRoom("tech-discussions"));
        chatSystem.tell(new MainSystemActor.CreateChatRoom("gossips"));

    }
}
