package de.geoinfoffm.registry.api.forum;

import de.geoinfoffm.registry.core.EntityRelatedEvent;
import de.geoinfoffm.registry.core.forum.Post;

public class PostSubmittedEvent extends EntityRelatedEvent<Post>
{

	public PostSubmittedEvent(Post entity) {
		super(entity);
	}

}
