package de.geoinfoffm.registry.persistence.jpa;

import org.hibernate.event.spi.PostLoadEvent;

/**
 * Hibernate event listener that listens for post-load events. 
 *
 * @see org.hibernate.event.spi.PostLoadEventListener
 * @author Florian Esser
 *
 */
@SuppressWarnings("serial")
public class PostLoadEventListener implements org.hibernate.event.spi.PostLoadEventListener
{

	/**
	 * Called immediately after the setting of the property values in the
	 * loaded object is completed and resets the <code>loadingFromPersistedStorage</code> flag 
	 * in {@link PersistableEntity} objects.
	 */
	@Override
	public void onPostLoad(PostLoadEvent event) {
//		if (event.getEntity() instanceof AbstractEntity) {
//			AbstractEntity entityToLoad = ((AbstractEntity)event.getEntity());
//			entityToLoad.loadingFromPersistedStorage = false;
//		}
	}

}
