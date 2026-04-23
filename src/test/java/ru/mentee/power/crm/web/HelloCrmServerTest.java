package ru.mentee.power.crm.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HelloCrmServerTest {

  private HelloCrmServer server;
  private static final int TEST_PORT = 8888;

  @Mock
  private HttpExchange mockExchange;

  @Mock
  private Headers mockHeaders;

  @Test
  void serverShouldStartAndStop() throws IOException {
    // Создаем сервер прямо в тесте
    HelloCrmServer testServer = new HelloCrmServer(TEST_PORT);

    assertThatCode(() -> testServer.start()).doesNotThrowAnyException();
    assertThatCode(() -> testServer.stop()).doesNotThrowAnyException();
  }

  @Test
  void handlerShouldReturn200() throws IOException {
    when(mockExchange.getRequestMethod()).thenReturn("GET");
    when(mockExchange.getRequestURI()).thenReturn(URI.create("/hello"));
    when(mockExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
    when(mockExchange.getResponseHeaders()).thenReturn(mockHeaders);

    new HelloCrmServer.HelloHandler().handle(mockExchange);

    verify(mockExchange).sendResponseHeaders(eq(200), anyLong());
  }

  @Test
  void handlerShouldSetContentType() throws IOException {
    when(mockExchange.getRequestMethod()).thenReturn("GET");
    when(mockExchange.getRequestURI()).thenReturn(URI.create("/hello"));
    when(mockExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
    when(mockExchange.getResponseHeaders()).thenReturn(mockHeaders);

    new HelloCrmServer.HelloHandler().handle(mockExchange);

    verify(mockHeaders).set("Content-Type", "text/html; charset=UTF-8");
  }

  @Test
  void handlerShouldReturnHtmlWithHelloCrm() throws IOException {
    when(mockExchange.getRequestMethod()).thenReturn("GET");
    when(mockExchange.getRequestURI()).thenReturn(URI.create("/hello"));
    when(mockExchange.getResponseHeaders()).thenReturn(mockHeaders);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    when(mockExchange.getResponseBody()).thenReturn(out);

    new HelloCrmServer.HelloHandler().handle(mockExchange);

    String response = out.toString("UTF-8");
    assertThat(response)
        .contains("<h1>Hello CRM!</h1>")
        .contains("Добро пожаловать в CRM систему");
  }

  @Test
  void handlerShouldCloseResources() throws IOException {
    when(mockExchange.getRequestMethod()).thenReturn("GET");
    when(mockExchange.getRequestURI()).thenReturn(URI.create("/hello"));
    when(mockExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
    when(mockExchange.getResponseHeaders()).thenReturn(mockHeaders);

    new HelloCrmServer.HelloHandler().handle(mockExchange);

    verify(mockExchange).close();
  }
}