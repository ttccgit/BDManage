/**
 * Creation Date:2015年12月24日-下午12:37:52
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.provider.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 用于jsonp<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年12月24日-下午12:37:52
 * @since 2015年12月24日-下午12:37:52
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
} 

