/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Hibernate;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.util.config.ConfigurationException;
import org.hibernate.service.spi.InjectService;
import org.hibernate.service.spi.Startable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;


public class TenantConnectionProvider extends AbstractMultiTenantConnectionProvider implements Startable {

    private final DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
    private ConfigurationService configurationService;

    @Override
    public void start() {
        DataSource dataSource = configurationService.getSetting(AvailableSettings.DATASOURCE, DataSource.class, null);
        if (dataSource == null) {
            throw new ConfigurationException("No Datasource configured.");
        }
        connectionProvider.configure(Collections.singletonMap(Environment.DATASOURCE, dataSource));
        //initDatasource(dataSource);
    }
    /*
    private void initDatasource(DataSource dataSource) {
        //log.info("Initializing database schema");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("RUNSCRIPT FROM 'classpath:sql/create.sql'");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }*/

    @InjectService
    public void setConfigurationProvider(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProvider;
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProvider;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        //log.debugf("Requesting connection for tenant %s", tenantIdentifier);
        final Connection connection = getAnyConnection();
        connection.createStatement().execute("USE " + tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        final String schema = configurationService.getSetting(
                AvailableSettings.DEFAULT_SCHEMA, String.class, "db_0");
        connection.createStatement().execute("USE " + schema);
        selectConnectionProvider(tenantIdentifier).closeConnection(connection);
    }
}
