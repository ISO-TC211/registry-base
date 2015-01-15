package de.geoinfoffm.registry.core.model.iso19115;

import java.util.Date;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.model.DateAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Standardized resource reference
 * @created 10-Sep-2013 19:42:28
 */
@XmlType(name = "CI_Citation_Type", 
		 namespace = "http://www.isotc211.org/2005/gmd",
		 propOrder = { "title", "alternateTitle", "date", "edition", "editionDate", "identifier",
					   "citedResponsibleParty", "presentationForm", "series", "otherCitationDetails",
					   "collectiveTitle", "isbn", "issn"})
@XmlRootElement(name = "CI_ResponsibleParty", namespace = "http://www.isotc211.org/2005/gmd")
@XmlAccessorType(XmlAccessType.FIELD)
@Access(AccessType.FIELD)
@Audited @Entity
public class CI_Citation extends de.geoinfoffm.registry.core.Entity
{

	/**
	 * Name by which the cited information is known
	 */
	@XmlElement(name = "title", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String title;
	/**
	 * Short name or other language name by which the cited information is known.
	 * -Example: "Digital Chart of the World" or "DCW"
	 */
	@XmlElement(name = "alternateTitle", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	@ElementCollection
	private Set<String> alternateTitle;
	/**
	 * Reference date for the cited information
	 */
	@XmlElement(name = "date", namespace = "http://www.isotc211.org/2005/gmd")
	@ElementCollection
	@AttributeOverrides({
		@AttributeOverride(name = "date", column = @Column(name = "date_date")),
		@AttributeOverride(name = "dateType", column = @Column(name = "date_dateType")),
		@AttributeOverride(name = "dateType.codeList", column = @Column(name = "date_dateType_codeList", length = 2000)),
		@AttributeOverride(name = "dateType.codeListValue", column = @Column(name = "date_dateType_codeListValue", length = 2000)),
		@AttributeOverride(name = "dateType.codeSpace", column = @Column(name = "date_dateType_codeSpace", length = 2000)),
		@AttributeOverride(name = "dateType.type", column = @Column(name = "date_dateType_type"))
	})
	private Set<CI_Date> date;
	/**
	 * Version of the dataset
	 */
	@XmlElement(name = "edition", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String edition;
	/**
	 * Date of the edition
	 */
	@XmlElement(name = "editionDate", namespace = "http://www.isotc211.org/2005/gmd", type = de.geoinfoffm.registry.core.model.iso19103.Date.class)
	@XmlJavaTypeAdapter(DateAdapter.class)
	@Temporal(TemporalType.DATE)
	private Date editionDate;
	/**
	 * value uniquely identifying an object within a namespace
	 */
	@XmlElement(name = "identifier", namespace = "http://www.isotc211.org/2005/gmd")
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "code", column = @Column(name = "identifier_code", length = 2000)),
		@AttributeOverride(name = "authority", column = @Column(name = "identifierAuthority", length = 2000))
	})
	private MD_Identifier identifier;
	/**
	 * Name and position information for an individual or organisation that is
	 * responsible for the resource.
	 */
	@XmlElement(name = "citedResponsibleParty", namespace = "http://www.isotc211.org/2005/gmd")
	@OneToMany
	private Set<CI_ResponsibleParty> citedResponsibleParty;
	/**
	 * Mode in which the data is represented
	 */
	@XmlElement(name = "presentationForm", namespace = "http://www.isotc211.org/2005/gmd")
	@ElementCollection
	private Set<CI_PresentationFormCode> presentationForm;
	/**
	 * Name of the series of which the dataset is a part
	 */
	@XmlElement(name = "series", namespace = "http://www.isotc211.org/2005/gmd")
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "name", column = @Column(name = "series_name", length = 2000)),
		@AttributeOverride(name = "issueIdentification", column = @Column(name = "series_issueIdentification", length = 2000)),
		@AttributeOverride(name = "page", column = @Column(name = "series_page", length = 2000))		
	})
	private CI_Series series;
	/**
	 * Other information required to complete the citation
	 */
	@XmlElement(name = "otherCitationDetails", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String otherCitationDetails;
	/**
	 * Common title with holdings note.
	 */
	@XmlElement(name = "collectiveTitle", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String collectiveTitle;
	/**
	 * International Standard Book Number.
	 */
	@XmlElement(name = "ISBN", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	private String isbn;
	/**
	 * International Standard Serial Number.
	 */
	@XmlElement(name = "ISSN", namespace = "http://www.isotc211.org/2005/gmd", type = CharacterString.class)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	private String issn;

	public CI_Citation(){

	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the alternateTitle
	 */
	public Set<String> getAlternateTitle() {
		return alternateTitle;
	}

	/**
	 * @param alternateTitle the alternateTitle to set
	 */
	public void setAlternateTitle(Set<String> alternateTitle) {
		this.alternateTitle = alternateTitle;
	}

	/**
	 * @return the date
	 */
	public Set<CI_Date> getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Set<CI_Date> date) {
		this.date = date;
	}

	/**
	 * @return the edition
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * @param edition the edition to set
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * @return the editionDate
	 */
	public Date getEditionDate() {
		return editionDate;
	}

	/**
	 * @param editionDate the editionDate to set
	 */
	public void setEditionDate(Date editionDate) {
		this.editionDate = editionDate;
	}

	/**
	 * @return the identifier
	 */
	public MD_Identifier getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(MD_Identifier identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the citedResponsibleParty
	 */
	public Set<CI_ResponsibleParty> getCitedResponsibleParty() {
		return citedResponsibleParty;
	}

	/**
	 * @param citedResponsibleParty the citedResponsibleParty to set
	 */
	public void setCitedResponsibleParty(Set<CI_ResponsibleParty> citedResponsibleParty) {
		this.citedResponsibleParty = citedResponsibleParty;
	}

	/**
	 * @return the presentationForm
	 */
	public Set<CI_PresentationFormCode> getPresentationForm() {
		return presentationForm;
	}

	/**
	 * @param presentationForm the presentationForm to set
	 */
	public void setPresentationForm(Set<CI_PresentationFormCode> presentationForm) {
		this.presentationForm = presentationForm;
	}

	/**
	 * @return the series
	 */
	public CI_Series getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(CI_Series series) {
		this.series = series;
	}

	/**
	 * @return the otherCitationDetails
	 */
	public String getOtherCitationDetails() {
		return otherCitationDetails;
	}

	/**
	 * @param otherCitationDetails the otherCitationDetails to set
	 */
	public void setOtherCitationDetails(String otherCitationDetails) {
		this.otherCitationDetails = otherCitationDetails;
	}

	/**
	 * @return the collectiveTitle
	 */
	public String getCollectiveTitle() {
		return collectiveTitle;
	}

	/**
	 * @param collectiveTitle the collectiveTitle to set
	 */
	public void setCollectiveTitle(String collectiveTitle) {
		this.collectiveTitle = collectiveTitle;
	}

	/**
	 * @return the iSBN
	 */
	public String getISBN() {
		return isbn;
	}

	/**
	 * @param isbn the iSBN to set
	 */
	public void setISBN(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the iSSN
	 */
	public String getISSN() {
		return issn;
	}

	/**
	 * @param issn the iSSN to set
	 */
	public void setISSN(String issn) {
		this.issn = issn;
	}

	public void finalize() throws Throwable {

	}
}//end CI_Citation