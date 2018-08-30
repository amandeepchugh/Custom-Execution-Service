package concurreny.executorService.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import concurreny.executorService.ExecutorService;
import concurreny.executorService.ExecutorServiceImpl;
import concurreny.executorService.Future;

public class MainClass {

	public static final int NUMBER_OF_TASKS = 200;
	public static void main(String[] args) throws Throwable {
		ExecutorService executorService = new ExecutorServiceImpl();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		
		for (int i = 1; i <= 100; i++) {
			numbers.add(i);
		}
		
		List<Callable<Integer>> sumOfNumbers = new ArrayList<Callable<Integer>>();
		for (int i = 0; i < NUMBER_OF_TASKS; i++) {
			Callable<Integer> sumofNumber = new CallableSumOfArray(numbers);
			sumOfNumbers.add(sumofNumber);
			
		}
	
		List<Future<Integer>> futureObjs = new ArrayList<>();
		for (int i = 0; i < sumOfNumbers.size(); i++) {
			Future<Integer> futureObj = executorService.submit(sumOfNumbers.get(i));
			futureObjs.add(futureObj);
		}
		
		executorService.shutdownNow();
		
		for (int i = 0; i < futureObjs.size(); i++) {
			System.out.println(i + " result " + futureObjs.get(i).getResult());
		}
		
		
	}

}

class CallableSumOfArray implements Callable<Integer> {
	

	private ArrayList<Integer> numbers;
	
	public CallableSumOfArray(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
	
	@Override
	public Integer call() throws Exception {
		 
		int sum = 0;
		for (Integer t : numbers) {
			sum += t.intValue();
		}
		Thread.sleep(3);
		return sum;
	}
	
}
