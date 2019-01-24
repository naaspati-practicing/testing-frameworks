package squawker;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import static java.util.Collections.*;
import static java.util.Objects.*;

import java.beans.ConstructorProperties;

public class User {
	private static int mod = 0;
	
	protected final Serializable id;
	private final String username;
	protected final Set<User> following = new HashSet<>();
	protected final List<Message> posts = new ArrayList<>();
	private final Instant registered;
	
	public User(String username) {
		this(username, Instant.now());
	}
	public User(String username, Instant registered) {
		this(UUID.randomUUID(), username, registered);
	}
	
	@ConstructorProperties({"id", "username", "registered"})
	public User(int id, String username, Instant registered) {
		this(Integer.valueOf(id), username, registered);
	}
	public User(Serializable id, String username, Instant registered) {
		this.id = requireNonNull(id);
		this.username = requireNonNull(username);
		this.registered = requireNonNull(registered);
		
		if(username.isEmpty())
			throw new IllegalArgumentException("username cannot only empty string");
		if(username.trim().isEmpty())
			throw new IllegalArgumentException("username cannot only consist empty space");
		if(!username.trim().equals(username))
			throw new IllegalArgumentException("username cannot have trailing/following spaces");
	}
	public Serializable getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public Set<User> getFollowing() {
		return unmodifiableSet(following);
	}
	public List<Message> getPosts() {
		return unmodifiableList(posts);
	}
	public Instant getRegistered() {
		return registered;
	}
	public void follow(User followee) {
		Objects.requireNonNull(followee);
		if(this.equals(followee))
			throw new IllegalArgumentException("a user cannot follow themself");
		
		following.add(followee);
	}
	public boolean isFollowing(User followee) {
		return following.contains(followee);
	}
	public Message post(String content, Instant at) {
		Message msg = new Message(this, content, at);
		posts.add(0, msg);
		mod++;
		return msg;
	}
	
	private WeakReference<List<Message>> wtimeline = new WeakReference<List<Message>>(null);
	private int mod_timeline = -1;
	
	public List<Message> timeline() {
		if(posts.isEmpty() && following.isEmpty())
			return Collections.emptyList();
		
		List<Message> timeline;
		
		if(mod_timeline == mod) {
			timeline = wtimeline.get();
			if(timeline != null)
				return timeline;
		}
		
		timeline = new ArrayList<>();
		timeline.addAll(posts);
		
		if(!following.isEmpty()) {
			for (User f : following) 
				timeline.addAll(f.posts);
		}
		
		Collections.sort(timeline, Comparator.reverseOrder());
		timeline = unmodifiableList(timeline);
		wtimeline = new WeakReference<>(timeline); 
		return timeline;
	}
	@Override
	public String toString() {
		return "@".concat(username);
	}
	@Override
	public int hashCode() {
		return username.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}
}
