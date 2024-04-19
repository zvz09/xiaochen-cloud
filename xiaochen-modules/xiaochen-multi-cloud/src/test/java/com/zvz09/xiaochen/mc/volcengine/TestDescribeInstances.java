package com.zvz09.xiaochen.mc.volcengine;

import com.volcengine.ApiClient;
import com.volcengine.ApiException;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.DescribeInstancesRequest;
import com.volcengine.sign.Credentials;

public class TestDescribeInstances {
    public static void main(String[] args) throws Exception {
        // 注意示例代码安全，代码泄漏会导致AK/SK泄漏，有极大的安全风险。
        String ak = "";
        String sk = "==";
        String region = "cn-beijing";

        ApiClient apiClient = new ApiClient()
                .setCredentials(Credentials.getCredentials(ak, sk))
                .setRegion(region);

        EcsApi api = new EcsApi(apiClient);

        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();

        try {
            // 复制代码运行示例，请自行打印API返回值。
            api.describeInstances(describeInstancesRequest);
        } catch (ApiException e) {
            // 复制代码运行示例，请自行打印API错误信息。
            // System.out.println(e.getResponseBody());
        }
    }
}
