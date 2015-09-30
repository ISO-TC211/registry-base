package de.geoinfoffm.registry.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.hibernate.metamodel.relational.Datatype;

import de.geoinfoffm.registry.core.Entity;

public class EntityUnitOfWork
{
	private final LinkedList<UUID> entityUuids = new LinkedList<>();
	private final Map<UUID, Entity> entities = new HashMap<>();
	private final Map<UUID, Runnable> runnables = new HashMap<>();
	private final EntityManager entityManager;
	
	public EntityUnitOfWork(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void registerNew(Entity entity) {
		entityUuids.add(entity.getUuid());
		entities.put(entity.getUuid(), entity);
	}
	
	public void registerRunnable(Entity entity, Runnable runnable) {
		runnables.put(entity.getUuid(), runnable);
	}
	
	public boolean isNew(UUID uuid) {
		return entityUuids.contains(uuid);
	}
	
	public Entity resolveEntity(UUID uuid, Class<? extends Entity> entityType) {
		if (isNew(uuid)) {
			return entities.get(uuid);
		}
		else {
			return entityManager.find(entityType, uuid);
		}
	}
	
	public void commit() {
		for (UUID uuid : entityUuids) {
			if (runnables.containsKey(uuid)) {
				Runnable runnable = runnables.get(uuid);
				runnable.run();
			}
//			entityManager.merge(entities.get(uuid));
		}
		
		for (UUID uuid : entityUuids) {
			entityManager.persist(entities.get(uuid));
		}
	}
}
