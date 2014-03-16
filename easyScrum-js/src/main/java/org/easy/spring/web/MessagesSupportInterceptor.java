/*
 * Copyright 2014 Paul.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.easy.spring.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.easy.spring.web.MessagesSupportInterceptor.WebMessage.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Support Messages ala JSF in Spring.
 * 
 * Not working in Spring bause of some desing errors in the framework
 * http://stackoverflow.com/questions/14705787/handlerinterceptoradapter-json-encoding-and-post-processing-of-response
 * 
 * 
 */
public class MessagesSupportInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesSupportInterceptor.class);

    public static class WebContext {
        static ThreadLocal<WebContext> instance = new ThreadLocal<>();
        @Getter
        private List<WebMessage> messages = new ArrayList<>();
        
        public static WebContext getInstance() {
            return instance.get();
        }
        
        public WebContext addMessage(String message) {
            this.messages.add(new WebMessage(Status.OKAY, message, null, true));
            return this;
        }
    }
    
    @Data
    @AllArgsConstructor
    public static class WebMessage {
        public static enum Status {
            ERROR,
            WARNING,
            INFO,
            OKAY
            
        }
        private Status status = Status.OKAY;
        private String summary;
        private String detail;
        private boolean rendered;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.instance.set(new WebContext());
        LOG.warn("preHandle...");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // always null in spring REST with @ResponseBody -- to tiered to patch Spring
        if (modelAndView != null) {
            WebContext context = WebContext.getInstance();
            WebContext.instance.remove(); // clean
            modelAndView.addObject("messages", context.getMessages());
        }
    }
}
