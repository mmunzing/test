import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
//https://orajavasolutions.wordpress.com/tag/sleeping-barber-problem/

public class Hotel {
	protected static int roomNum = 1;
	protected static Semaphore s1 = new Semaphore(0, true);
	protected static Semaphore s2 = new Semaphore(0, true);
	protected static Semaphore checkinQSema = new Semaphore(1, true);
	protected static Semaphore bellhopQSema = new Semaphore(1, true);
	
	protected static Queue<Guest> checkinQ = new LinkedList<Guest>();
	protected static Queue<Guest> bellhopQ = new LinkedList<Guest>();
	
	private void runHotel(){
		Employee empArr[] = new Employee[2];
		Thread empThreads[] = new Thread[2];
		Bellhop bellArr[] = new Bellhop[2];
		Thread bellThreads[] = new Thread[2];
		Guest guestArr[] = new Guest[25];
	    Thread guestThreads[] = new Thread[25];
	    
	    // Create 2 front desk workers
	    for(int i = 0; i < empArr.length; i++){
	    	empArr[i] = new Employee(i);
	    	empThreads[i] = new Thread(empArr[i]);
	    	empThreads[i].start();
	    }
	    
	    // Create 2 Bellhops
	    for(int j = 0; j < bellArr.length; j++){
	    	bellArr[j] = new Bellhop(j);
	    	bellThreads[j] = new Thread(bellArr[j]);
	    	bellThreads[j].start();
	    }
	    
	    // Create 25 Guests
	    for(int k = 0; k < guestArr.length; k++ ) {
	    	guestArr[k] = new Guest(k);
	        guestThreads[k] = new Thread( guestArr[k] );
	        guestThreads[k].start();
	    }
		
	}	// End of runHotel function
	
	public static void main(String[] args) {
		Hotel hotel = new Hotel();
		
		System.out.println("Simulation starts");
		
		hotel.runHotel();
		
	    try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
	    
	    System.out.println("Simulation ends");
	    System.exit(0);
	}
}



	class Employee implements Runnable {
	private int empNum;
	private Guest helping;
	Employee(int num) {
		this.empNum = num;
	}	// End of constructor
	
	public void run() {
		System.out.println("Front desk employee " + empNum + " created");
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
		}
		Hotel.s1.release();
		try
		{
			Hotel.s2.acquire();
			Hotel.checkinQSema.acquire();
			helping = Hotel.checkinQ.remove();
			Hotel.checkinQSema.release();
		}
		catch (InterruptedException e)
		{
		}
		System.out.println("Front desk employee " + empNum + " registers guest " + helping.getNum() + " and assigns room " + Hotel.roomNum);
		helping.setRoomNum(Hotel.roomNum);
		
		Hotel.roomNum++;
	}	// End of run function
	
	public void post() {
		System.out.println("EmpPost");
	}	// End of post function
}	// End of Employee class

class Bellhop implements Runnable {
	private int bellNum;
	Bellhop (int num) {
		this.bellNum = num;
	}	// End of constructor
	
	public void run() {
		System.out.println("Bellhop " + bellNum + " created");
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
		}
	}	// End of run function
	
	public void post() {
		System.out.println("BellPost");
	}	// End of post function
}	// End of Bellhop class

class Guest implements Runnable {
	private int guestNum;
	private int numBags;
	private int roomNum;
	
	Guest(int num){
		this.guestNum = num;
	}
	
	public void run() {
	   System.out.println( "Guest " + guestNum + " created" );
	   try {
		   Thread.sleep(1000);
	   } 
	   catch (InterruptedException e) {
	   }
	   
	   
	   try
	   {
	      Hotel.s1.acquire();
	      
	      Hotel.checkinQSema.acquire();
	      Hotel.checkinQ.add(this);
	      Hotel.checkinQSema.release();
	      
	      randBags();
		   if(numBags == 1)
			   System.out.println( "Guest " + guestNum + " enters hotel with " + numBags + " bag" );
		   else
			   System.out.println( "Guest " + guestNum + " enters hotel with " + numBags + " bags" );
		   Thread.sleep(1000);
	   }
	   catch (InterruptedException e)
	   {
	   }	   
	   
	   Hotel.s1.release();
	}	// End of run function
	
//	public void post() {
//	   Hotel.s1.release();
//	}	// End of post function
	
	public int getNum(){
		return guestNum;
	}	// End of getNum function
	
	public void setRoomNum(int num){
		this.roomNum = num;
	}	// End of setRoomNum function
	
	public int getRoomNum(){
		return this.roomNum;
	}	// End of getRoomNum function
	
	private void randBags(){
		Random randomNum = new Random();
    	this.numBags = randomNum.nextInt(6);
	}	// End of randBags function
}	// End of Employee Class

