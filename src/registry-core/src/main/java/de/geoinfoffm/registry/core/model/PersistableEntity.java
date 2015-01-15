//package de.geoinfoffm.registry.core.model;
//
//import java.util.UUID;
//
//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Inheritance;
//import javax.persistence.InheritanceType;
//import javax.persistence.Transient;
//
//import org.hibernate.annotations.Type;
//import org.hibernate.envers.Audited;
//
//import de.geoinfoffm.registry.core.xml.AbstractXmlSerializable;
//
///**
// * Abstract base class for persisted entities with a UUID primary key.
// * 
// * @author Florian Esser
// * 
// */
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Access(AccessType.PROPERTY)
//@Audited @Entity 
//public abstract class PersistableEntity extends AbstractXmlSerializable implements Persistable<UUID>
//{
//	private static final long serialVersionUID = -7672440477735478179L;
//
//	private UUID uuid;
//	
//	public PersistableEntity() {
//		this.uuid = null;
//	}
//	
//	public PersistableEntity(UUID id) {
//		this.uuid = id;
//	}
//	
//	@Override
//	@Id
////	@GeneratedValue(generator = "uuid")
////	@GenericGenerator(name = "uuid", strategy = "uuid2")
//	@Type(type = "pg-uuid")
//	public UUID getId() {
//		return uuid;
//	}
//
//	public void setId(UUID uuid) {
//		this.uuid = uuid;
//	}
//	
//	@Transient
//	public boolean isNew() {
//		return uuid == null;
//	}
//
//	@Override
//	public String toString() {
//		return String.format("Entity %s [type: %s]", this.getId(), this.getClass().getName());
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (o == null) {
//			return false;
//		}
//
//		if (o == this) {
//			return true;
//		}
//
//		if (!o.getClass().equals(this.getClass())) {
//			return false;
//		}
//
//		if (this.getId() == null) {
//			// Unpersisted entities cannot be compared
//			return false;
//		}
//
//		PersistableEntity other = (PersistableEntity)o;
//		return other.getId().equals(this.getId());
//	}
//
//	@Override
//	public int hashCode() {
//		if (this.getId() != null) {
//			return uuid.hashCode();
//		}
//		else {
//			return 1;
//		}
//	}	
//}
