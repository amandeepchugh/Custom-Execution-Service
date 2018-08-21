package concurreny.executorService;

import java.util.concurrent.Callable;


public class Task<T> {
	private Callable<T> callable;
	private FutureObj<T> futureObj;
	

	public Task(Callable<T> callable, FutureObj<T> futureObj) {
		this.callable = callable;
		this.futureObj = futureObj;
	}

	public Callable<T> getCallable() {
		return callable;
	}


	public FutureObj<T> getFutureObj() {
		return futureObj;
	}

}