  package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.example.listeners.ActiveMQMessageListener;

  public class ActiveMQRoute extends RouteBuilder {

  private final String routeId;
  private final String queueName;
  private final ActiveMQMessageListener listener;

  public ActiveMQRoute(String routeId, String queueName, ActiveMQMessageListener listener) {
    this.routeId = routeId;
    this.queueName = queueName;
    this.listener = listener;
  }

  @Override
  public void configure() throws Exception {
    from("activemq:" + queueName)
        .routeId(routeId)
        .log("yo!")
        .log("${body}")
        .process(exchange -> listener.onMessage(exchange.getMessage().getBody(String.class)))
        .onException(Exception.class)
          .maximumRedeliveries(1);
  }
}
