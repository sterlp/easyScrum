package org.easy.scrum.controller.config;

import java.io.Serializable;
import java.util.Random;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.easy.scrum.model.ConfigBE;
import org.easy.scrum.service.ConfigBF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ApplicationScoped
public class ConfigModel implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigModel.class);
    
    private static final String USER_ID_COOKIE_KEY = "EASY_SCRUM_USER_ID";
    
    @EJB
    private ConfigBF configBF;
    
    private ConfigBE userConfig;
    
    private static final Random R = new Random();

    public ConfigBE getUserConfig() {
        if (userConfig == null) {
            userConfig = reload();
        }
        return userConfig;
    }
    
    public ConfigBE reload() {
        userConfig = configBF.find(getUserId());
        return userConfig;
    }
    
    public void save() {
        configBF.edit(userConfig);
    }
    
    private Long getUserId() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Object cookieUser = externalContext.getRequestCookieMap().get(USER_ID_COOKIE_KEY);
        // no ID now? lets take the IP address
        if (cookieUser == null) {
            HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
            String remoteIp = request.getRemoteAddr();
            if (StringUtils.trimToNull(remoteIp) != null && remoteIp.length() >= 9) {
                remoteIp = remoteIp.replace(".", "").replace(":", "");
                LOG.debug("Using IP {} as user ID.", remoteIp);
                cookieUser = remoteIp;
            }
        } else {
            if (cookieUser instanceof javax.servlet.http.Cookie) {
                cookieUser = ((javax.servlet.http.Cookie)cookieUser).getValue();
            }
        }
        Long result;
        try {
            result = Long.valueOf(String.valueOf(cookieUser));
        } catch (Exception e) {
            result = R.nextLong();
            LOG.warn("Failed to parse userID {} to a number. Using generated {} as user id.", cookieUser, result, e);
        }
        // save the cookie
        externalContext.addResponseCookie(USER_ID_COOKIE_KEY, String.valueOf(result), null);
        return result;
    }

    public void setUserConfig(ConfigBE userConfig) {
        this.userConfig = userConfig;
    }
}
