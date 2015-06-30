package de.geoinfoffm.registry.persistence;

public interface DatabaseSchemaMangementService 
{
	void analyze();
	void migrate();
}
