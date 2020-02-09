package nameGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Generator {
	String gender;
	int maxLength;
	int minLength;
	int maxNumNames;
	int order;
	Hashtable<String, ArrayList<Object>> markovModel;
	MarkovModelBuilder builder;
	ArrayList<String> trainingWords;

	public Generator(String gender, int maxLength, int minLength, int order, int maxNumNames) throws IOException {
		this.gender = gender;
		this.maxLength = maxLength;
		this.minLength = minLength;
		this.maxNumNames = maxNumNames;
		this.order = order;
		this.builder = new MarkovModelBuilder();

	}

	// generate() calls getName() and print only the names that fit the user
	// requirements
	public void generate() throws IOException {
		this.markovModel = builder.build(gender, order);
		this.trainingWords = builder.getTrainingWords();
		int numNames = 0;
		while (numNames < maxNumNames) {
			String name = this.getName();
			// only prints name if it matches the user requirements
			if (name.length() <= maxLength && name.length() >= minLength && name.length() != 0
					&& !(trainingWords.contains(name))) {
				System.out.println(name);
				numNames++;
			}
		}
	}

	// print() is used for debugging, this prints out the Markov model
	private void print(Hashtable<String, ArrayList<Object>> markovModel) {
		for (Map.Entry<String, ArrayList<Object>> entry : markovModel.entrySet()) {
			System.out.println(entry.getKey() + " ");
			for (Object obj : entry.getValue()) {
				System.out.print(obj + " ");
			}
			System.out.println("");

		}

	}

	// getName() generates the name based off the information in the Markov model
	private String getName() {

		ArrayList<String> name = this.prepareName();
		double random = 0;
		String letter = "";
		while (!letter.equals("_")) {
			random = Math.random();
			String key = this.getKey(name);
			// gets the list of percentages based on the key
			ArrayList<Object> list = markovModel.get(key);
			double countWeight = 0;

			for (int i = 1; i < list.size(); i = i + 2) {
				// keep adding countWeight until their is a percentages that is >= to the random
				// number
				countWeight = countWeight + (float) list.get(i);
				if ((float) list.get(i) + countWeight >= random) {
					// gets the letter that corresponds to the percentage
					letter = (String) list.get(i - 1);
					break;
				}

			}

			name.add(letter);
		}

		// removes all _ from the name and returns it
		String stringName = "";
		for (String l : name) {
			if (!l.equals("_")) {
				stringName = stringName + l;
			}
		}

		return stringName;

	}

	// prepares the start of the of the name based on the order of the Markov model
	private ArrayList<String> prepareName() {
		ArrayList<String> name = new ArrayList<>();
		for (int i = 0; i < this.order; i++) {
			name.add("_");
		}

		return name;
	}

	// returns the key based off the order of the model so that we need to search on
	// the Markov model
	private String getKey(ArrayList<String> name) {
		String key = "";
		int lastIndex = name.size();
		for (int j = lastIndex - this.order; j < lastIndex; j++) {
			key = key + name.get(j);
		}
		return key;

	}

}
