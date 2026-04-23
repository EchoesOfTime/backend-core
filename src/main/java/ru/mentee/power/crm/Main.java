package ru.mentee.power.crm;

import ru.mentee.power.crm.web.HelloCrmServer;

public class Main {

  public static void main(String[] args) throws Exception {
    int port = 8080;
    HelloCrmServer server = new HelloCrmServer(port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Stopping server...");
      server.stop();
      System.out.println("Server is halted");
    }));

    server.start();
    System.out.println("The server is running on the port " + port);
    System.out.println("To stop press Ctrl+C");

    Thread.currentThread().join();
  }
}