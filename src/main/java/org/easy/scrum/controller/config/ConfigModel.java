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
    
    private String getUserId() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Object cookieUser = externalContext.getRequestCookieMap().get(USER_ID_COOKIE_KEY);
        // no ID now? lets take the IP address
        if (cookieUser == null) {
            HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
            String remoteHost = request.getRemoteHost();
            if (StringUtils.trimToNull(remoteHost) != null && remoteHost.length() >= 2) {
                LOG.debug("Using Host ID as {} as user ID.", remoteHost);
                cookieUser = remoteHost;
            }
        } else {
            if (cookieUser instanceof javax.servlet.http.Cookie) {
                cookieUser = ((javax.servlet.http.Cookie)cookieUser).getValue();
            }
        }
        String result = cookieUser != null ? String.valueOf(cookieUser) : "Random_" + R.nextInt();
        externalContext.addResponseCookie(USER_ID_COOKIE_KEY, String.valueOf(result), null);
        return result;
    }

    public void setUserConfig(ConfigBE userConfig) {
        this.userConfig = userConfig;
    }
}
