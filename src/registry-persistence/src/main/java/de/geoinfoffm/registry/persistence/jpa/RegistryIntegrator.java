package de.geoinfoffm.registry.persistence.jpa;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class RegistryIntegrator implements Integrator
{

	@SuppressWarnings("unchecked")
	@Override
	public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {

        // As you might expect, an EventListenerRegistry is the place with which event listeners are registered  It is a service
        // so we look it up using the service registry
        final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );

        // If you wish to have custom determination and handling of "duplicate" listeners, you would have to add an implementation
        // of the org.hibernate.event.service.spi.DuplicationStrategy contract like this
//        eventListenerRegistry.addDuplicationStrategy( myDuplicationStrategy );

        // EventListenerRegistry defines 3 ways to register listeners:
        //     1) This form overrides any existing registrations with 
//        eventListenerRegistry.setListeners( EventType.AUTO_FLUSH, myCompleteSetOfListeners );
        //     2) This form adds the specified listener(s) to the beginning of the listener chain
//        eventListenerRegistry.prependListeners( EventType.AUTO_FLUSH, myListenersToBeCalledFirst );
        //     3) This form adds the specified listener(s) to the end of the listener chain
        eventListenerRegistry.prependListeners(EventType.PRE_LOAD, PreLoadEventListener.class);
        eventListenerRegistry.prependListeners(EventType.POST_LOAD, PostLoadEventListener.class);

	}

	@Override
	public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {

	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// TODO Auto-generated method stub

	}

}
