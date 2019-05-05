package mypack.uifxtestnetwork.actors;

import akka.actor.AbstractActor;


// để actor này làm network luôn hay chỉ là actor
public class ActorHandleNetwork extends AbstractActor {

    public ActorHandleNetwork() {


        //tạo mới 1 thread làm reader

        // nhưng lại cần chia sẻ datagram socket/hoặc data gram channel

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
