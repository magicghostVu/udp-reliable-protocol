package mypack.testwithnetty.servertest;

import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.servertest.actors.SocketServerActor;

public class StartServer {
    public static int portNetty = 9098;

    public static void main(String[] args) {
        ActorSystemContainer.getInstance().createNew(SocketServerActor.props());
        //ActorSystemContainer.getInstance().createNew(LogicServerActor.props());
    }
}
