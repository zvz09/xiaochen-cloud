/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.wrapper
 * @className com.zvz09.xiaochen.common.web.wrapper.BodyCachingHttpServletResponseWrapper
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * BodyCachingHttpServletResponseWrapper
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 17:49
 */
public class BodyCachingHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final HttpServletResponse response;

    public BodyCachingHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    public byte[] getBody() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(this.byteArrayOutputStream, this.response.getCharacterEncoding()));
    }


    private static class ServletOutputStreamWrapper extends ServletOutputStream {

        private final ByteArrayOutputStream outputStream;
        private final HttpServletResponse response;

        public ServletOutputStreamWrapper(ByteArrayOutputStream outputStream, HttpServletResponse response) {
            this.outputStream = outputStream;
            this.response = response;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            // 只用来缓存，不需要设置读监听器
        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (!this.response.isCommitted()) {
                byte[] body = this.outputStream.toByteArray();
                ServletOutputStream servletOutputStream = this.response.getOutputStream();
                servletOutputStream.write(body);
                servletOutputStream.flush();
            }
        }
    }

}
 