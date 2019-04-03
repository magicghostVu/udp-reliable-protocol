package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import mypack.log.LoggingService;
import mypack.servicewrap.actsys.ActorSystemContainer;
import org.apache.commons.lang3.mutable.MutableInt;


// actor này chỉ là cánh cổng hay là logic thật?
// tạm thời sẽ cài đặt đơn giản
public class LogicServerActor extends AbstractActor {


    public static Props props() {
        return Props.create(LogicServerActor.class);
    }


    private MutableInt autoIndexUid;

    //
    //private Map<String,>


    private static ActorRef _self;

    public LogicServerActor() {
        _self = getSelf();
        autoIndexUid = new MutableInt();
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
