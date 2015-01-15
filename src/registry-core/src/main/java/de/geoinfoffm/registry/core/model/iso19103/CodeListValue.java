package de.geoinfoffm.registry.core.model.iso19103;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import de.geoinfoffm.registry.core.ValueObject;

/**
 * Class for codelist values.
 * 
 * @author Florian Esser
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class CodeListValue extends ValueObject
{
	private static final long serialVersionUID = -2457064963628233278L;

	@XmlAttribute(name = "codeList", namespace = "http://www.isotc211.org/2005/gmd", required = true)
	@Column(columnDefinition = "text")
	private String codeList;
	@XmlAttribute(name = "codeListValue", namespace = "http://www.isotc211.org/2005/gmd", required = true)
	@Column(columnDefinition = "text")
	private String codeListValue;
	@XmlAttribute(name = "codeSpace", namespace = "http://www.isotc211.org/2005/gmd")
	@Column(columnDefinition = "text")
	private String codeSpace;
	
	@XmlValue
	@Column(columnDefinition = "text")
	private String value;
	
	@XmlTransient
	private QName type;
	
	protected CodeListValue() {
		
	}
	
	public CodeListValue(String codeList, String codeListValue, String value, QName type) {
		this(codeList, codeListValue, null, value, type);
	}
	
	public CodeListValue(String codeList, String codeListValue, String codeSpace, String value, QName type) {
		this.codeList = codeList;
		this.codeListValue = codeListValue;
		this.codeSpace = codeSpace;
		this.type = type;
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public String getCodeList() {
		return codeList;
	}
	
	public String getCodeListValue() {
		return codeListValue;
	}

	public String getCodeSpace() {
		return codeSpace;
	}
}
