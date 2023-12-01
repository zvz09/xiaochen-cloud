package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.strategy.impl.CSDNParser;
import com.zvz09.xiaochen.blog.strategy.impl.JueJinParser;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import lombok.Getter;

/**
 * @author lizili-YF0033
 */

@Getter
public enum ReptileType {

    JUEJIN("juejin",new JueJinParser()),
    CSDN("csdn",new CSDNParser())
    ;

    private final String name;
    private final ReptileDataParserStrategy strategy;

    ReptileType(String name, ReptileDataParserStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    //这里有一个 根据code 获得对于Strategy方法
    public static ReptileType getByName(String name){
        for (ReptileType value : ReptileType.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        throw new BusinessException(String.format("该类型%s，暂不支持",name));
    }





}
