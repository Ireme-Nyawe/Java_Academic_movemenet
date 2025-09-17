package question1;
import java.util.Scanner;
public class Supermarket {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("|Enter number of items |Customer bought: ");
		int itemsNumber = scanner.nextInt();
		String[] items = new String[itemsNumber];
		double[] prices = new double[itemsNumber];
		int[] quantity = new int[itemsNumber];
		double subTotal = 0;
		double discount = 0;

		for(int i =0;i<itemsNumber;i++){
			System.out.print("|Enter Item "+(i+1)+" Name: ");
			items[i]=scanner.next();
			System.out.print("|Enter Item "+(i+1)+" Price: ");
			prices[i]=scanner.nextDouble();
			System.out.print("|Enter Item "+(i+1)+" Quantity: ");
			quantity[i]=scanner.nextInt();

		}

		System.out.println("\nPayment receipt \nItem names \t quantity \t unit price \t  ");
		System.out.println("-------------------------------------");

		for(int i =0;i<itemsNumber;i++){
			subTotal += prices[i]*quantity[i];		
			System.out.println(items[i]+" | "+quantity[i]+" | "+prices[i]);			
		}
		if(subTotal>50000){
			discount =(50000*5)/100;
		}
		System.out.println("Grand total: "+subTotal);
		System.out.println("Discount: "+discount);
		System.out.println("Final amount payable: "+(subTotal-discount));

	}

}
