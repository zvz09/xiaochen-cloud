package com.zvz09.xiaochen.k8s.manage.filter.mapper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpResponseMapper {
    public void map(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        setStatus(responseEntity, response);
        setHeaders(responseEntity, response);
        setBody(responseEntity, response);
    }

    private void setStatus(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) {
        response.setStatus(responseEntity.getStatusCode().value());
    }

    private void setHeaders(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) {
        responseEntity.getHeaders().forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));
    }

    /**
     * 把结果写回客户端
     *
     * @param responseEntity
     * @param response
     * @throws IOException
     */
    private void setBody(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        if (responseEntity.getBody() != null) {
            response.getOutputStream().write(responseEntity.getBody());
        }
    }
}
