package com.zvz09.xiaochen.log.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {


    public static final String EXCHANGE_TOPICS="exchange_topics_operation_log";

    public static final String QUEUE_INFORM_LOG = "queue_operation_log";
    public static final String ROUTING_KEY_LOG="operation.#.log.#";

    //声明交换机
    @Bean(EXCHANGE_TOPICS)
    public Exchange exchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_LOG)
    public Queue queue(){
        return new Queue(QUEUE_INFORM_LOG);
    }

    //队列绑定交换机
    @Bean
    public Binding binding(@Qualifier(QUEUE_INFORM_LOG) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_LOG).noargs();
    }


}
