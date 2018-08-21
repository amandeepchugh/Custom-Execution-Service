package concurreny.executorService;

import java.util.concurrent.Callable;

public interface ExecutorService {

	public <T> FutureObj<T> submit(Callable<T> callable) throws Exception;
}
