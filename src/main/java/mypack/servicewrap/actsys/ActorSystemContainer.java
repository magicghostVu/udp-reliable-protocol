package mypack.servicewrap.actsys;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorSystemContainer {
    private static ActorSystemContainer ourInstance = new ActorSystemContainer();

    public static ActorSystemContainer getInstance() {
        return ourInstance;
    }

    private ActorSystemContainer() {
        this.actorSystem = ActorSystem.create();
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    public ActorRef createNew(Props props) {
        return actorSystem.actorOf(props);
    }

    private ActorSystem actorSystem;
}
