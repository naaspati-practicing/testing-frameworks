package squawker.jdbi;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;


public class PersistentUserCache {
	private final Map<Integer, WeakReference<PersistentUser>> users;

	private PersistentUserCache(Map<Integer, WeakReference<PersistentUser>> users) {
		this.users = users;
	}
	public PersistentUser get(Integer user_id){
		return get(users.get(user_id));
	}
	private PersistentUser get(WeakReference<PersistentUser> w) {
		return w == null ? null : w.get();
	}

	public void put(PersistentUser user){
		users.put(user.getId(), new WeakReference<>(user));
	}
	public void clear() {
		users.clear();
	}
	public Optional<PersistentUser> findByUsername(String username) {
		return users.values().stream()
				.map(this::get)
				.filter(u -> u != null && u.getUsername().equals(username))
				.findFirst();
	}

}
