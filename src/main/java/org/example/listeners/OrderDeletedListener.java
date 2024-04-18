package org.example.listeners;

public class OrderDeletedListener implements ActiveMQMessageListener {

    @Override
    public void onMessage(String message) {
      System.out.println("I'm " + this.getClass().getName() + ", received " + message);
    }
  }
