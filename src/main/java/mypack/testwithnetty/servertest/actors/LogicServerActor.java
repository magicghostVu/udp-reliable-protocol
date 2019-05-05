package mypack.testwithnetty.servertest.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import io.netty.buffer.ByteBufAllocator;
import mypack.log.LoggingService;
import mypack.mconfig.ServerConfig;
import mypack.mthread.ServiceForAsyncAndSchedule;
import mypack.mutils.MyPair;
import mypack.mutils.MyUtils;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.servertest.actors.msgs.KillIdleSession;
import mypack.testwithnetty.servertest.actors.msgs.OnePackageCome;
import mypack.testwithnetty.servertest.cmd.CmdDefine;
import org.apache.commons.lang3.mutable.MutableInt;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


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


    // long trong pair chính là createTime của session này
    // được sử dụng để khi quét, sẽ kill session nếu quá thời gian chờ


    private Map<SocketAddress, MyPair<ActorRef, Long>> mapSessionCreatedTimeByAddress;


    private static ActorRef _self;


    private ScheduledFuture<?> scheduleScanSession;

    public LogicServerActor() {
        _self = getSelf();
        autoIndexUid = new MutableInt();
        mapUserById = new HashMap<>();
        mapUidBySocket = new HashMap<>();
        autoIndexWaitingSession = new MutableInt();

        mapSessionCreatedTimeByAddress = new HashMap<>();


        //todo: thiết lập schedule để remove session và user lâu không gửi package


        LoggingService.getInstance().getLogger().info("Server logic actor ready");

        Runnable runScanIdeSessions = () ->
                getSelf().tell(new KillIdleSession(), ActorRef.noSender());

        scheduleScanSession = ServiceForAsyncAndSchedule.getInstance()
                .scheduleFixRate(runScanIdeSessions, ServerConfig.getTimeScanSession(), ServerConfig.getTimeScanSession(), TimeUnit.SECONDS);
    }


    @Override
    public void postStop() {
        scheduleScanSession.cancel(false);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OnePackageCome.class, this::newPackageComing)
                .match(KillIdleSession.class, this::killIdleSession)
                .build();
    }

    // dump version, need to improve
    private void killIdleSession(KillIdleSession k) {
        LoggingService.getInstance().getLogger().info("kill idle session run");

        //nếu không trống thì mới chạy dọn dẹp

        if (!mapSessionCreatedTimeByAddress.isEmpty()) {
            var setWillRemove = new HashSet<SocketAddress>();

            mapSessionCreatedTimeByAddress.forEach((add, p) -> {
                long timeElapsed = MyUtils.getCrTimeInSecond() - p.getS();

                //nếu đã quá thời gian thì xóa bỏ session này
                if (timeElapsed > ServerConfig.getTimeScanSession()) {
                    setWillRemove.add(add);
                }
            });

            setWillRemove.forEach(add -> {
                mapSessionCreatedTimeByAddress.get(add).getF().tell(PoisonPill.getInstance(), getSelf());
                mapSessionCreatedTimeByAddress.remove(add);
                LoggingService.getInstance().getLogger().info("session at {} removed", add);
            });
        }
    }

    //huge of message send here
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
        // user chưa có trong hệ thống, check đến session và tạo mới session
        else {
            var rawDataPackage = newPackage.getData();
            // session đã tồn tại trong hệ thống rồi, gửi gói tin đến session đó
            if (mapSessionCreatedTimeByAddress.containsKey(newPackage.getAddSender())) {
                mapSessionCreatedTimeByAddress.get(newPackage.getAddSender()).getF().tell(rawDataPackage, getSelf());
            }
            //chưa có session thì chỉ chấp nhận gói ping_pong, tất cả các gói khác sẽ discard
            else {
                short cmdId = rawDataPackage.getMajorPackage().getCmdId();

                var cmdDefine = CmdDefine.fromCode(cmdId);
                if (cmdDefine == null) {
                    LoggingService.getInstance().getLogger().warn("undefine cmd from {}", newPackage.getAddSender());
                    return;
                }
                if (cmdDefine == CmdDefine.PING_PONG) {
                    var newSession = getContext()
                            .actorOf(WaitingSessionActor.props(), "waitSession" + addSender);
                    var p = new MyPair<>(newSession, MyUtils.getCrTimeInSecond());
                    mapSessionCreatedTimeByAddress.put(addSender, p);
                    newSession.tell(rawDataPackage, getSelf());
                } else {
                    LoggingService.getInstance().getLogger().warn("wrong protocol, new session can only send ping pong");
                    //todo: nếu có nhiều gói kiểu này quá từ một address thì nên block address này trong một khoảng thời gian
                }
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
