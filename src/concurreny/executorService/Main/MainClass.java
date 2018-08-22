package concurreny.executorService.Main;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import concurreny.executorService.ExecutorService;
import concurreny.executorService.ExecutorServiceImpl;
import concurreny.executorService.Future;

public class MainClass {

	public static void main(String[] args) throws Throwable {
		ExecutorService executorService = new ExecutorServiceImpl();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= 100; i++) {
			numbers.add(i);
		}
		Callable<Integer> sumOfNumbers = new CallableSumOfArray(numbers);
	
		Future<Integer> futureObj = executorService.submit(sumOfNumbers);
		Integer result = futureObj.getResult();
		
		System.out.println(result);
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
		Thread.sleep(3000);
		return sum;
	}
	
}
