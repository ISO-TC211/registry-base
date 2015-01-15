package de.geoinfoffm.registry.core;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class NullMailSender implements JavaMailSender
{

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		// do nothing
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		// do nothing
	}

	@Override
	public MimeMessage createMimeMessage() {
		// do nothing
		return null;
	}

	@Override
	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		// do nothing
		return null;
	}

	@Override
	public void send(MimeMessage mimeMessage) throws MailException {
		// do nothing
	}

	@Override
	public void send(MimeMessage[] mimeMessages) throws MailException {
		// do nothing
	}

	@Override
	public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
		// do nothing
	}

	@Override
	public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
		// do nothing
	}

}
