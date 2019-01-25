package squawker.jdbi;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.UnableToCreateStatementException;

import squawker.Message;
import squawker.User;

public class PersistentUser extends User {
	private final FollowingStore fs;
	private final MessageStore ms;

	public PersistentUser(FollowingStore fs, MessageStore ms, int id, String username, Instant registered) {
		super(id, username, registered);
		this.fs = fs;
		this.ms = ms;
	}

	@Override
	public Integer getId() {
		return (Integer)super.getId();
	}
	
	@Override
	public Message post(String content, Instant at) {
		Message m = new Message(0, this, content, at); //validating
		m = ms.insert(this, content, at); //insert
		posts.add(m);
		return m;
	}

	private boolean following_loaded;
	@Override
	public Set<User> getFollowing() {
		if(following_loaded)
			return super.getFollowing();
		following_loaded = true;
		try {
			following.addAll(fs.findFollowees(this));
		} catch (UnableToCreateStatementException e) {
			throw new IllegalStateException("database connection is stale", e);
		}
		return super.getFollowing();
	}
	private boolean posts_loaded;
	@Override
	public List<Message> getPosts() {
		if(posts_loaded)
			return super.getPosts();
		posts_loaded = true;
		
		try {
			posts.addAll(ms.postsBy(this));
		} catch (UnableToCreateStatementException e) {
			throw new IllegalStateException("database connection is stale", e);
		}
		return super.getPosts();
	}


	@Override
	public void follow(User followee) {
		super.follow(followee);
		fs.follow(this, followee);
	}
	
	@Override
	public List<Message> timeline() {
		return ms.timeline(this);
	}

}
