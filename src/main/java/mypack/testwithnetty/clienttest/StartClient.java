package mypack.testwithnetty.clienttest;

import akka.actor.ActorRef;
import mypack.log.LoggingService;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.clienttest.actors.ClientNetWorkActor;
import mypack.testwithnetty.clienttest.actors.msgs.TestSendPackageToServer;

import java.util.Scanner;

public class StartClient {
    public static void main(String[] args) {
        var clientActorRef = ActorSystemContainer.getInstance()
                .createNew(ClientNetWorkActor.props());
        var scanner = new Scanner(System.in);
        LoggingService.getInstance().getLogger()
                .info("press enter to send some package to server");
        scanner.next();
        clientActorRef.tell(new TestSendPackageToServer(), ActorRef.noSender());

    }
}
