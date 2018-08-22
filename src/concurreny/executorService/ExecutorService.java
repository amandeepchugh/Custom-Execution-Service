package concurreny.executorService;

import java.util.concurrent.Callable;

public interface ExecutorService {

	public <T> Future<T> submit(Callable<T> callable) throws Exception;
}
