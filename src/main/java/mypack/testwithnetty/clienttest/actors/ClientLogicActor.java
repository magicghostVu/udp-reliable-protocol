package mypack.testwithnetty.clienttest.actors;

import akka.actor.AbstractActor;

public class ClientLogicActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
