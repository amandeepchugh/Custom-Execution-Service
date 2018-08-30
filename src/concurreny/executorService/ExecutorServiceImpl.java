package concurreny.executorService;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionException;

public class ExecutorServiceImpl implements ExecutorService {

	private static final int THREAD_POOL_SIZE = 100;
	private Thread[] threadPool = new Thread[THREAD_POOL_SIZE];
	private Queue<Task<?>> taskQueue = new LinkedList<Task<?>>();
	private boolean gracefulShutdown = false;
	private int taskCount = 0;

	public ExecutorServiceImpl() {
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new WorkerThread("thread number " + i);
			threadPool[i].start();
		}
	}

	@Override
	public <T> Future<T> submit(Callable<T> callable) throws RejectedExecutionException {

		if (gracefulShutdown) {
			throw new RejectedExecutionException();
		}

		Future<T> futureObj = new Future<T>();

		Task<T> task = new Task<T>(callable, futureObj);

		synchronized (taskQueue) {
			taskQueue.add(task);
			taskQueue.notify();
			System.out.println("task queue notified " + ++taskCount);

		}

		return futureObj;
	}

	class WorkerThread extends Thread {

		public WorkerThread(String name) {
			super(name);
		}

		@Override
		public void run() {

			while (true) {

				if (isInterrupted()) {
					break;
				}

				Task<?> task = null;
				try {
					synchronized (taskQueue) {
						if (gracefulShutdown && taskQueue.isEmpty()) {
							break;
						}
						boolean stopThread = false;
						while (taskQueue.isEmpty()) {
							System.out.println(Thread.currentThread().getName() + " is waiting for task");
							taskQueue.wait();
							
							if (gracefulShutdown && taskQueue.isEmpty()) {
								stopThread = true;
								break;
							}
							
						}
						if (stopThread) {
							break;
						}

						task = taskQueue.poll();
						System.out.println(Thread.currentThread().getName() + " is starting to execute");
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

				} catch (InterruptedException e) {
					if (task != null) {
						Future<?> futureObj = task.getFutureObj();
						futureObj.completionStatus = CompletionStatus.INTERRUPTED;
						synchronized (futureObj) {
							futureObj.notify();
						}

					}
					break;
					// log it
					// do nothing?
				} catch (Exception e) {
					e.printStackTrace();
					// throw new RuntimeException(e);??
				}
			}

		}

	}

	@Override
	public void shutdown() {
		gracefulShutdown = true;
		synchronized (taskQueue) {
			taskQueue.notifyAll();
		}
	}

	@Override
	public List<Callable<?>> shutdownNow() {
		shutdown();

		for (Thread thread : threadPool) {
			thread.interrupt();
		}

		List<Callable<?>> unexecutedCallables = new LinkedList<>();
		synchronized (taskQueue) {
			for (Task<?> task : taskQueue) {
				unexecutedCallables.add(task.getCallable());
				task.getFutureObj().completionStatus = CompletionStatus.REJECTED;
			}
			taskQueue.clear();
		}

		return unexecutedCallables;

	}
}
