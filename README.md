## bbd-adk-event
## 事件监听机分发

## 1、简介

  事件监听分发模块基于观察者模式提供了事件监听分发功能，同时提供同步与异步监听与spring框架无缝集成。
  
## 2、使用方法

  # 1:依耐：
  
   请在pom中加入以下依耐：
   
      <dependency>
            <groupId>com.bbd</groupId>
            <artifactId>bbd-adk-event</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
  # 2：配置
  
      请在application.properties中至少配置bbd.adk.event.enabel=true，其他配置请参考:EventProperties类
      
   # 3：注入NotifierBus(在容器启动的时候自动创建，无需配置)
   
   @Autowired
    private NotifierBus notifierBus;
    
   # 4：触发事件
   
     notifierBus.dispatcher(new DemoEvent()
                .setEventName("事件测试"));
                
   # 5：编写监听器
     
     @Listener
     public class DemoListener {

    @Subscribe(isAsync = true)
    public void testAsync(DemoController.DemoEvent event) {
        System.out.println(Thread.currentThread().getName()+":"+event.getEventName());
    }
    @Subscribe()
    public void testSync(DemoController.DemoEvent event) {
        System.out.println(Thread.currentThread().getName()+":"+event.getEventName());
    }
}
     
