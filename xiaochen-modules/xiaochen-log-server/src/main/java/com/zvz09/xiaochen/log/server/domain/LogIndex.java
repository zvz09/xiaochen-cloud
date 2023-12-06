package com.zvz09.xiaochen.log.server.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zvz09
 */
@Data
@IndexName("xlog*")
public class LogIndex implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;
    @IndexId
    private String id;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String dateTime;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String applicationName;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String level;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String traceId;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String host;
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String className;

    private String thread;

    private String message;

    private String error;

    @IndexField( value = "@timestamp",fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonAlias("@timestamp")
    private String timestamp;
}
