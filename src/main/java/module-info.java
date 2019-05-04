open module mvn.java.ten {

    //apache common io
    requires org.apache.commons.io;
    // common lang3
    requires org.apache.commons.lang3;

    requires org.apache.commons.collections4;


    // log4j2
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    //netty
    requires io.netty.all;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;


    // akka
    requires akka.actor;
    requires scala.library;
    requires typesafe.config;
    requires org.scala.java.eight.compat;
    requires jdk.unsupported;




    // gson
    requires java.sql;
    requires gson;
}