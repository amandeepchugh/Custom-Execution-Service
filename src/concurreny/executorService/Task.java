package concurreny.executorService;

import java.util.concurrent.Callable;


public class Task<T> {
	private Callable<T> callable;
	private Future<T> futureObj;
	

	public Task(Callable<T> callable, Future<T> futureObj) {
		this.callable = callable;
		this.futureObj = futureObj;
	}

	public Callable<T> getCallable() {
		return callable;
	}


	public Future<T> getFutureObj() {
		return futureObj;
	}

}