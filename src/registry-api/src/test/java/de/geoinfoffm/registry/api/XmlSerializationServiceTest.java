package de.geoinfoffm.registry.api;

import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.dom.DOMResult;

import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.junit.Test;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.PropertyType;
import de.geoinfoffm.registry.core.PropertyTypeAdapter;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.Integer;
import de.geoinfoffm.registry.core.model.iso19135.RE_AlternativeName;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterManager;
import de.geoinfoffm.registry.persistence.xml.StaxSerializationStrategy;
import de.geoinfoffm.registry.persistence.xml.StaxXmlSerializer;

public class XmlSerializationServiceTest {

	@Test
	public void testSerialize() throws Exception {
		CharacterString cs = new CharacterString("test");
		TestElement t3 = new TestElement(BigInteger.valueOf(3), "Test 3");
		TestElement t2 = new TestElement(BigInteger.valueOf(2), "Test 2");
		TestElement t1 = new TestElement(BigInteger.ONE, "Test 1");
		t1.addSuccessor(t2);
		t1.addSuccessor(t3);
		t2.addPredecessor(t1);
		t3.addPredecessor(t1);
		StringWriter sw = new StringWriter();
		
//		XmlSerializationService service = new XmlSerializationServiceImpl();
//		service.serialize(org, sw);
		
		StaxXmlSerializer<StringWriter> s = new StaxXmlSerializer<StringWriter>(new StaxSerializationStrategy(true));
		s.serialize(t1, sw);
		
		System.out.println(sw.toString());
	}
	
	@XmlRootElement(name = "T")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class T {

		@XmlJavaTypeAdapter(CharacterStringAdapter.class)
		private String name;
		
		@XmlJavaTypeAdapter(PropertyTypeAdapter.class)
		private RE_AlternativeName alternativeName;
		
		public T() { }
		
		public T(String name) {
			this.setName(name);
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public RE_AlternativeName getAlternativeName() {
			return alternativeName;
		}

		public void setAlternativeName(RE_AlternativeName alternativeName) {
			this.alternativeName = alternativeName;
		}
		
	}

	@Test
	public void testMoxy() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(RE_Register.class, T.class);
		
		RE_Register r = new RE_Register("Test");
		RE_RegisterManager mgr = new RE_RegisterManager();
		mgr.setName("Flo");
		r.setManager(mgr);
		
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);  
//		m.marshal(new RE_AlternativeName("teste"), sw);
		m.marshal(r, System.out);
//		m.marshal(new T("test"), System.out);
	}
	
	@XmlType(name = "Test_Type", namespace = "http://example.org/test", propOrder = { "id", "name", "successors", "predecessors", "status" })
	@XmlRootElement(name = "Test", namespace = "http://example.org/test")
	private static class TestElement implements Serializable {
		@XmlID
		@XmlAttribute(name = "uuid", namespace = "http://www.isotc211.org/2005/gco")
		private UUID uuid;
		
		@XmlElement(name = "successors", namespace = "http://example.org/test")
		private Set<TestElement> successors;

		@XmlElement(name = "predecessors", namespace = "http://example.org/test")
		@XmlInverseReference(mappedBy = "successors")
		private Set<TestElement> predecessors;

		@XmlElement(name = "name", namespace = "http://example.org/test", type = CharacterString.class)
		private String name;

		@XmlElement(name = "id", namespace = "http://example.org/test", type = Integer.class)
		private BigInteger id;
		
		@XmlElement(name = "status", namespace = "http://www.isotc211.org/2005/grg")
		private RE_ItemStatus status;

		public TestElement(BigInteger id, String name) {
			this.uuid = UUID.randomUUID();
			this.id = id;
			this.name = name;
			this.successors = new HashSet<XmlSerializationServiceTest.TestElement>();
			this.predecessors = new HashSet<XmlSerializationServiceTest.TestElement>();
			this.status = RE_ItemStatus.VALID;
		}
		
		public void addSuccessor(TestElement test) {
			this.successors.add(test);
		}
		
		public void addPredecessor(TestElement test) {
			this.predecessors.add(test);
		}
	}
}
