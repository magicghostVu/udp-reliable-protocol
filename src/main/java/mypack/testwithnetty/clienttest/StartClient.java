package mypack.testwithnetty.clienttest;

import akka.actor.ActorRef;
import mypack.mconfig.ServerConfig;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.clienttest.actors.ClientNetWorkActor;
import mypack.testwithnetty.clienttest.actors.msgs.TestSendPackageToServer;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StartClient {
    public static void main(String[] args) {
        ServerConfig.initConfig();
        var clientActorRef = ActorSystemContainer.getInstance()
                .createNew(ClientNetWorkActor.props());
        var scanner = new Scanner(System.in);
        /*LoggingService.getInstance().getLogger()
                .info("press enter to send some package to server");
        scanner.next();*/

        var scheduleExe = Executors.newSingleThreadScheduledExecutor();

        Runnable r = () -> {
            clientActorRef.tell(new TestSendPackageToServer(), ActorRef.noSender());
        };

        scheduleExe.scheduleAtFixedRate(r, 5000, 500, TimeUnit.MILLISECONDS);


    }
}
