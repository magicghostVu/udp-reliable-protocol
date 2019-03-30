open module mvn.java.ten {

    //apache common
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;


    // log4j2
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    //netty
    requires io.netty.all;


    // akka
    requires akka.actor;
    requires scala.library;
    requires typesafe.config;
    requires org.scala.java.eight.compat;
    requires jdk.unsupported;

    // gson
    requires java.sql;
    requires gson;

    //exports mypack;
}