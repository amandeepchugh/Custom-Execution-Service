package concurreny.executorService;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;

public interface ExecutorService {

	public <T> Future<T> submit(Callable<T> callable) throws RejectedExecutionException;
	
	public void shutdown();
	
	public List<Callable<?>> shutdownNow();
}
