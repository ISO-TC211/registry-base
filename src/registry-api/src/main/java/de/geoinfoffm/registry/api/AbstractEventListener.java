package de.geoinfoffm.registry.api;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.RegistryUser;

public class AbstractEventListener 
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractEventListener.class);
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private RegistryConfiguration registryConfiguration;

	public AbstractEventListener() {
		super();
	}
	
	protected JavaMailSender mailSender() {
		return mailSender;
	}
	
	protected MessageSource messageSource() {
		return messageSource;
	}
	
	protected VelocityEngine velocityEngine() {
		return velocityEngine;
	}
	
	protected RegistryConfiguration registryConfiguration() {
		return registryConfiguration;
	}

	protected void sendMailToUser(final RegistryUser user, final String subjectKey, final String template, 
			final String baseUrl, final Map<String, Object> model) throws MailException {
	    model.put("user", user);

	    InternetAddress recipient;
		try {
			recipient = new InternetAddress(user.getEmailAddress(), user.getName());
		}
		catch (UnsupportedEncodingException e) {
			throw new MailPreparationException(e.getMessage(), e);
		}

		this.sendMail(recipient, subjectKey, template, user.getPreferredLanguage(), baseUrl, model);
	}

	protected void sendMail(final InternetAddress recipient, final String subjectKey, final String template, 
			final String preferredLanguage, final String baseUrl, final Map<String, Object> model) throws MailException {

		logger.debug("{Registry Mail} Mail submission requested [subjectKey = '{}', recipient = '{}']", subjectKey, recipient.getAddress());

		Locale userPreferredLanguage;
		try {
			userPreferredLanguage = Locale.forLanguageTag(preferredLanguage);
		}
		catch (Throwable t) {
			userPreferredLanguage = Locale.ENGLISH;
		}

//		model.put("baseUrl", ClientConfiguration.getMailBaseUrl());
		model.put("baseUrl", baseUrl);

		String templatePath = template + ".vm";
		String localizedTemplatePath = template + "_" + preferredLanguage.toLowerCase() + ".vm";
		
		final String subject = messageSource.getMessage(subjectKey, new Object[] {}, userPreferredLanguage);

		String body;
		try {
			body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, localizedTemplatePath, "UTF-8", model);
		}
		catch (Throwable t) {
			// Fall back to default template if language-specific template is not to be found
			body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, "UTF-8", model);
		}

		if (!registryConfiguration().isMailEnabled()) {
			logger.debug("{Registry Mail} Did NOT send e-mail with subject {} to {}: e-mails are disabled globally", recipient.getAddress(), subject);
			return;
		}
		else if (recipient.getAddress().contains("@example.org")) {
			logger.debug("{Registry Mail} Did NOT send e-mail with subject {} to {}: e-mails to addresses ending in @example.org are not supported", recipient.getAddress(), subject);
			return;			
		}
		final String finalBody = body;

		logger.debug("{Registry Mail} Sending e-mail with subject {} to {}", recipient, subject);
		mailSender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setFrom(registryConfiguration.getSenderAddress());
				message.setTo(recipient);
				message.setSubject(subject);
	            message.setText(finalBody, true);
			}
		});
	}
}