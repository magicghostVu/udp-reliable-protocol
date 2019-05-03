package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import io.netty.buffer.ByteBufAllocator;
import mypack.log.LoggingService;
import mypack.mutils.MyPair;
import mypack.mutils.MyUtils;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.servertest.actors.msgs.OnePackageCome;
import mypack.testwithnetty.servertest.cmd.CmdDefine;
import org.apache.commons.lang3.mutable.MutableInt;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;


// actor này chỉ là cánh cổng hay là logic thật?
// tạm thời sẽ cài đặt đơn giản
public class LogicServerActor extends AbstractActor {

    public static Props props() {
        return Props.create(LogicServerActor.class);
    }

    private MutableInt autoIndexUid;

    private MutableInt autoIndexWaitingSession;


    //user actor có thể nằm trên một jvm khác hay ở luôn node chính
    private Map<Integer, ActorRef> mapUserById;

    private Map<SocketAddress, Integer> mapUidBySocket;


    private Map<SocketAddress, MyPair<ActorRef, Long>> mapWaitingSession;


    private static ActorRef _self;

    public LogicServerActor() {
        _self = getSelf();
        autoIndexUid = new MutableInt();
        mapUserById = new HashMap<>();
        mapUidBySocket = new HashMap<>();
        autoIndexWaitingSession = new MutableInt();
        mapWaitingSession = new HashMap<>();

        //todo: thiết lập schedule để remove session và user lâu không gửi package


        LoggingService.getInstance().getLogger().info("Server logic actor ready");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OnePackageCome.class, this::newPackageComing)
                .build();
    }

    private void newPackageComing(OnePackageCome newPackage) {
        LoggingService.getInstance().getLogger().info("received package from client {}", newPackage.getAddSender());


        //todo: check xem đã có user này trong map hay chưa


        var addSender = newPackage.getAddSender();

        // đã có user này đang tồn tại
        // gửi gói tin này đến cho user đó
        if (mapUidBySocket.containsKey(addSender)) {
            int uid = mapUidBySocket.get(addSender);
            mapUserById.get(uid).tell(newPackage.getData(), getSelf());

        }
        // user chưa có trong hệ thống
        else {

            var rawDataPackage = newPackage.getData();
            var waitingSession = mapWaitingSession.get(addSender);
            //ip này đã từng gửi gói tin đến trước
            if (waitingSession != null) {
                waitingSession.getF().tell(rawDataPackage, getSelf());
                waitingSession.setS(MyUtils.getCrTimeInSecond());
            } else {
                //tạo mới
                var newSession = getContext().actorOf(WaitingSessionActor.props());
                long crTime = MyUtils.getCrTimeInSecond();
                var pairActorLastPackageTime = new MyPair<>(newSession, crTime);
                mapWaitingSession.put(addSender, pairActorLastPackageTime);
                newSession.tell(rawDataPackage, getSelf());
            }
        }


    }

    public static ActorRef getActorRef() {
        return _self;
    }

    public static void initSelf() {
        _self = ActorSystemContainer.getInstance().createNew(props());
    }
}
