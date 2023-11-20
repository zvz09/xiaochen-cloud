package org.casbin.adapter;

import cn.hutool.core.collection.CollUtil;
import com.zvz09.xiaochen.common.core.util.JacksonUtil;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.RedisClusterURIUtil;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.casbin.adapter.constants.Constant;
import org.casbin.adapter.domain.CasbinRule;
import org.casbin.jcasbin.model.Assertion;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.BatchAdapter;
import org.casbin.jcasbin.persist.Helper;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zvz09
 */
@Slf4j
public class RedisAdapter implements Adapter, BatchAdapter {

    private String key;
    private final AbstractRedisClient abstractRedisClient;

    /**
     * Constructor
     *
     * @param redisIp   Redis IP
     * @param redisPort Redis Port
     */
    public RedisAdapter(String redisIp, Integer redisPort) {
        this(redisIp, redisPort, 2000, null, Constant.DEFAULT_ADAPTER_KEY);
    }

    /**
     * Constructor
     *
     * @param redisIp   Redis IP
     * @param redisPort Redis Port
     */
    public RedisAdapter(String redisIp, Integer redisPort, String password) {
        this(redisIp, redisPort, 2000, password, Constant.DEFAULT_ADAPTER_KEY);
    }

    /**
     * Constructor
     *
     * @param redisIp   Redis IP
     * @param redisPort Redis Port
     * @param timeout   Redis Timeout
     * @param password  Redis Password
     * @param key       Redis key
     */
    public RedisAdapter(String redisIp, Integer redisPort, int timeout, String password, String key) {
        this.abstractRedisClient = this.getLettuceRedisClient(redisIp, redisPort, null, password, timeout, Constant.LETTUCE_REDIS_TYPE_STANDALONE);
        this.key = key;
    }

    /**
     * Constructor
     *
     * @param nodes    Redis Nodes
     * @param timeout  Redis Timeout
     * @param password Redis Password
     * @param key      Redis key
     */
    public RedisAdapter(String nodes, Integer timeout, String password, String key) {
        this.abstractRedisClient = this.getLettuceRedisClient(null, null, nodes, password, timeout, Constant.LETTUCE_REDIS_TYPE_CLUSTER);
    }


    @Override
    public void addPolicies(String sec, String ptype, List<List<String>> rules) {
        for (List<String> rule : rules) {
            addPolicy(sec, ptype, rule);
        }
    }

    @Override
    public void removePolicies(String sec, String ptype, List<List<String>> rules) {
        for (List<String> rule : rules) {
            removePolicy(sec, ptype, rule);
        }
    }

    @Override
    public void loadPolicy(Model model) {
        try (StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection =
                     this.getStatefulRedisPubSubConnection(this.abstractRedisClient)) {
            if (statefulRedisPubSubConnection.isOpen()) {
                Long length = statefulRedisPubSubConnection.sync().llen(this.key);
                if (length == null) {
                    return;
                }
                List<String> policies = statefulRedisPubSubConnection.sync().lrange(this.key, 0, length);
                for (String policy : policies) {
                    CasbinRule rule = JacksonUtil.readValue(policy, CasbinRule.class);
                    loadPolicyLine(rule, model);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("loadPolicy error", e);
        }
    }

    @Override
    public void savePolicy(Model model) {
        try (StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection =
                     this.getStatefulRedisPubSubConnection(this.abstractRedisClient)) {
            if (statefulRedisPubSubConnection.isOpen()) {
                statefulRedisPubSubConnection.sync().del(this.key);
                extracted(statefulRedisPubSubConnection, model, "p");
                extracted(statefulRedisPubSubConnection, model, "g");
            }
        }
    }

    @Override
    public void addPolicy(String sec, String ptype, List<String> rule) {
        if (CollUtil.isEmpty(rule)) {
            return;
        }
        CasbinRule line = savePolicyLine(ptype, rule);
        try (StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection =
                     this.getStatefulRedisPubSubConnection(this.abstractRedisClient)) {
            if (statefulRedisPubSubConnection.isOpen()) {
                statefulRedisPubSubConnection.sync().rpush(this.key, JacksonUtil.writeValueAsString(line));
            }
        }
    }

    @Override
    public void removePolicy(String sec, String ptype, List<String> rule) {
        if (CollUtil.isEmpty(rule)) {
            return;
        }
        CasbinRule line = savePolicyLine(ptype, rule);
        try (StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection =
                     this.getStatefulRedisPubSubConnection(this.abstractRedisClient)) {
            if (statefulRedisPubSubConnection.isOpen()) {
                statefulRedisPubSubConnection.sync().lrem(this.key, 1, JacksonUtil.writeValueAsString(line));
            }
        }
    }

    @Override
    public void removeFilteredPolicy(String sec, String ptype, int fieldIndex, String... fieldValues) {
        List<String> values = Optional.of(Arrays.asList(fieldValues)).orElse(new ArrayList<>());
        if (CollUtil.isEmpty(values)) {
            return;
        }

        String regexRule = "";
        for (int i = 0; i < values.size(); ++i) {
            regexRule += "v" + fieldIndex + ":" + values.get(i) + (i + 1 == values.size() ? "" : ",");
            fieldIndex++;
        }
        try (StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection =
                     this.getStatefulRedisPubSubConnection(this.abstractRedisClient)) {
            if (statefulRedisPubSubConnection.isOpen()) {
                List<String> rulesMatch = statefulRedisPubSubConnection.sync().lrange(this.key, 0, -1);
                statefulRedisPubSubConnection.sync().ltrim(this.key, 1, 0);
                String finalRegexRule = ".*" + regexRule + ".*";
                rulesMatch.forEach(rule -> {
                    String tempRule = rule.replaceAll("[\\{ | \\} | \"]", "");
                    if (!tempRule.matches(finalRegexRule)) {
                        statefulRedisPubSubConnection.sync().rpush(this.key, rule);
                    }
                });
            }
        }

    }

    /**
     * Initialize the Redis Client
     *
     * @param host     Redis Host
     * @param port     Redis Port
     * @param nodes    Redis Nodes
     * @param password Redis Password
     * @param timeout  Redis Timeout
     * @param type     Redis Type (standalone | cluster) default:standalone
     * @return AbstractRedisClient
     */
    private AbstractRedisClient getLettuceRedisClient(String host, Integer port, String nodes, String password, int timeout, String type) {
        if (StringUtils.isNotEmpty(type) && StringUtils.equalsAnyIgnoreCase(type,
                Constant.LETTUCE_REDIS_TYPE_STANDALONE, Constant.LETTUCE_REDIS_TYPE_CLUSTER)) {
            ClientResources clientResources = DefaultClientResources.builder()
                    .ioThreadPoolSize(4)
                    .computationThreadPoolSize(4)
                    .build();
            if (StringUtils.equalsIgnoreCase(type, Constant.LETTUCE_REDIS_TYPE_STANDALONE)) {
                // standalone
                RedisURI redisUri = null;
                if (StringUtils.isNotEmpty(password)) {
                    redisUri = RedisURI.builder()
                            .withHost(host)
                            .withPort(port)
                            .withPassword(password.toCharArray())
                            .withTimeout(Duration.of(timeout, ChronoUnit.SECONDS))
                            .build();
                } else {
                    redisUri = RedisURI.builder()
                            .withHost(host)
                            .withPort(port)
                            .withTimeout(Duration.of(timeout, ChronoUnit.SECONDS))
                            .build();
                }
                ClientOptions clientOptions = ClientOptions.builder()
                        .autoReconnect(true)
                        .pingBeforeActivateConnection(true)
                        .build();
                RedisClient redisClient = RedisClient.create(clientResources, redisUri);
                redisClient.setOptions(clientOptions);
                return redisClient;
            } else {
                // cluster
                TimeoutOptions timeoutOptions = TimeoutOptions.builder().fixedTimeout(Duration.of(timeout, ChronoUnit.SECONDS)).build();
                ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                        .enablePeriodicRefresh(Duration.ofMinutes(10))
                        .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.values())
                        .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
                        .build();
                ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                        .autoReconnect(true)
                        .timeoutOptions(timeoutOptions)
                        .topologyRefreshOptions(topologyRefreshOptions)
                        .pingBeforeActivateConnection(true)
                        .validateClusterNodeMembership(true)
                        .build();
                // Redis Cluster Node
                String redisUri = StringUtils.isNotEmpty(password) ?
                        Constant.REDIS_URI_PREFIX.concat(password).concat(Constant.REDIS_URI_PASSWORD_SPLIT).concat(nodes) :
                        Constant.REDIS_URI_PREFIX.concat(nodes);
                log.info("Redis Cluster Uri: {}", redisUri);
                List<RedisURI> redisURIList = RedisClusterURIUtil.toRedisURIs(URI.create(redisUri));
                RedisClusterClient redisClusterClient = RedisClusterClient.create(clientResources, redisURIList.get(0));
                redisClusterClient.setOptions(clusterClientOptions);
                return redisClusterClient;
            }
        } else {
            throw new IllegalArgumentException("Redis-Type is required and can only be [standalone] or [cluster]");
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

    private void extracted(StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection, Model model, String type) {
        for (Map.Entry<String, Assertion> entry : model.model.get(type).entrySet()) {
            String ptype = entry.getKey();
            Assertion ast = entry.getValue();

            for (List<String> rule : ast.policy) {
                CasbinRule line = savePolicyLine(ptype, rule);
                statefulRedisPubSubConnection.sync().rpush(this.key, JacksonUtil.writeValueAsString(line));
            }
        }
    }

    private void loadPolicyLine(CasbinRule line, Model model) {
        String lineText = line.getPtype();
        if (!"".equals(line.getV0()) && line.getV0() != null) {
            lineText += ", " + line.getV0();
        }
        if (!"".equals(line.getV1()) && line.getV1() != null) {
            lineText += ", " + line.getV1();
        }
        if (!"".equals(line.getV2()) && line.getV2() != null) {
            lineText += ", " + line.getV2();
        }
        if (!"".equals(line.getV3()) && line.getV3() != null) {
            lineText += ", " + line.getV3();
        }
        if (!"".equals(line.getV4()) && line.getV4() != null) {
            lineText += ", " + line.getV4();
        }
        if (!"".equals(line.getV5()) && line.getV5() != null) {
            lineText += ", " + line.getV5();
        }

        Helper.loadPolicyLine(lineText, model);
    }

    private CasbinRule savePolicyLine(String ptype, List<String> rule) {
        CasbinRule line = new CasbinRule();

        line.setPtype(ptype);
        if (rule.size() > 0) {
            line.setV0(rule.get(0));
        }
        if (rule.size() > 1) {
            line.setV1(rule.get(1));
        }
        if (rule.size() > 2) {
            line.setV2(rule.get(2));
        }
        if (rule.size() > 3) {
            line.setV3(rule.get(3));
        }
        if (rule.size() > 4) {
            line.setV4(rule.get(4));
        }
        if (rule.size() > 5) {
            line.setV5(rule.get(5));
        }

        return line;
    }
}
