package mypack.testwithnetty.servertest;

import mypack.mconfig.ServerConfig;
import mypack.servicewrap.actsys.ActorSystemContainer;
import mypack.testwithnetty.servertest.actors.SocketServerActor;

public class StartServer {
    //public static int portNetty = 10017;

    public static void main(String[] args) {
        ServerConfig.initConfig();
        ActorSystemContainer.getInstance().createNew(SocketServerActor.props());
    }
}
