package com.bbd.adk.event;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;

public class Tester {

    public static void main(String[] args) {
        System.out.println("主线程:" + Thread.currentThread().getName());
        Notifier notifier = new NotifierBus(Executors.newFixedThreadPool(2));
        notifier.register(new TestListener());
        notifier.dispatcher(new TestEvent());
    }
}
