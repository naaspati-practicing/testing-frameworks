package squawker;

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
	
	public Message(User postedBy, String text, Instant postedAt){
		this(UUID.randomUUID(), postedBy, text, postedAt);
	}
	public Message(int id, User postedBy, String text, Instant postedAt){
		this(Integer.valueOf(id), postedBy, text, postedAt);
	}
	public Message(Serializable id, User postedBy, String text, Instant postedAt) {
		this.id = requireNonNull(id);
		this.postedBy = requireNonNull(postedBy);
		this.text = requireNonNull(text);
		this.postedAt = requireNonNull(postedAt);
		
		if(text.length() > MAX_TEXT_LENGTH)
			throw new IllegalArgumentException("given text length("+text.length()+") exceeds max allowed length("+MAX_TEXT_LENGTH+")");
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
	@Override
	public String toString() {
		return getClass()+"@"+id;
	}
	@Override
	public int compareTo(Message o) {
		return postedAt.compareTo(o.postedAt);
	}
	
}
