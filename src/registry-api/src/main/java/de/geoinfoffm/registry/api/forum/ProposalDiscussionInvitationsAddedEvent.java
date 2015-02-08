package de.geoinfoffm.registry.api.forum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.geoinfoffm.registry.core.EntityRelatedEvent;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion;

public class ProposalDiscussionInvitationsAddedEvent extends EntityRelatedEvent<ProposalDiscussion>
{
	private List<String> inviteeMailAddresses;

	public ProposalDiscussionInvitationsAddedEvent(ProposalDiscussion entity, Collection<String> inviteeMailAddresses) {
		super(entity);
		
		this.inviteeMailAddresses = new ArrayList<String>();
		this.inviteeMailAddresses.addAll(inviteeMailAddresses);
	}
	
	public List<String> getInviteeMailAddresses() {
		return Collections.unmodifiableList(inviteeMailAddresses);
	}
}
