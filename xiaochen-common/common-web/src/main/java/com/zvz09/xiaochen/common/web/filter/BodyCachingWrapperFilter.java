/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.filter
 * @className com.zvz09.xiaochen.common.web.filter.BodyCachingWrapperFilter
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.filter;

import com.zvz09.xiaochen.common.web.wrapper.BodyCachingHttpServletRequestWrapper;
import com.zvz09.xiaochen.common.web.wrapper.BodyCachingHttpServletResponseWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * BodyCachingWrapperFilter
 *
 * @author zvz09
 * @version 1.0
 * @description
 * @date 2023/9/13 17:50
 */
@Component
@WebFilter(filterName = "BodyCachingWrapperFilter", urlPatterns = "/**")
public class BodyCachingWrapperFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(request.getContentType()!=null && !request.getContentType().contains("boundary")){
            BodyCachingHttpServletRequestWrapper requestWrapper = new BodyCachingHttpServletRequestWrapper((HttpServletRequest) request);
            BodyCachingHttpServletResponseWrapper responseWrapper = new BodyCachingHttpServletResponseWrapper((HttpServletResponse) response);
            // 这里用wrapper类代替，以达到可重复读的目的
            chain.doFilter(requestWrapper, responseWrapper);
        }else {
            chain.doFilter(request, response);
        }

    }

}
 