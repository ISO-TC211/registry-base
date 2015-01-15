package de.geoinfoffm.registry.persistence.jpa;

import org.hibernate.event.spi.PreLoadEvent;

/**
 * Hibernate event listener that listens for pre-load events. 
 *
 * @see org.hibernate.event.spi.PreLoadEventListener
 * @author Florian Esser
 *
 */
@SuppressWarnings("serial")
public class PreLoadEventListener implements org.hibernate.event.spi.PreLoadEventListener
{

	/**
	 * Called immediately before the setting of the property values in the
	 * object starts and sets the <code>loadingFromPersistedStorage</code> flag 
	 * in {@link PersistableEntity} objects.
	 */
	@Override
	public void onPreLoad(PreLoadEvent event) {
//		if (event.getEntity() instanceof AbstractEntity) {
//			AbstractEntity entityToLoad = ((AbstractEntity)event.getEntity());
//			entityToLoad.loadingFromPersistedStorage = true;
//		}
//		else if (event.getEntity() instanceof HashMap<?, ?>) {
//			HashMap<?, ?> map = (HashMap<?, ?>)event.getEntity();
//			new Object();
//		}
	}
}
