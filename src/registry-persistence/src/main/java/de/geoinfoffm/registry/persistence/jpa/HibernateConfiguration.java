package de.geoinfoffm.registry.persistence.jpa;

import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public interface HibernateConfiguration
{

	public abstract Properties additionalParameters();

	public abstract DriverManagerDataSource dataSource();

}