package org.easy.scrum.controller.config;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.easy.scrum.model.BurnDownType;
import org.easy.scrum.model.ConfigBE;

@Named
@ApplicationScoped
public class ConfigController {
    
    @Inject
    private ConfigModel configModel;
    
    public ConfigBE getUserConfig() {
        return configModel.getUserConfig();
    }

    public void setUserConfig(ConfigBE userConfig) {
        configModel.setUserConfig(userConfig);
    }

    public void save() {
        configModel.save();
    }
    public void reload() {
        configModel.reload();
    }
    
    public List<BurnDownType> getBurnDownTypes() {
        return Arrays.asList(BurnDownType.values());
    }
    
    public String localizeEnum(Enum<?> enumValue) {
        return null;
    }
}
