import java.util.Random;

/**
 * This example show how to generate Random Numbers.
 *
 * */
public class RandomInteger {
  
  public static void main(String[] aArgs){

    System.out.println("Generating 10 random numbers.");

    Random randomGenerator = new Random();
    for (int x = 1; x <= 10; x++) {
      int randomInt = randomGenerator.nextInt(100);
      System.out.println("Generated : " + randomInt);
    }

    System.out.println("Done.");
  }

}