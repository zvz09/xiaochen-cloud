package com.zvz09.xiaochen.k8s.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class ClusterDTO {

    @Schema(description = "集群名称")
    @NotBlank(message = "集群名称不能为空")
    private String clusterName;

    @Schema(description = "apiServer url")
    @NotBlank(message = "apiServer路径不能为空")
    private String apiServer;

    @Schema(description = "token")
    private String token;

    @Schema(description = "kubeconfig")
    private String kubeconfig;
}
