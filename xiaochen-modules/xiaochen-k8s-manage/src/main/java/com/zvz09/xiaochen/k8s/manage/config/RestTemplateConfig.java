package com.zvz09.xiaochen.k8s.manage.config;

import com.zvz09.xiaochen.k8s.manage.utils.OkHttpUtil;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 连接池
     *
     * @return 连接池
     */
    public ConnectionPool pool() {
        return new ConnectionPool(20, 5, TimeUnit.MINUTES);
    }

    /**
     * okHttp3Client 客户端
     * {@code https://square.github.io/okhttp/}
     *
     * @return httpClient
     */
    @Bean
    public OkHttpClient okHttp3Client() throws NoSuchAlgorithmException, KeyManagementException {
        return new OkHttpClient().newBuilder()
                .connectionPool(pool())
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(5, TimeUnit.MINUTES)
                .sslSocketFactory(OkHttpUtil.getIgnoreInitedSslContext().getSocketFactory(), OkHttpUtil.IGNORE_SSL_TRUST_MANAGER_X509)
                .hostnameVerifier(OkHttpUtil.getIgnoreSslHostnameVerifier())
                // 禁止重定向
                .followRedirects(false)
                .build();
    }




    @Bean
    public RestTemplate restTemplate(OkHttpClient okHttp3Client){
        OkHttp3ClientHttpRequestFactory clientHttpRequestFactory =
                new OkHttp3ClientHttpRequestFactory(okHttp3Client);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return restTemplate;
    }
}
