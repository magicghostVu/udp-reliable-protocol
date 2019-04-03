package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import mypack.log.LoggingService;
import mypack.servicewrap.actsys.ActorSystemContainer;

public class LogicServerActor extends AbstractActor {


    public static Props props() {
        return Props.create(LogicServerActor.class);
    }

    private static ActorRef _self;

    public LogicServerActor() {
        _self = getSelf();
        LoggingService.getInstance().getLogger().info("Server logic actor ready");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .build();
    }


    public static ActorRef getActorRef() {
        return _self;
    }

    public static void initSelf() {
        _self = ActorSystemContainer.getInstance().createNew(props());
    }
}
