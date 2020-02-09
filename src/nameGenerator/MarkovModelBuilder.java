package nameGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class MarkovModelBuilder {
	Hashtable<String, ArrayList<Object>> markovModel;
	int order;
	ArrayList<String> trainingWords;

	public Hashtable<String, ArrayList<Object>> build(String gender, int order) throws IOException {
		File trainingData;
		if (gender.equals("m")) {
			trainingData = new File("namesBoys.txt");
		} else {
			trainingData = new File("namesGirls.txt");
		}
		this.markovModel = new Hashtable<>();
		this.trainingWords = new ArrayList<>();
		this.order = order;

		this.parseData(trainingData);
		this.calculatePercentages();

		return markovModel;

	}

	// parseData() parses the training data and build the Markov model with the
	// number of occurrences of a letter after a key
	private void parseData(File data) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(data));
		String word = "";
		while ((word = reader.readLine()) != null) {
			// add each word to list of used training names
			this.trainingWords.add(word);
			// prepares word with correct number of _ before and after the word
			word = this.prepareWord(word);
			// splits the word into an array of letters
			String[] wordArray = word.split("");
			// iterate though the letter starting at the first actual letter not _
			for (int i = this.order; i < wordArray.length - 1; i++) {
				// gets the key to search or add to Markov model
				String key = this.getKey(wordArray, i);
				// if the key is already in the model update the list according to the occurance
				// of the letter after the key
				// if the letter is not in the valueList then add the letter
				if (markovModel.containsKey(key)) {
					ArrayList<Object> valueList = markovModel.get(key);
					if (valueList.contains(wordArray[i])) {
						int j = valueList.indexOf(wordArray[i]);
						valueList.set((j + 1), (int) valueList.get(j + 1) + 1);

					} else {
						valueList.add(wordArray[i]);
						valueList.add(1);
					}
					markovModel.put(key, valueList);

				} else {
					// adds the key to the with the letter after the key
					ArrayList<Object> tempList = new ArrayList<Object>();
					tempList.add(wordArray[i]);
					tempList.add(1);
					markovModel.put(key, tempList);
				}

			}
		}
		reader.close();
	}

	// calculatePercentages() calculates what the percentage for each letter that
	// appears after a key
	private void calculatePercentages() {
		for (Map.Entry<String, ArrayList<Object>> entry : markovModel.entrySet()) {
			ArrayList<Object> list = entry.getValue();
			float size = 0;
			// adds of the total number of possible letters that occurred after the key
			for (int i = 1; i < list.size(); i = i + 2) {
				size = size + (int) list.get(i);

			}
			// updates the list with the correct percentages based off the the size
			// calculated above
			for (int i = 1; i < list.size(); i = i + 2) {
				list.set(i, ((int) list.get(i) / size));

			}
			markovModel.put(entry.getKey(), list);

		}

	}

	// prepareWord() this adds x _'s to the beginning and end of each word where x
	// is the order of the model
	private String prepareWord(String word) {
		String prepend = "";
		for (int i = 0; i < this.order; i++) {
			prepend = prepend + "_";
		}
		word = prepend + word + prepend;
		return word;
	}

	// getKey() returns a key thats size is equal to the order of the model
	private String getKey(String[] wordArray, int i) {
		String key = "";
		for (int j = i - this.order; j < i; j++) {
			key = key + wordArray[j];
		}

		return key;

	}

	// getTrainingWords() returns all words used in the training data
	public ArrayList<String> getTrainingWords() {
		return trainingWords;
	}

}
