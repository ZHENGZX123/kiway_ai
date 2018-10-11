package cn.kiway.kiway_ai.config;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加CORS 跨域支持 Created by xyz327 on 17-7-8.
 */
@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	HttpServletResponse httpResponse = (HttpServletResponse) response;
	HttpServletRequest r = (HttpServletRequest) request;
	String header = r.getHeader("Origin");
	if (StringUtils.isEmpty(header)) {
	    header = "*";
	}
	httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE,PUT,HEAD");
	httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, header);
	httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
		"x-auth-token, Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, x-requested-with");
	httpResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
	chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
