package de.geoinfoffm.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public class RegisterRelatedRole extends Role
{
	@ManyToOne(optional = false)
	private RE_Register register;

	protected RegisterRelatedRole() {
	}

	public RegisterRelatedRole(String name, RE_Register register) {
		super(name);
		
		this.setRegister(register);
	}

	public RE_Register getRegister() {
		return register;
	}

	public void setRegister(RE_Register register) {
		this.register = register;
	}
}
