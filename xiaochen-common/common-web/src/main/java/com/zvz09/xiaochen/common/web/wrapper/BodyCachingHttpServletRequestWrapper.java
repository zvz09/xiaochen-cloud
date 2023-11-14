/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.wrapper
 * @className com.zvz09.xiaochen.common.web.wrapper.HttpServletRequestWrapper
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * HttpServletRequestWrapper
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 17:48
 */
public class BodyCachingHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private ServletInputStreamWrapper inputStreamWrapper;

    private byte[] body;

    public BodyCachingHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = StreamUtils.copyToByteArray(request.getInputStream());
        this.inputStreamWrapper = new ServletInputStreamWrapper(new ByteArrayInputStream(this.body));
        resetInputStream();
    }

    private void resetInputStream() {
        this.inputStreamWrapper.setInputStream(new ByteArrayInputStream(this.body != null ? this.body : new byte[0]));
    }

    public byte[] getBody() {
        return body;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.inputStreamWrapper;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.inputStreamWrapper));
    }


    private static class ServletInputStreamWrapper extends ServletInputStream {

        private InputStream inputStream;

        ServletInputStreamWrapper(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // 只用来缓存，不需要设置读监听器
        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }

}
 