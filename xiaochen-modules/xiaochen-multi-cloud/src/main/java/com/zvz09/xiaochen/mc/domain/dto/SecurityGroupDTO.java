package com.zvz09.xiaochen.mc.domain.dto;

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

    private String securityGroupName;

    private String securityGroupId;

    private String vpcId;

    private String description;

    private String status;

    private String type;


    private List<PermissionDTO> permissions;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionDTO {
        private String cidrIp;
        private String ipProtocol;
        private String portRange;
        private String policy;
        private String direction;
        private String priority;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PermissionDTO that = (PermissionDTO) o;
            return Objects.equals(cidrIp, that.cidrIp) && Objects.equals(ipProtocol, that.ipProtocol) && Objects.equals(portRange, that.portRange)
                    &&  Objects.equals(policy, that.policy)
                    &&  Objects.equals(priority, that.priority);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cidrIp, ipProtocol, portRange,policy,priority);
        }
    }
}
