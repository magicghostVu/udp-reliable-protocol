package mypack.testwithnetty.clienttest;

import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.clienttest.actors.ClientNetWorkActor;

public class StartClient {
    public static void main(String[] args) {
        var clientActorRef = ActorSystemContainer.getInstance().createNew(ClientNetWorkActor.props());

    }
}
