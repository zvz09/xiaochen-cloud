package com.zvz09.xiaochen.mcmp.domain.dto;

import com.zvz09.xiaochen.common.web.dto.BaseDto;
import com.zvz09.xiaochen.mcmp.domain.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 主账号表
 * @TableName mcmp_primary_account
 */
@Getter
@Setter
public class AccountDTO extends BaseDto<Account> {


    @Schema(description ="名称")
    @NotBlank(message = "名称不能为空")
    private String name;


    @Schema(description ="云厂商code")
    @NotBlank(message = "云厂商code不能为空")
    private String provider;


    @Schema(description ="账号ID")
    @NotBlank(message = "账号ID不能为空")
    private String account;


    @Schema(description ="AK")
    @NotBlank(message = "AK不能为空")
    private String accessKey;


    @Schema(description ="SK")
    @NotBlank(message = "SK不能为空")
    private String secretKey;


    @Schema(description ="描述")
    private String description;

    @Override
    public Account convertedToPo() {
        return Account.builder()
                .name(this.name)
                .provider(this.provider)
                .account(this.account)
                .accessKey(this.accessKey)
                .secretKey(this.secretKey)
                .description(this.description)
                .build();
    }
}
