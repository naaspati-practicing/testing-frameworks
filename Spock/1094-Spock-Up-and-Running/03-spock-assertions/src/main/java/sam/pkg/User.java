package sam.pkg;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class User {
	protected final Serializable id;
	private final String username;
	protected final  Set<User> following = new HashSet<>();
	protected final List<Message> posts = new ArrayList<>();
	protected final Instant registered;
	
	public User(String username) {
		this(username, Instant.now());
	}
	public User(String username, Instant registered) {
		this(UUID.randomUUID(), username, registered);
	}
	public User(Serializable id, String username, Instant registered) {
		this.id = requireNonNull(id);
		this.username = requireNonNull(username);
		this.registered = requireNonNull(registered);
		
		if (username.isEmpty()) 
		      throw new IllegalArgumentException("username must be at least 1 character long");
	}
	public Serializable getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public Set<User> getFollowing() {
		return Collections.unmodifiableSet(following);
	}
	public List<Message> getPosts() {
		return Collections.unmodifiableList(posts);
	}
	public Instant getRegistered() {
		return registered;
	}
	public void follow(User follow) {
		if(this.equals(follow))
			throw new IllegalArgumentException("a user cannot follow themself");
		
		following.add(requireNonNull(follow));
	}
	public boolean isFollowing(User user) {
		return user != null && following.contains(user);
	}
	public boolean post(String msg) {
		return post(msg, Instant.now());
	}
	public boolean post(String msg, Instant time) {
		return posts.add(new Message(this, msg, time));
	}
	public Collection<Message> timeline() {
		TreeSet<Message> set = new TreeSet<>();
		
		set.addAll(posts);
		following.forEach(s -> set.addAll(s.posts));
		return Collections.unmodifiableCollection(set);
	}
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}
}
