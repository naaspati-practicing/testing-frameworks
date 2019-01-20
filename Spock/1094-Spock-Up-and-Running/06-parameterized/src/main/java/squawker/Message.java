package squawker;

import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Message implements Comparable<Message> {
	public static final int MAX_TEXT_LENGTH = 140;
	
	protected final Serializable id;
	@ColumnName("posted_by") private final User postedBy;
	@ColumnName("_text")     private final String text;
	@ColumnName("posted_at") private final Instant postedAt;
	
	public Message(User postedBy, String text, Instant postedAt) {
		this(UUID.randomUUID(), postedBy, text, postedAt);
	}
	
	public Message(int id, @Nested User postedBy, String text, Instant postedAt) {
		this(Integer.valueOf(id), postedBy, text, postedAt);
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
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", postedBy=" + postedBy + ", text=" + text + ", postedAt=" + postedAt + "]";
	}

	@Override
	public int compareTo(Message o) {
		return this.postedAt.compareTo(o.postedAt);
	}
	
}
