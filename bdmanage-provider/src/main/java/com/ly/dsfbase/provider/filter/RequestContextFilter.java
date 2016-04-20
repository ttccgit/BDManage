package com.ly.dsfbase.provider.filter;

import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RequestContextFilter extends AbstractBaseFilter {


    public void additionalInit(FilterConfig filterConfig) {
     //   secretHandler = getBean(SecretHandler.class);
    }

    @Override
    public void processFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // handle across domain
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,token");
            response.addHeader("Access-Control-Max-Age", "1800");
        }

      //  RequestContext.setAuthenticateSysUser(secretHandler.getAuthenticateSysUser(request, response));

        chain.doFilter(request, response);

      //  RequestContext.cleanup();
    }

  //  private SecretHandler secretHandler;
}
