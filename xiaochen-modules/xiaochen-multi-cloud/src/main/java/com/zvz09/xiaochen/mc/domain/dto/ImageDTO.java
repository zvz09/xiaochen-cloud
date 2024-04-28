package com.zvz09.xiaochen.mc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    /**
     * 地域
     */
    private String region;

    /**
     * 镜像的架构类型：* amd64：x86计算 * arm64：ARM计算
     */
    private String architecture;
    /**
     * 镜像的启动模式。UEFI
     */
    private String bootMode;
    /**
     * 镜像描述信息。
     */
    private String description;
    /**
     * 镜像ID。
     */
    private String imageId;
    /**
     * 镜像名称。
     */
    private String imageName;
    /**
     * 镜像是否支持Cloud-init。* true：支持 * false：不支持
     */
    private Boolean isSupportCloudInit;

    /**
     * 镜像操作系统的名称。
     */
    private String osName;

    /**
     * 操作系统类型：* Linux * Windows
     */
    private String osType;

    /**
     * 镜像操作系统的发行版本，枚举值：* CentOS * Debian * veLinux * Windows Server * Fedora * OpenSUSE * Ubuntu
     */
    private String platform;

    /**
     * 镜像的发行版本。
     */
    private String platformVersion;

    /**
     * 镜像大小，单位为GiB。
     */
    private Integer size;

    /**
     * 镜像的状态：* available：可用
     */
    private String status;

}
