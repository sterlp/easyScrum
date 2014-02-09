package org.easy.scrum.service.db;

import java.sql.SQLException;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.sql.DataSource;
import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

/**
 * http://www.liquibase.org/documentation/cdi.html
 */

public class LiquibaseProducer {
    @Resource(name = "jdbc/easyScrum")
    private DataSource myDataSource;

    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("/changelog.xml");
        return config;
    }

    public DataSource createDataSource() throws SQLException, NamingException {
        if (myDataSource == null) throw new IllegalStateException("Missing jdbc/easyScrum in container!");
        return myDataSource;
    }

    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }
}
