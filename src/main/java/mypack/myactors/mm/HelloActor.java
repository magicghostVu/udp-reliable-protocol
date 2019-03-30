package mypack.myactors.mm;

import akka.actor.AbstractActor;
import akka.actor.Props;
import mypack.log.LoggingService;

public class HelloActor extends AbstractActor {

    public static Props props() {
        return Props.create(HelloActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, str -> {
                    LoggingService.getInstance().getLogger().info("msg is {}", str);
                })
                .build();
    }
}
