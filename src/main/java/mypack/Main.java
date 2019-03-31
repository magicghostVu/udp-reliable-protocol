package mypack;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.gson.Gson;
import mypack.log.LoggingService;
import mypack.myactors.mm.HelloActor;

public class Main {
    public static void main(String[] args) {
        Person p = new Person(24, "phuvh");
        Gson gson = new Gson();
        String json = gson.toJson(p);
        System.out.println("json is " + json);
        Person p2 = gson.fromJson(json, Person.class);
        System.out.println("new p is " + p2);
        LoggingService.getInstance().getLogger().info("log from log 4j {}", 2);
        var actorSystem = ActorSystem.create();
        ActorRef ref = actorSystem.actorOf(HelloActor.props());
        ref.tell("Hello world", ActorRef.noSender());
    }
}
