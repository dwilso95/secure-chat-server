package chat;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import chat.clearance.ClearanceLevel;

/**
 * Object for handling messages between client and server.
 * 
 * Contains the message, the clearance level of the message, the user's dn which
 * sent the message, and a signature based on the message.
 */
@XmlRootElement
public class ChatMessage {

	private String message;
	private ClearanceLevel clearanceLevel;
	private String signature;
	private String userDn;

	/**
	 * Empty default constructor. Required for simple JAXB marshaling.
	 */
	public ChatMessage() {

	}

	/**
	 * Constructor which defines all fields of the instance.
	 * 
	 * @param message
	 *            - String
	 * @param clearanceLevel
	 *            - {@link ClearanceLevel}
	 * @param signature
	 *            - Base64 encoded signature of included message
	 * @param userDn
	 *            - Dn of the user who send the message
	 */
	public ChatMessage(final String message, final ClearanceLevel clearanceLevel, final String signature,
			final String userDn) {
		this.message = message;
		this.clearanceLevel = clearanceLevel;
		this.signature = signature;
		this.userDn = userDn;
	}

	@XmlElement
	public final String getMessage() {
		return message;
	}

	public final void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public final ClearanceLevel getClearanceLevel() {
		return clearanceLevel;
	}

	public final void setClearanceLevel(ClearanceLevel clearanceLevel) {
		this.clearanceLevel = clearanceLevel;
	}

	@XmlElement
	public final String getSignature() {
		return signature;
	}

	public void setSignature(final String signature) {
		this.signature = signature;
	}

	@XmlElement
	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public static final String toXML(final ChatMessage chatMessage) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(ChatMessage.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			final StringWriter writer = new StringWriter();
			jaxbMarshaller.marshal(chatMessage, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException("Problem transforming chat message XML to ChatMessage", e);
		}
	}

	public static final ChatMessage fromXML(final String chatMessageXML) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(ChatMessage.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (ChatMessage) jaxbUnmarshaller.unmarshal(new StringReader(chatMessageXML));
		} catch (JAXBException e) {
			throw new RuntimeException("Problem transforming chat message XML to ChatMessage");
		}
	}

}
