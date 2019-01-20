package squawker.jdbi;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import squawker.Message;
import squawker.User;

public class PersistentUser extends User {
	
	private DataStore dataStore;

	public PersistentUser(int id, String username, Instant registered) {
		super(id, username, registered);
	}
	public PersistentUser(Serializable id, String username, Instant registered) {
		super(id, username, registered);
	}
	public PersistentUser(String username, Instant registered) {
		super(username, registered);
	}
	public PersistentUser(String username) {
		super(username);
	}
	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	@Override
	public Message post(String msg, Instant time) {
		Message m = super.post(msg, time);
		dataStore.insert(m);
		return m;
	}
	
	private boolean following_loaded;
	
	@Override
	public Set<User> getFollowing() {
		if(!following_loaded) {
			following_loaded = true;
			following.addAll(dataStore.findFollowees(this));
		}
		return super.getFollowing();
	}
	
	private boolean posts_loaded;
	
	@Override
	public List<Message> getPosts() {
		if(!posts_loaded) {
			posts_loaded = true;
			posts.addAll(dataStore.postsBy(this));
		}
		return super.getPosts();
	}
	@Override
	public void follow(User follow) {
		super.follow(follow);
		dataStore.follow(this, follow);
	}
	
	@Override
	public Collection<Message> timeline() {
		return dataStore.timeline(this);
	}
}
