package com.zvz09.xiaochen.log.server.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zvz09
 */
@Data
public class LogIndex implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;
    private String id;
    private String dateTime;
    private String applicationName;
    private String level;
    private String traceId;
    private String host;
    private String className;

    private String thread;

    private String message;

    private String error;

    @JsonAlias("@timestamp")
    private String timestamp;
}
