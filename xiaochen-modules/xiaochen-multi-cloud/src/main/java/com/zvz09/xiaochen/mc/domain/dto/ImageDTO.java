package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
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
    @FieldMapping(aliyun = @BaseFieldMapping(value = "architecture"),
            tencentcloud = @BaseFieldMapping(value = "Architecture"),
            volcengine = @BaseFieldMapping(value = "architecture")
    )
    private String architecture;
    /**
     * 镜像的启动模式。UEFI
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "bootMode"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "bootMode")
    )
    private String bootMode;
    /**
     * 镜像描述信息。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "description"),
            tencentcloud = @BaseFieldMapping(value = "ImageDescription"),
            volcengine = @BaseFieldMapping(value = "description")
    )
    private String description;
    /**
     * 镜像ID。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "imageId"),
            tencentcloud = @BaseFieldMapping(value = "ImageId"),
            volcengine = @BaseFieldMapping(value = "imageId")
    )
    private String imageId;
    /**
     * 镜像名称。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "imageName"),
            tencentcloud = @BaseFieldMapping(value = "ImageName"),
            volcengine = @BaseFieldMapping(value = "imageName")
    )
    private String imageName;
    /**
     * 镜像是否支持Cloud-init。* true：支持 * false：不支持
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "isSupportCloudinit"),
            tencentcloud = @BaseFieldMapping(value = "IsSupportCloudinit"),
            volcengine = @BaseFieldMapping(value = "isSupportCloudInit")
    )
    private Boolean isSupportCloudInit;

    /**
     * 镜像操作系统的名称。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "OSName"),
            tencentcloud = @BaseFieldMapping(value = "OsName"),
            volcengine = @BaseFieldMapping(value = "osName")
    )
    private String osName;

    /**
     * 操作系统类型：* Linux * Windows
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "OSType"),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "osType")
    )
    private String osType;

    /**
     * 镜像操作系统的发行版本，枚举值：* CentOS * Debian * veLinux * Windows Server * Fedora * OpenSUSE * Ubuntu
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "platform"),
            tencentcloud = @BaseFieldMapping(value = "Platform"),
            volcengine = @BaseFieldMapping(value = "platform")
    )
    private String platform;

    /**
     * 镜像的发行版本。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = ""),
            tencentcloud = @BaseFieldMapping(value = ""),
            volcengine = @BaseFieldMapping(value = "platformVersion")
    )
    private String platformVersion;

    /**
     * 镜像大小，单位为GiB。
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "size"),
            tencentcloud = @BaseFieldMapping(value = "ImageSize"),
            volcengine = @BaseFieldMapping(value = "size")
    )
    private Integer size;

    /**
     * 镜像的状态：* available：可用
     */
    @FieldMapping(aliyun = @BaseFieldMapping(value = "status"),
            tencentcloud = @BaseFieldMapping(value = "ImageState"),
            volcengine = @BaseFieldMapping(value = "status")
    )
    private String status;

}
