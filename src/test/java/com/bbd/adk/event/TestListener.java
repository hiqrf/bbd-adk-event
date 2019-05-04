package com.bbd.adk.event;

public class TestListener {

    @Subscribe(isAsync = true)
    public void testAsync(TestEvent testEvent) {
        System.out.println(Thread.currentThread().getName());
       System.out.println("触发事件"+testEvent);
    }

    @Subscribe()
    public void testYnc(TestEvent testEvent) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("触发事件"+testEvent);
    }

}
