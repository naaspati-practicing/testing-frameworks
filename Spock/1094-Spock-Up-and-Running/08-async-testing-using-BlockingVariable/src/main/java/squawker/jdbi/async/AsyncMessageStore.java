package squawker.jdbi.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

import squawker.Message;
import squawker.User;
import squawker.jdbi.MessageStore;

public class AsyncMessageStore {
	private final MessageStore mStore;
	private final ExecutorService ex;
	
	public AsyncMessageStore(MessageStore messageStore, ExecutorService executorService) {
		this.mStore = messageStore;
		this.ex = executorService;
	}
	
	public void latestPostBy(User user, BiConsumer<Message, Throwable> callback) {
		CompletableFuture.supplyAsync(() -> mStore.latestPostBy(user), ex)
		.whenCompleteAsync(callback);
	}
	public void latestPostsByFollowed(User user, BiConsumer<Message, Throwable> callback) {
	    user
	      .getFollowing()
	      .forEach(followed -> latestPostBy(followed, callback));
	  }
}
