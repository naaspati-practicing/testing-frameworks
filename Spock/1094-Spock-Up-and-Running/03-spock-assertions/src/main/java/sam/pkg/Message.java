package sam.pkg;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message implements Comparable<Message> {
	public static final int MAX_TEXT_LENGTH = 140;
	
	protected final Serializable id;
	private final User postedBy;
	private final String text;
	private final Instant postedAt;
	
	public Message(User postedBy, String text, Instant postedAt) {
		this(UUID.randomUUID(), postedBy, text, postedAt);
	}
	
	public Message(Serializable id, User postedBy, String text, Instant postedAt) {
		if(text.length() > MAX_TEXT_LENGTH)
			throw new IllegalArgumentException("Message text cannot be longer than 140 characters");
		
		this.id = requireNonNull(id);
		this.postedBy = requireNonNull(postedBy);
		this.text = text;
		this.postedAt = requireNonNull(postedAt);
	}
	
	public Serializable getId() {
		return id;
	}
	public User getPostedBy() {
		return postedBy;
	}
	public String getText() {
		return text;
	}
	public Instant getPostedAt() {
		return postedAt;
	}
	
	private static final String prefix = Message.class.getName().concat("@");
	
	@Override
	public String toString() {
		return prefix.concat(id.toString());
	}
	
	@Override
	public int compareTo(Message o) {
		return this.postedAt.compareTo(o.postedAt);
	}
}
