package vn.fs.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.fs.dto.MailInfo;
import vn.fs.service.SendMailService;

/**
 * @author DongTHD
 *
 */
@Service
public class SendMailServiceImplement implements SendMailService {
	@Autowired
	JavaMailSender sender;

	List<MailInfo> list = new ArrayList<>();

	@Override
	public void send(MailInfo mail) throws MessagingException, IOException {
		// Tạo message
		MimeMessage message = sender.createMimeMessage();
		// Sử dụng Helper để thiết lập các thông tin cần thiết cho message
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());

		if (mail.getAttachments() != null) {
			FileSystemResource file = new FileSystemResource(new File(mail.getAttachments()));
			helper.addAttachment(mail.getAttachments(), file);
		}

		// Gửi message đến SMTP server
		sender.send(message);

	}
	
//	private MimeMessage createDataMail(Email email) throws MessagingException, UnsupportedEncodingException {
//		MimeMessage msg = mailService.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
//		helper.setFrom(new InternetAddress(emailConfig.getSenderEmail(), emailConfig.getSenderDisplayName()));
//		helper.setReplyTo(new InternetAddress(email.getSenderEmail(), email.getSenderDisplayName()));
//		helper.setText(email.getContent(), true);
//		helper.setTo(email.getSendTo().split(","));
//		helper.setSubject(email.getSubject());
//		if (email.getCc() != null) {
//			helper.setCc(email.getCc().split(","));
//		}
//		if (email.getBcc() != null) {
//			helper.setBcc(email.getBcc().split(","));
//		}
//
//		// creates message part
//		MimeBodyPart messageBodyPart = new MimeBodyPart();
//		messageBodyPart.setContent(email.getContent(), "text/html; charset=UTF-8");
//
//		// creates multi-part
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(messageBodyPart);
//
//		MimeBodyPart attPart;
//
//		// adds inline image attachments
//		if (email.getAttachments() != null && !email.getAttachments().isEmpty()) {
//			for (EmailAttachment attachment : email.getAttachments()) {
//
//				try {
//					switch (attachment.getAttachmentType()) {
//					case IMAGE_LINK:
//						attPart = new MimeBodyPart();
//						attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//						attPart.attachFile(attachment.getContent());
//						attPart.setDisposition(MimeBodyPart.INLINE);
//						multipart.addBodyPart(attPart);
//						break;
//					case IMAGE_BASE64:
//						attPart = new PreencodedMimeBodyPart("base64");
//						attPart.setFileName(attachment.getName());
//						attPart.setText(attachment.getContent().replace("data:image/png;base64,", ""));
//						attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//						attPart.setDisposition(MimeBodyPart.INLINE);
//						multipart.addBodyPart(attPart);
//						break;
//					case CALENDAR:
//						attPart = new MimeBodyPart();
//						// attPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
//						attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//						attPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getContent(),
//								"text/calendar;method=REQUEST;name=\"invite.ics\"")));
//						attPart.setDisposition(MimeBodyPart.ATTACHMENT);
//						multipart.addBodyPart(attPart);
//						break;
//					case FILE:
//						attPart = new MimeBodyPart();
//						String base64Image =  attachment.getContent().split(",")[1];
//						byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
//						BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
//						ByteArrayOutputStream baos = new ByteArrayOutputStream();
//						ImageIO.write(img, "png", baos);
//						baos.flush();
//						byte[] imageBytes2= baos.toByteArray();
//						baos.close();
//						ByteArrayDataSource bds = new ByteArrayDataSource(imageBytes2, "image/png");
//						attPart.setDataHandler(new DataHandler(bds));
//						attPart.setFileName(attachment.getName());
//						messageBodyPart.setHeader("Content-ID", "<image>");
//						attPart.setDisposition(MimeBodyPart.ATTACHMENT);
//						multipart.addBodyPart(attPart);
//						break;
//					case EXCEL:
//						attPart = new MimeBodyPart();
//						attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//						DataSource source = new FileDataSource(attachment.getContent());
//						attPart.setDataHandler(new DataHandler(source));
//						attPart.setFileName(attachment.getName());
//						multipart.addBodyPart(attPart);
//						break;
//					}
//				} catch (Exception ex) {
//					logger.logError("Add Attachment failed for Attachment Type '"
//							+ attachment.getAttachmentType().name() + "' :" + ex.getMessage());
//				}
//			}
//		}
//
//		msg.setContent(multipart);
//
//		return msg;
//	}
//	
//	private String appendContentData(String content, Map<String, String> data) {
//		for (Map.Entry<String, String> entry : data.entrySet()) {
//			if (content.contains("${" + entry.getKey() + "}") && entry.getValue() != null)
//				content = content.replace("${" + entry.getKey() + "}", entry.getValue());
//		}
//		content = content.replace("${" + Constants.LOGO_URL + "}", notiIconConfig.getLogoUrl());
//		content = content.replace("${" + Constants.DOMAIN + "}", domainConfig.getDomain());
//		content = content.replace("${" + Constants.MAIL_TO + "}", domainConfig.getContactEmail());
//		return content;
//	}
//
//	private String appendSubjectData(String subject, Map<String, String> data) {
//		for (Map.Entry<String, String> entry : data.entrySet()) {
//			if (subject.contains("${" + entry.getKey() + "}") && entry.getValue() != null)
//				subject = subject.replace("${" + entry.getKey() + "}", entry.getValue());
//		}
//		return subject;
//	}

	@Override
	public void queue(MailInfo mail) {
		list.add(mail);
	}

	@Override
	public void queue(String to, String subject, String body) {
		queue(new MailInfo(to, subject, body));
	}

	@Override
	@Scheduled(fixedDelay = 5000)
	public void run() {
		while (!list.isEmpty()) {
			MailInfo mail = list.remove(0);
			try {
				this.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
