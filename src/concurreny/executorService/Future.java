package concurreny.executorService;

public class Future<T> {

	T result;
	CompletionStatus completionStatus = CompletionStatus.NOT_ASSIGNED;

	public T getResult() {
		try {
			synchronized (this) {

				if (completionStatus.equals(CompletionStatus.NOT_ASSIGNED)) {

					System.out.println("future obj waiting for the task to be assigned " + System.currentTimeMillis());
					this.wait();

				} else if (completionStatus.equals(CompletionStatus.IN_PROGRESS)) {

					System.out.println("waiting for thread to complete " + System.currentTimeMillis());
					this.wait();

				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (completionStatus.equals(CompletionStatus.INTERRUPTED)
				|| completionStatus.equals(CompletionStatus.REJECTED)) {
			result = null;
		}

		return result;
	}

	void setResult(Object result) {
		this.result = (T) result; // TODO: what will happen at the runtime? who will give the information about T
									// casting here?
	}

}
