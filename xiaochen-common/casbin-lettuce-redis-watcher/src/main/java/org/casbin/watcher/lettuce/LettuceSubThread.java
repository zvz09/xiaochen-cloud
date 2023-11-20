package org.casbin.watcher.lettuce;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * @author Shingmo Yeung
 */
@Slf4j
public class LettuceSubThread extends Thread {
    private final String channel;
    private final LettuceSubscriber lettuceSubscriber;
    private final AbstractRedisClient abstractRedisClient;
    private StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection;

    /**
     * Construction method
     *
     * @param abstractRedisClient abstractRedisClient
     * @param channel             channel
     * @param updateCallback      updateCallback
     */
    public LettuceSubThread(AbstractRedisClient abstractRedisClient, String channel, Runnable updateCallback) {
        super("LettuceSubThread");
        this.channel = channel;
        this.abstractRedisClient = abstractRedisClient;
        lettuceSubscriber = new LettuceSubscriber(updateCallback);
    }

    /**
     * set runnable
     *
     * @param runnable runnable
     */
    public void setUpdateCallback(Runnable runnable) {
        lettuceSubscriber.setUpdateCallback(runnable);
    }

    /**
     * set consumer
     *
     * @param consumer runnable
     */
    public void setUpdateCallback(Consumer<String> consumer) {
        lettuceSubscriber.setUpdateCallback(consumer);
    }

    @Override
    public void run() {
        try {
            this.statefulRedisPubSubConnection = this.getStatefulRedisPubSubConnection(this.abstractRedisClient);
            if (this.statefulRedisPubSubConnection.isOpen()) {
                this.statefulRedisPubSubConnection.addListener(new RedisPubSubListener<String, String>() {
                    @Override
                    public void unsubscribed(String channel, long count) {
                        log.info("[unsubscribed] {}", channel);
                    }

                    @Override
                    public void subscribed(String channel, long count) {
                        log.info("[subscribed] {}", channel);
                    }

                    @Override
                    public void punsubscribed(String pattern, long count) {
                        log.info("[punsubscribed] {}", pattern);
                    }

                    @Override
                    public void psubscribed(String pattern, long count) {
                        log.info("[psubscribed] {}", pattern);
                    }

                    @Override
                    public void message(String pattern, String channel, String message) {
                        log.info("[message] {} -> {} -> {}", pattern, channel, message);
                        lettuceSubscriber.onMessage(channel, message);
                    }

                    @Override
                    public void message(String channel, String message) {
                        log.info("[message] {} -> {}", channel, message);
                        lettuceSubscriber.onMessage(channel, message);
                    }
                });
                this.statefulRedisPubSubConnection.async().subscribe(this.channel);

                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("error message {}", e.getMessage());
            this.close(this.statefulRedisPubSubConnection);
        }
    }

    /**
     * Close Redis PubSub Connection
     *
     * @param statefulRedisPubSubConnection Redis PubSub Connection
     */
    private void close(StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection) {
        if (statefulRedisPubSubConnection.isOpen()) {
            statefulRedisPubSubConnection.closeAsync();
        }
    }

    /**
     * Get Redis PubSub Connection
     *
     * @param abstractRedisClient Redis Client
     * @return StatefulRedisPubSubConnection
     */
    private StatefulRedisPubSubConnection<String, String> getStatefulRedisPubSubConnection(AbstractRedisClient abstractRedisClient) {
        if (abstractRedisClient instanceof RedisClient) {
            return ((RedisClient) abstractRedisClient).connectPubSub();
        } else {
            return ((RedisClusterClient) abstractRedisClient).connectPubSub();
        }
    }
}
