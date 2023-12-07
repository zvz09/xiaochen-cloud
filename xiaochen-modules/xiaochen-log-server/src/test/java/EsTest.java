import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Locale;

public class EsTest {
    public static void main(String[] args) throws IOException {
        // URL and API key
        String serverUrl = "https://192.168.191.1:9200";
        String apiKey = "cldYMFBJd0JTOUpnd0FhOTJmV2o6Wi0yaFM1Wk5UUnFlZDliLXlNMnJxQQ==";

// Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // 转为小写
        String iName = "xlog*".toLowerCase(Locale.ROOT);
        // 获取【索引客户端对象】
        ElasticsearchIndicesClient indexClient = esClient.indices();

        // 查询结果；得到【查询索引响应对象】
        GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index(iName).build();

        GetIndexResponse getIndexResponse = indexClient.get(getIndexRequest);
        getIndexResponse.result().forEach((k,v)->{
            System.out.println(k+"----"+v);
        });
    }
}
