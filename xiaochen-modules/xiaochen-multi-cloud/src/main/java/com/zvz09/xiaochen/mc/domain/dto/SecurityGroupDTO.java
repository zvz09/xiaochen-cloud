package com.zvz09.xiaochen.mc.domain.dto;

import com.zvz09.xiaochen.mc.annotation.BaseFieldMapping;
import com.zvz09.xiaochen.mc.annotation.FieldMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityGroupDTO {

    @FieldMapping(aliyun = @BaseFieldMapping(value = "securityGroupName"),
            tencentcloud = @BaseFieldMapping(value = "SecurityGroupName"),
            volcengine = @BaseFieldMapping(value = "securityGroupName"))
    private String securityGroupName;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "securityGroupId"),
            tencentcloud = @BaseFieldMapping(value = "SecurityGroupId"),
            volcengine = @BaseFieldMapping(value = "securityGroupId"))
    private String securityGroupId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "vpcId"),
            tencentcloud = @BaseFieldMapping(),
            volcengine = @BaseFieldMapping(value = "vpcId"))
    private String vpcId;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "description"),
            tencentcloud = @BaseFieldMapping(value = "SecurityGroupDesc"),
            volcengine = @BaseFieldMapping(value = "description"))
    private String description;

    @FieldMapping(aliyun = @BaseFieldMapping(),
            tencentcloud = @BaseFieldMapping(),
            volcengine = @BaseFieldMapping(value = "status"))
    private String status;

    @FieldMapping(aliyun = @BaseFieldMapping(value = "securityGroupType"),
            tencentcloud = @BaseFieldMapping(),
            volcengine = @BaseFieldMapping(value = "type"))
    private String type;


    private List<PermissionDTO> permissions;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionDTO {
        @FieldMapping(aliyun = @BaseFieldMapping(value = "sourceCidrIp"),
                tencentcloud = @BaseFieldMapping(value = "CidrBlock"),
                volcengine = @BaseFieldMapping(value = "cidrIp"))
        private String cidrIp;

        @FieldMapping(aliyun = @BaseFieldMapping(value = "ipProtocol"),
                tencentcloud = @BaseFieldMapping(value = "Protocol"),
                volcengine = @BaseFieldMapping(value = "protocol"))
        private String ipProtocol;

        /**
         * 访问类型。ingress（默认）：入方向。 egress：出方向。
         */
        @FieldMapping(aliyun = @BaseFieldMapping(value = "direction"),
                tencentcloud = @BaseFieldMapping(),
                volcengine = @BaseFieldMapping(value = "direction"))
        private String direction;

        @FieldMapping(aliyun = @BaseFieldMapping(value = "portRange"),
                tencentcloud = @BaseFieldMapping(value = "Port"),
                volcengine = @BaseFieldMapping())
        private String portRange;
        /**
         * 访问策略。accept（默认）：接受访问。 drop：拒绝访问。
         */
        @FieldMapping(aliyun = @BaseFieldMapping(value = "policy"),
                tencentcloud = @BaseFieldMapping(value = "Action"),
                volcengine = @BaseFieldMapping(value = "policy"))
        private String policy;

        @FieldMapping(aliyun = @BaseFieldMapping(value = "description"),
                tencentcloud = @BaseFieldMapping(value = "PolicyDescription"),
                volcengine = @BaseFieldMapping(value = "description"))
        private String description;
        /**
         * 安全组规则优先级，数字越小，代表优先级越高。
         */
        @FieldMapping(aliyun = @BaseFieldMapping(value = "priority"),
                tencentcloud = @BaseFieldMapping(value = "PolicyIndex"),
                volcengine = @BaseFieldMapping(value = "priority"))
        private String priority;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PermissionDTO that = (PermissionDTO) o;
            return Objects.equals(cidrIp, that.cidrIp) && Objects.equals(ipProtocol, that.ipProtocol) && Objects.equals(portRange, that.portRange)
                    &&  Objects.equals(policy, that.policy)
                    &&  Objects.equals(priority, that.priority)
                    &&  Objects.equals(direction, that.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cidrIp, ipProtocol, portRange,policy,priority,direction);
        }
    }
}
