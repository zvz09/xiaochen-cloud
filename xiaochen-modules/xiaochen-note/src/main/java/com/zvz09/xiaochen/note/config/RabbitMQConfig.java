package com.zvz09.xiaochen.note.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_TOPICS="exchange_topics_reptile_rule";

    public static final String QUEUE_INFORM_ADD = "reptile_data_parser_add";
    public static final String QUEUE_INFORM_REMOVE = "reptile_data_parser_remove";
    public static final String ROUTING_KEY_ADD = "reptile.strategy.add";
    public static final String ROUTING_KEY_REMOVE = "reptile.strategy.remove";

    //声明交换机
    @Bean(EXCHANGE_TOPICS)
    public Exchange exchange(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_ADD)
    public Queue addQueue(){
        return new Queue(QUEUE_INFORM_ADD);
    }
    @Bean(QUEUE_INFORM_REMOVE)
    public Queue removeQueue(){
        return new Queue(QUEUE_INFORM_REMOVE);
    }

    //队列绑定交换机
    @Bean
    public Binding bindingAdd(@Qualifier(QUEUE_INFORM_ADD) Queue queue,
                           @Qualifier(EXCHANGE_TOPICS) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_ADD).noargs();
    }
    @Bean
    public Binding bindingRemove(@Qualifier(QUEUE_INFORM_REMOVE) Queue queue,
                              @Qualifier(EXCHANGE_TOPICS) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_REMOVE).noargs();
    }


}
