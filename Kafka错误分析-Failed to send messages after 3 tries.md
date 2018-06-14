# Kafka错误分析-Failed to send messages after 3 tries
如果是在同一台机器上（localhost），kafka的consumer、producer都能正常的发送和接收信息，但是若是用java api远程连接kafka服务器，则默认配置不能正常工作。会出现"kafka.common.FailedToSendMessageException: Failed to send messages after 3 tries"的错误。
```
[2015-07-02 15:15:39,295] WARN Error while fetching metadata [{TopicMetadata for topic datadog-dev ->   
No partition metadata for topic datadog-dev due to kafka.common.LeaderNotAvailableException}] for topic [datadog-dev]: class kafka.common.LeaderNotAvailableException  (kafka.producer.BrokerPartitionInfo)  
[2015-07-02 15:15:39,295] ERROR Failed to collate messages by topic, partition due to: Failed to fetch topic metadata for topic: datadog-dev (kafka.producer.async.DefaultEventHandler)  
[2015-07-02 15:15:39,399] WARN Error while fetching metadata [{TopicMetadata for topic datadog-dev ->   
No partition metadata for topic datadog-dev due to kafka.common.LeaderNotAvailableException}] for topic [datadog-dev]: class kafka.common.LeaderNotAvailableException  (kafka.producer.BrokerPartitionInfo)  
[2015-07-02 15:15:39,399] ERROR Failed to send requests for topics datadog-dev with correlation ids in [9,16] (kafka.producer.async.DefaultEventHandler)  
[2015-07-02 15:15:39,399] ERROR Error in handling batch of 4 events (kafka.producer.async.ProducerSendThread)  
kafka.common.FailedToSendMessageException: Failed to send messages after 3 tries.  
  at kafka.producer.async.DefaultEventHandler.handle(DefaultEventHandler.scala:90)  
  at kafka.producer.async.ProducerSendThread.tryToHandle(ProducerSendThread.scala:104)  
  at kafka.producer.async.ProducerSendThread$$anonfun$processEvents$3.apply(ProducerSendThread.scala:87)  
  at kafka.producer.async.ProducerSendThread$$anonfun$processEvents$3.apply(ProducerSendThread.scala:67)  
  at scala.collection.immutable.Stream.foreach(Stream.scala:547)  
  at kafka.producer.async.ProducerSendThread.processEvents(ProducerSendThread.scala:66)  
  at kafka.producer.async.ProducerSendThread.run(ProducerSendThread.scala:44)  
```
解决方法其实很简单，只需要在Kafka的配置文件server.properties中，设置好主机名即可
```
listeners=PLAINTEXT://192.168.133.129:9092
```
究其原因，其实从注释里，我们可以知道，这是一个指定broker的地址（严格来说是所监听的网络接口，或者网卡），同时，也可以看出它还和下面的属性相关。
```
# Hostname and port the broker will advertise to producers and consumers. If not set, 
# it uses the value for "listeners" if configured.  Otherwise, it will use the value
# returned from java.net.InetAddress.getCanonicalHostName().
#advertised.listeners=PLAINTEXT://your.host.name:9092
```
也就是说，producer和consumer是通过这个主机名（advertised.host.name）来连接broker的，而如果这个值没有设置，则会使用上面的host.name的值，如果上面的host.name也没有设置，则会使用java.net.InetAddress.getCanonicalHostName()获取的值。默认的时候该broker是localhost，从其他机器访问当然不可能成功。
  

