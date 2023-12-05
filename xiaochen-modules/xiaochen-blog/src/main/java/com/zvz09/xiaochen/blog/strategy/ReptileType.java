package com.zvz09.xiaochen.blog.strategy;

import com.zvz09.xiaochen.blog.strategy.impl.CSDNParser;
import com.zvz09.xiaochen.blog.strategy.impl.CloudTencentParser;
import com.zvz09.xiaochen.blog.strategy.impl.CnblogParser;
import com.zvz09.xiaochen.blog.strategy.impl.JueJinParser;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import lombok.Getter;

/**
 * @author zvz09
 */

@Getter
public enum ReptileType {

    CLOUD_TENCENT("https://cloud.tencent.com",new CloudTencentParser()),
    CNBLOG("https://www.cnblogs.com",new CnblogParser()),
    JUEJIN("https://juejin.cn",new JueJinParser()),
    CSDN("https://blog.csdn.net",new CSDNParser())
    ;

    private final String baseUrl;
    private final ReptileDataParserStrategy strategy;

    ReptileType(String baseUrl, ReptileDataParserStrategy strategy) {
        this.baseUrl = baseUrl;
        this.strategy = strategy;
    }

    //这里有一个 根据code 获得对于Strategy方法
    public static ReptileType getByUrl(String url){
        for (ReptileType value : ReptileType.values()) {
            if (url.startsWith(value.baseUrl)){
                return value;
            }
        }
        throw new BusinessException(String.format("该网站%s，暂不支持",url));
    }

}
