package de.geoinfoffm.registry.persistence;

import java.sql.Connection;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import de.bespire.LoggerFactory;

public class FlywayDatabaseSchemaManagementService implements DatabaseSchemaMangementService 
{
	private static final Logger logger = LoggerFactory.make();
	
	private Flyway flyway;
	
	private FlywayDatabaseSchemaManagementService() { }
	
	public FlywayDatabaseSchemaManagementService(DataSource dataSource) {
		Assert.notNull(dataSource);
		initFlyway(dataSource);
	}

	private void initFlyway(DataSource dataSource) {
		this.flyway = new Flyway();
		flyway.setDataSource(dataSource); 
		flyway.setLocations("classpath:de/geoinfoffm/registry/persistence/migration");
		
//		flyway.setLocations("classpath:de/geoinfoffm/registry/persistence/migration_test");
//		flyway.setBaselineOnMigrate(true);
		
		initCallbacks();
	}
	
	@Override
	public void analyze() {
		MigrationInfoService info = flyway.info();
		if (info == null) {
			logger.info("{{Flyway}} No migration information available. Empty database?");
		}
		else { 
			if (info.current() == null) {
				logger.info("{{Flyway}} No baseline found.");
			}
			else {
				logger.info("{{Flyway}} Database schema version is {} ({})", info.current().getVersion().getVersion(), info.current().getDescription());
			}
			for (MigrationInfo pending : info.pending()) {
				logger.info("{{Flyway}} Update to version {} ({}) is pending", pending.getVersion().getVersion(), pending.getDescription());
			}
		}		
	}

	@Override
	public void migrate() {
		flyway.migrate(); 
	}
	
	@Override
	public void repair() {
		flyway.repair();
	}

	private void initCallbacks() {
		flyway.setCallbacks(new FlywayCallback() {
			
			@Override
			public void beforeValidate(Connection connection) {
			}
			
			@Override
			public void beforeRepair(Connection connection) {
			}
			
			@Override
			public void beforeMigrate(Connection connection) {
			}
			
			@Override
			public void beforeInit(Connection connection) {
			}
			
			@Override
			public void beforeInfo(Connection connection) {
			}
			
			@Override
			public void beforeEachMigrate(Connection connection, MigrationInfo info) {
				logger.info("{{Flyway}} Migrating database to version " + info.getVersion().getVersion() + "...");
			}
			
			@Override
			public void beforeClean(Connection connection) {
			}
			
			@Override
			public void beforeBaseline(Connection connection) {
			}
			
			@Override
			public void afterValidate(Connection connection) {
			}
			
			@Override
			public void afterRepair(Connection connection) {
			}
			
			@Override
			public void afterMigrate(Connection connection) {
			}
			
			@Override
			public void afterInit(Connection connection) {
			}
			
			@Override
			public void afterInfo(Connection connection) {
			}
			
			@Override
			public void afterEachMigrate(Connection connection, MigrationInfo info) { 
				logger.info("{{Flyway}} Migration to version " + info.getVersion().getVersion() + " finished.");
			}
			
			@Override
			public void afterClean(Connection connection) {
			}
			
			@Override
			public void afterBaseline(Connection connection) {
				logger.info("{{Flyway}} Created baseline.");
			}
		});
	}
}
