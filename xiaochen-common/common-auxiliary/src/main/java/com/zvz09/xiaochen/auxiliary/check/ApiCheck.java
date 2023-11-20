package com.zvz09.xiaochen.auxiliary.check;

import com.zvz09.xiaochen.auxiliary.springdoc.MyOpenApiResource;
import com.zvz09.xiaochen.system.api.RemoteSysApiService;
import com.zvz09.xiaochen.system.api.domain.entity.SysApi;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zvz09.xiaochen.common.core.constant.Constants.FEIGN_PATH_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiCheck implements ApplicationRunner {

    private final MyOpenApiResource myOpenApiResource;

    private final Environment env;

    private final List<SysApi> sysApiList = new ArrayList<>();

    private final RemoteSysApiService remoteSysApiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("**********************检查SysAPi表是否缺少接口配置**********************");

        getAllApi();

        List<SysApi> notInDb = getNotInDbApi();

        if (!notInDb.isEmpty()) {
            notInDb.forEach(sysApi -> {
                log.info("**********************数据库缺少：【{}_{}】接口配置:{},Method:{},稍后自动进行插入操作**********************"
                        , sysApi.getApiGroup(), sysApi.getDescription(), sysApi.getPath(), sysApi.getMethod());
            });
            log.error("**********************开始进行补充接口数据信息**********************");
            remoteSysApiService.saveBatch(notInDb);
            log.error("**********************补充接口数据信息完毕，请检查角色权限**********************");
        }
        log.info("**********************检查SysAPi表是否缺少接口配置完毕**********************");
    }

    private List<SysApi> getNotInDbApi() {
        List<SysApi> notInDb = new ArrayList<>();
        List<SysApi> dbSysApiList = remoteSysApiService.list(env.getProperty("spring.application.name"));
        if (dbSysApiList.size() != sysApiList.size()) {
            Set<String> dbSet = new HashSet<>();
            dbSysApiList.forEach(dbSysApi -> {
                dbSet.add(dbSysApi.getPath() + "_" + dbSysApi.getMethod());
            });
            Map<String, SysApi> map = new HashMap<>();

            sysApiList.forEach(sysApi -> {
                map.put(sysApi.getPath() + "_" + sysApi.getMethod(), sysApi);
            });
            map.keySet().forEach(key -> {
                if (!dbSet.contains(key)) {
                    notInDb.add(map.get(key));
                }
            });
        }
        return notInDb;
    }

    private void getAllApi() {
        OpenAPI openAPI = myOpenApiResource.getOpenApi();
        openAPI.getPaths().forEach((path, pathItem) -> {
            Operation operation = null;
            String method = "", apiGroup = "";
            if (pathItem.getGet() != null) {
                operation = pathItem.getGet();
                method = RequestMethod.GET.toString();
            } else if (pathItem.getHead() != null) {
                operation = pathItem.getHead();
                method = RequestMethod.HEAD.toString();
            } else if (pathItem.getPost() != null) {
                operation = pathItem.getPost();
                method = RequestMethod.POST.toString();
            } else if (pathItem.getPut() != null) {
                operation = pathItem.getPut();
                method = RequestMethod.PUT.toString();
            } else if (pathItem.getPatch() != null) {
                operation = pathItem.getPatch();
                method = RequestMethod.PATCH.toString();
            } else if (pathItem.getDelete() != null) {
                operation = pathItem.getDelete();
                method = RequestMethod.DELETE.toString();
            } else if (pathItem.getOptions() != null) {
                operation = pathItem.getOptions();
                method = RequestMethod.OPTIONS.toString();
            } else if (pathItem.getTrace() != null) {
                operation = pathItem.getTrace();
                method = RequestMethod.TRACE.toString();
            }

            if (!path.startsWith(FEIGN_PATH_PREFIX) && operation != null) {
                String description = operation.getSummary() == null ? path : operation.getSummary();
                if (operation.getTags() != null && !operation.getTags().isEmpty()) {
                    apiGroup = operation.getTags().get(0);
                }
                sysApiList.add(new SysApi(env.getProperty("spring.application.name"), path, description, apiGroup, method));
            }
        });
    }
}
