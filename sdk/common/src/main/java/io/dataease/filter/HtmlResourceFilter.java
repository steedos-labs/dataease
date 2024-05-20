package io.dataease.filter;

import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class HtmlResourceFilter implements Filter, Ordered {

    @Value("${dataease.http.cache:false}")
    private Boolean httpCache;

    @Override
    public int getOrder() {
        return 99;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if(httpCache == null || !httpCache){
            // 禁用缓存
            httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            httpResponse.setHeader("Cache", "no-cache");
            httpResponse.setHeader(HttpHeaders.PRAGMA, "no-cache");
            httpResponse.setHeader(HttpHeaders.EXPIRES, "0");
        }

        // 判断请求是否为HTML文件
        if (httpRequest.getRequestURI().endsWith(".html")) {
            // 使用自定义的响应包装类
            HtmlResponseWrapper responseWrapper = new HtmlResponseWrapper(httpResponse);
            filterChain.doFilter(servletRequest, responseWrapper);

            // 获取原始HTML内容
            String originalHtml = new String(responseWrapper.getContent(), httpResponse.getCharacterEncoding());

            // 重写HTML内容
            String rewrittenHtml = rewriteHtml(originalHtml);

            // 写回重写后的HTML内容
            httpResponse.getWriter().write(rewrittenHtml);
        } else {
            // 继续执行过滤器链
            filterChain.doFilter(servletRequest, httpResponse);
        }
    }

   private String rewriteHtml(String originalHtml) {
        // 获取所有环境变量
        Map<String, String> env = System.getenv();

        // 正则表达式匹配 process.env.xxx 格式的占位符
        Pattern pattern = Pattern.compile("process\\.env\\.([a-zA-Z_][a-zA-Z0-9_]*)");
        Matcher matcher = pattern.matcher(originalHtml);

        // 替换匹配的占位符为环境变量值
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String envVar = matcher.group(1);
            String envValue = env.getOrDefault(envVar, "");
            matcher.appendReplacement(buffer, envValue);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
