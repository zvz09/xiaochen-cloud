package com.zvz09.xiaochen.autocode.engine.enjoy;

import com.jfinal.template.source.ISource;
import lombok.RequiredArgsConstructor;

/**
 * @author zvz09
 */
@RequiredArgsConstructor
public class StringSource implements ISource {

    private final String content;
    private final String cacheKey;

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public String getCacheKey() {
        return this.cacheKey;
    }

    @Override
    public StringBuilder getContent() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.content);
        return builder;
    }

    @Override
    public String getEncoding() {
        return "UTF-8";
    }
}
