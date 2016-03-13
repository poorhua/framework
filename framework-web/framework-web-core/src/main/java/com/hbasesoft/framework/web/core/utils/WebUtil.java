/****************************************************************************************
 * Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 * transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 * or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.web.core.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hbasesoft.framework.cache.core.CacheConstant;
import com.hbasesoft.framework.cache.core.CacheException;
import com.hbasesoft.framework.cache.core.CacheHelper;
import com.hbasesoft.framework.common.utils.CommonUtil;
import com.hbasesoft.framework.common.utils.logger.Logger;
import com.hbasesoft.framework.web.core.WebConstant;
import com.hbasesoft.framework.web.core.bean.OperatorPojo;
import com.hbasesoft.framework.web.core.bean.UrlResource;

/**
 * <Description> <br>
 *
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年12月5日 <br>
 * @see com.hbasesoft.framework.web <br>
 */
public final class WebUtil {

    private static Logger logger = new Logger(WebUtil.class);

    /**
     * matcher
     */
    private static AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 默认构造函数
     */
    private WebUtil() {
    }

    /**
     * Description: <br>
     *
     * @param attrCode <br>
     * @param attrValue <br>
     * @author yang.zhipeng <br>
     * @taskId <br>
     */
    public static void setAttribute(String attrCode, Object attrValue) {
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        if (requestAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
            request.getSession().setAttribute(attrCode, attrValue);
        }
    }

    /**
     * Description: <br>
     *
     * @param attrCode <br>
     * @return <br>
     * @author yang.zhipeng <br>
     * @taskId <br>
     */
    public static Object getAttribute(String attrCode) {
        Object obj = null;
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        if (requestAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
            obj = request.getAttribute(attrCode);
            if (obj == null) {
                obj = request.getSession().getAttribute(attrCode);
            }
        }
        return obj;
    }

    /**
     * 删除属性值 Description: <br>
     *
     * @param code <br>
     * @author 王伟<br>
     * @taskId <br>
     */
    public static void removeAttribute(String code) {
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        if (requestAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
            request.removeAttribute(code);
            request.getSession().removeAttribute(code);
        }
    }

    /***
     * Description: <br>
     *
     * @param request <br>
     * @return String <br>
     * @author bai.wenlong<br>
     * @taskId <br>
     */
    public static String getRemoteIP(HttpServletRequest request) {
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }
        else {
            ip = request.getHeader("x-forwarded-for");
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * Description: <br>
     *
     * @return <br>
     * @author yang.zhipeng <br>
     * @taskId <br>
     */
    public static Integer getCurrentOperatorId() {
        OperatorPojo pojo = getCurrentOperator();
        return pojo == null ? null : pojo.getOperatorId();
    }

    /**
     * Description: <br>
     *
     * @return <br>
     * @author yang.zhipeng <br>
     * @taskId <br>
     */
    public static OperatorPojo getCurrentOperator() {
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        if (requestAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
            String ip = request.getRemoteAddr();
            HttpSession session = request.getSession();
            OperatorPojo operator = (OperatorPojo) session.getAttribute(WebConstant.SESSION_OPERATOR);
            if (operator != null) {
                operator.setLastIp(ip);
            }
            return operator;
        }
        return null;
    }

    /**
     * Description: <br>
     *
     * @param request <br>
     * @return <br>
     * @author yang.zhipeng <br>
     * @taskId <br>
     */
    public static UrlResource urlMatch(HttpServletRequest request) {

        try {
            Map<String, UrlResource> urlResourceMap = CacheHelper.getCache().getNode(UrlResource.class,
                CacheConstant.URL_RESOURCE);

            if (CommonUtil.isNotEmpty(urlResourceMap)) {
                String url = request.getRequestURI().substring(request.getContextPath().length());
                String method = request.getMethod();
                for (UrlResource resource : urlResourceMap.values()) {
                    if (matcher.match(resource.getUrl(), url) && resource.match(method)) {
                        return resource;
                    }
                }
            }
        }
        catch (CacheException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
