package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import mypack.mutils.MyUtils;
import mypack.testwithnetty.servertest.network.RawDataPackage;
import org.apache.commons.lang3.mutable.MutableShort;

// actor để giữ connect đến những client đang muốn kết nối đến server
// chưa được làm user, các actor này nên được kill đi theo thời gian

// maybe huge of work to impl
public class WaitingSessionActor extends AbstractActor {


    //todo: impl reliable system based-on acks


    //private

    private MutableShort crSequenceSend;

    private long createdTime;


    public static Props props() {
        return Props.create(WaitingSessionActor.class);
    }


    public WaitingSessionActor() {

        crSequenceSend = new MutableShort();
        createdTime = MyUtils.getCrTimeInSecond();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RawDataPackage.class, this::rawPackageCome)
                .build();
    }

    private void rawPackageCome(RawDataPackage rp) {
        var listIncluded = rp.getListPackageResend();
    }
}
