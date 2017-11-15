package chat.message;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import chat.server.ClearanceLevel;

@XmlRootElement
public class ChatMessage {

	private String message;
	private ClearanceLevel clearanceLevel;
	private String userDn;

	public ChatMessage() {

	}

	public ChatMessage(final String message, final ClearanceLevel clearanceLevel, final String userDn) {
		this.message = message;
		this.clearanceLevel = clearanceLevel;
		this.setUserDn(userDn);
	}

	@XmlElement
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public ClearanceLevel getClearanceLevel() {
		return clearanceLevel;
	}

	public void setClearanceLevel(ClearanceLevel clearanceLevel) {
		this.clearanceLevel = clearanceLevel;
	}

	@XmlElement
	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public static String toXML(final ChatMessage chatMessage) {

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

	public static ChatMessage fromXML(final String chatMessageXML) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(ChatMessage.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (ChatMessage) jaxbUnmarshaller.unmarshal(new StringReader(chatMessageXML));
		} catch (JAXBException e) {
			throw new RuntimeException("Problem transforming chat message XML to ChatMessage");
		}
	}

}
