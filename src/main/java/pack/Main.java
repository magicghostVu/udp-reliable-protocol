package pack;

import akka.actor.ActorSystem;
import com.google.gson.Gson;
import pack.log.LoggingService;
import scala.compat.java8.JFunction;

public class Main {
    public static void main(String[] args) {
        Person p = new Person(24, "phuvh");
        Gson gson = new Gson();
        String json = gson.toJson(p);
        System.out.println("json is " + json);
        Person p2 = gson.fromJson(json, Person.class);
        System.out.println("new p is " + p2);

        LoggingService.getInstance().getLogger().info("log from log 4j {}", 2);
        ActorSystem.create();

        JFunction function;

        //DataGra
    }
}
