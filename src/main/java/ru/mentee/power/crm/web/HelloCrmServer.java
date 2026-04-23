package ru.mentee.power.crm.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HelloCrmServer {
  private final HttpServer server;

  public HelloCrmServer(int port) throws IOException {
    this.server = HttpServer.create(new InetSocketAddress(port), 0);
  }

  public void start() {
    server.createContext("/hello", new HelloHandler());
    server.start();
    System.out.println("Server started on port: " + server.getAddress().getPort());
  }

  public void stop() {
    server.stop(0);
  }

  static class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

      String method = exchange.getRequestMethod();
      String path = exchange.getRequestURI().getPath();
      System.out.println("Received " + method + " request for " + path);

      String response = "<!DOCTYPE html>\n"
          + "<html>\n"
          + "<head><meta charset=\"UTF-8\"><title>CRM Server</title></head>\n"
          + "<body>\n"
          + "    <h1>Hello CRM!</h1>\n"
          + "    <p>Добро пожаловать в CRM систему</p>\n"
          + "</body>\n"
          + "</html>";

      exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");

      exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);

      OutputStream outputStream = exchange.getResponseBody();
      outputStream.write(response.getBytes("UTF-8"));

      outputStream.close();
      exchange.close();
    }
  }
}