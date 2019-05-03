package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import mypack.testwithnetty.servertest.network.RawDataPackage;

// actor để giữ connect đến những client đang muốn kết nối đến server
// chưa được làm user, các actor này nên được kill đi theo thời gian
public class WaitingSessionActor extends AbstractActor {


    public static Props props() {
        return Props.create(WaitingSessionActor.class);
    }


    public WaitingSessionActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawDataPackage.class, this::rawPackageCome)
                .build();
    }

    private void rawPackageCome(RawDataPackage rp) {

    }
}
