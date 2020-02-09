package nameGenerator;

import java.io.IOException;
import java.util.Scanner;

// Created by Brandon Kessinger

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner reader = new Scanner(System.in);

		System.out.println("Welcome to the name generator!");
		System.out.println("Enter 'm' for male names or enter 'f' for female names");
		String gender = reader.nextLine();
		System.out.println("Enter the minimum name length");
		int minLength = reader.nextInt();
		System.out.println("Enter the maximum name length");
		int maxLength = reader.nextInt();
		System.out.println("Enter the order of the Markov model");
		int order = reader.nextInt();
		System.out.println("Enter the number of names you would like to generate");
		int numNames = reader.nextInt();

		Generator generator = new Generator(gender, maxLength, minLength, order, numNames);
		generator.generate();
		reader.close();

	}

}
