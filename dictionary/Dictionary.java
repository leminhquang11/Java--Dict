package dictionary;
import java.util.*;
import java.io.*; 

class Dictionary{
	public static void main(String args[]){
		DictionaryController.setDicNamesToView();
		DictionaryView theView = new DictionaryView();
		DictionaryModel theModel = new DictionaryModel();
		DictionaryController theController = new DictionaryController(theView, theModel);
		Dictionary dictionary = new Dictionary();

		theModel.createTreeMap();
		// System.out.println(dictionary.findWord("dog"));
		// dictionary.viewTreeMap();
		
		// theView.setVisible(true);
	}
}