package squawker.jdbi;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import squawker.User;


public class UserCache {
	private static volatile UserCache INSTANCE;

	public static UserCache getInstance() {
		if (INSTANCE != null)
			return INSTANCE;

		synchronized (UserCache.class) {
			if (INSTANCE != null)
				return INSTANCE;

			INSTANCE = new UserCache();
			return INSTANCE;
		}
	}

	private UserCache() { }
	private final Map<Integer, WeakReference<User>> users = new ConcurrentHashMap<>();
	
	public User get(Integer user_id){
		return get(users.get(user_id));
	}
	private User get(WeakReference<User> w) {
		return w == null ? null : w.get();
	}

	public void put(squawker.User user){
		if(user.getId().getClass() != Integer.class)
			throw new IllegalArgumentException("unpersisted user");
		
		users.put((Integer)user.getId(), new WeakReference<>(user));
	}

	public Optional<User> findByUsername(String username) {
		return users.values().stream().map(this::get).filter(u -> u != null && u.getUsername().equals(username)).findFirst();
	}

}
