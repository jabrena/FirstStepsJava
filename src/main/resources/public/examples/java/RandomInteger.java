import java.util.Random;

/**
 * This example show how to generate Random Numbers.
 * */
public class RandomInteger {
  
  public static void main(final String[] args) {

    System.out.println("Generating 10 random numbers.");

    int randomNumber = 0;
    final Random randomGenerator = new Random();
    for (int x = 1; x <= 10; x++) {
      randomNumber = randomGenerator.nextInt(100);
      System.out.println("Generated : " + randomNumber);
    }

    System.out.println("Done.");
  }

}