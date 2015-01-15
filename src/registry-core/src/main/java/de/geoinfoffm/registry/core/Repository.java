package de.geoinfoffm.registry.core;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface for generic CRUD operations on a
 * repository for a specific type.
 * 
 * @author Florian Esser
 *
 * @param <T> Type held in repository
 */
@NoRepositoryBean
public interface Repository<T extends Entity> extends JpaRepository<T, UUID> 
{ 
}
