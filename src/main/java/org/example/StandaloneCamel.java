package org.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.example.listeners.OrderCreatedListener;
import org.example.listeners.OrderDeletedListener;

public class StandaloneCamel {

  public static void main(String[] args) throws Exception {
    // create a new CamelContext
    try (CamelContext camelContext = new DefaultCamelContext()) {

      // configure activemq component
      ActiveMQConnectionFactory connectionFactory = configureConnectionFactory();
      ActiveMQComponent activeMQComponent = new ActiveMQComponent();
      activeMQComponent.setConnectionFactory(connectionFactory);
      camelContext.addComponent("activemq", activeMQComponent);

      // instantiate different routes
      var orderInsertedRoute = new ActiveMQRoute("myFirstRoute", "order.inserted", new OrderCreatedListener());
      var orderCreatedRoute = new ActiveMQRoute("myOtherRoute", "order.aborted", new OrderDeletedListener());

      // add routes to Camel
      camelContext.addRoutes(orderInsertedRoute);
      camelContext.addRoutes(orderCreatedRoute);

      // start Camel
      camelContext.start();

      // just run for 10 seconds and stop
      System.out.println("Running for 10 seconds and then stopping");
      Thread.sleep(300000);
    }
  }

  private static ActiveMQConnectionFactory configureConnectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL("tcp://localhost:61616");
    connectionFactory.setUserName("system");
    connectionFactory.setPassword("manager");
    connectionFactory.setNonBlockingRedelivery(true);
    return connectionFactory;
  }

}
