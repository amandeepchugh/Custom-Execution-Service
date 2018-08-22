package concurreny.executorService;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class ExecutorServiceImpl implements ExecutorService {

	private static final int THREAD_POOL_SIZE = 1;
	private Thread[] threadPool = new Thread[THREAD_POOL_SIZE];
	private Queue<Task<?>> taskQueue = new LinkedBlockingQueue<>();

	public ExecutorServiceImpl() {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new WorkerThread("thread number " + i);
			threadPool[i].start();
		}
	}

	@Override
	public <T> Future<T> submit(Callable<T> callable) throws Exception {

		Future<T> futureObj = new Future<T>();

		Task<T> task = new Task<T>(callable, futureObj);

		synchronized (taskQueue) {
			taskQueue.add(task);
			taskQueue.notify();
			System.out.println("task queue notified");

		}

		return futureObj;
	}

	class WorkerThread extends Thread {

		public WorkerThread(String name) {
			super(name);
		}

		@Override
		public void run() {

			try {

				while (true) {
					Task<?> task = null;
					synchronized (taskQueue) {
						System.out.println(Thread.currentThread().getName() + " is waiting for task");
						taskQueue.wait();
						System.out.println(Thread.currentThread().getName() + " is starting to execute");

						task = taskQueue.poll();
					}
					Callable<?> callable = task.getCallable();
					Future<?> futureObj = task.getFutureObj();
					futureObj.completionStatus = CompletionStatus.IN_PROGRESS;

					Object result = callable.call();

					futureObj.setResult(result);
					futureObj.completionStatus = CompletionStatus.COMPLETED;
					synchronized (futureObj) {
						futureObj.notify();
					}

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	}
}
