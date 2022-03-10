package comp6521_assignment1;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = -1;
		
		while(true) {
			boolean exit = false;
			System.out.println("Select choice: ");
			System.out.println("1.	Create a random list of integers");
			System.out.println("2.	Display the random list");
			System.out.println("3.	Run 2PMMS");
			System.out.println("4.	Exit");
			
			TwoPassMultiwayMerge t = new TwoPassMultiwayMerge();
			int choice = sc.nextInt();
			
			switch(choice) {
			case 1:
				System.out.println("Enter number of integers in input.txt:");
				n = sc.nextInt();
				t.generateRandomInts("input.txt", n);
				break;
				
			case 2:
				System.out.println("Unsorted random generated list:");
				t.display();
				break;
				
			case 3:
				System.out.println("Enter memory size:");
				int m = sc.nextInt();
				try {
					t.firstPhase(m, n);
					t.secondPhase();
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
//				if(n == -1) {
//					System.out.println("Select option 1 before this option");
//				}else {
//					
//				}
				break;
			
			case 4:
				exit = true;
				break;
				
			}
			
			if(exit) break;
		}
		sc.close();
	}
	
}

