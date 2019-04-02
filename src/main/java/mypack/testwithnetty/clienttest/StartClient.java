package mypack.testwithnetty.clienttest;

import akka.actor.ActorRef;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.clienttest.actors.ClientActor;

public class StartClient {
    public static void main(String[] args) {
        ActorRef clientActorRef = ActorSystemContainer.getInstance().createNew(ClientActor.props());
    }
}
