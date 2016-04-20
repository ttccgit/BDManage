package com.ly.dsfbase.provider.filter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractBaseFilter implements Filter {

    public final void init(FilterConfig filterConfig) throws ServletException {
        String excludedSetting = filterConfig.getInitParameter(EXCLUDED);
        if (StringUtils.hasText(excludedSetting)) {
            String[] patterns = excludedSetting.split(",");
            for (String pattern : patterns) {
                pattern = pattern.trim();
                if (StringUtils.hasText(pattern)) {
                    excludedPatterns.add(Pattern.compile(pattern));
                }
            }
        }

        String supportSetting = filterConfig.getInitParameter(SUPPORT);
        if (StringUtils.hasText(supportSetting)) {
            String[] methods = supportSetting.split(",");
            for (String method : methods) {
                method = method.trim();
                if ("GET".equals(method) || "POST".equals(method)) {
                    supportMethods.add(method);
                }
            }
        }

        ServletContext context = filterConfig.getServletContext();
        System.out.println("context is "+context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        additionalInit(filterConfig);
    }

    public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // If the request URI is excluded, continue to the next filter
        if (isExcluded(request.getRequestURI())) {
            chain.doFilter(req, res);
            return;
        }

        if (!isMethodSupported(request.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        try {
            processFilter(request, response, chain);
        } catch (Exception e) {
            request.setAttribute("ERROR_DETAILS", e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public final void destroy() {
    }

    protected boolean isGET(final HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Subclasses can override this method to provide additional initialization
     */
    protected void additionalInit(FilterConfig filterConfig) {
    }

    protected abstract void processFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException;

    /**
     * Get Spring Bean by class
     */
    @SuppressWarnings("unchecked")
    protected final <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * Get Spring Bean by name
     * @param beanName bean name
     * @return spring bean
     */
    protected final Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    protected Log log = LogFactory.getLog(getClass());

    /**
     * Check if the request URI is excluded and doesn't need processing
     *
     * @return true when excluded, false when included
     */
    private boolean isExcluded(final String requestURI) {
        boolean excluded = false;
        for (Pattern pattern : excludedPatterns) {
            if (pattern.matcher(requestURI).find()) {
                excluded = true;
                break;
            }
        }
        return excluded;
    }

    private boolean isMethodSupported(final String method) {
        if (supportMethods.isEmpty()) {
            return true;
        }

        boolean support = false;
        for (String supportMethod : supportMethods) {
            if (supportMethod.equalsIgnoreCase(method)) {
                support = true;
                break;
            }
        }
        return support;
    }

    private final static String EXCLUDED = "excluded";
    private final static String SUPPORT = "support";
    private final Set<Pattern> excludedPatterns = new HashSet<Pattern>();
    private final Set<String> supportMethods = new HashSet<String>();
    private ApplicationContext applicationContext;

}
