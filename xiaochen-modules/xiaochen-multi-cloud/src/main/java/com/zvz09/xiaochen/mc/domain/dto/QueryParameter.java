package com.zvz09.xiaochen.mc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameter {

    private Integer pageNumber;

    private Integer pageSize;

    private String nextToken;

    @Builder.Default
    private boolean haveNext = false;

    public Integer getOffset(){
        if(pageNumber == null){
            return 0;
        }
        return pageNumber <= 1 ? 0 : (pageNumber - 1) * pageSize;
    }

}
