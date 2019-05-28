package dictionary;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class DictionaryController{
	private DictionaryView dicView;
	private DictionaryModel dicModel;

	public DictionaryController(DictionaryView view, DictionaryModel model){
		this.setDicNamesToView();
		this.dicView = view;
		this.dicModel = model;

		/*add necessary listener to element in the view*/
		this.dicView.search.addButtonListener(new searchListener(dicView.search));
		this.dicView.search.addPartialListener(new partialListener(dicView.search));
		this.dicView.search.addListListener(new listSelectionListener(dicView.search));
		this.dicView.search.addDicListItemListener(new dicListListener(dicView.search));
		
		this.dicView.delete.addButtonListener(new deleteListener(dicView.delete));
		this.dicView.delete.addPartialListener(new partialListener(dicView.delete));
		this.dicView.delete.addListListener(new listSelectionListener(dicView.delete));
		this.dicView.delete.addDicListItemListener(new dicListListener(dicView.delete));
		

		this.dicView.add.addButtonListener(new addListener(dicView.add));
		this.dicView.add.addDicListItemListener(new dicListListener(dicView.add));

		this.dicView.addDic.addButtonListener(new addDicListener(dicView.addDic));



		// dicView.addButton.addAddListener(new addListener());
		// dicView.deleteButton.addDeleteListener(new deleteListener());
	}

	public static void setDicNamesToView(){
		DictionaryModel dicModel = new DictionaryModel();
		ArrayList<String> dicNamesReceived = dicModel.getDicNames();
		String[] dicNames = new String[dicNamesReceived.size()];
		try{
			dicNamesReceived.toArray(dicNames);
			DictionaryViewFrame.setDicNames(dicNames);
		}catch(Exception e){
			System.out.println(e.getMessage());
		
		}
	}

	// public class partialListener implements ActionListener{
	// 	private Set<String> wordList;
	// 	public void actionPerformed(ActionEvent event){
	// 		String partial = dicView.getSearchWord();
	// 		wordList = dicModel.getPartial(partial);
	// 		dicView.updateList(wordList);
	// 	}
	// }
	/*check whether a character pressed is a valid one and add to the current word in search box*/
	private boolean isAccepted(char c){
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '-') || c == '_' || c == '.' || c == ',' || c == '\'' || c == ' '){
			return (true);
		}
		return (false);
	}


	/*handle action when a key is pressed*/
	public class partialListener extends KeyAdapter{
		private Set<String> wordList;
		private String meaning;
		private DictionaryViewFrame obj;
		partialListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void keyPressed(KeyEvent event){
			char c = event.getKeyChar();
			String partial = obj.getSearchWord();
			
			if(isAccepted(c)){
				wordList = dicModel.getPartial(partial+c);
				obj.updateList(wordList);
			}else if(c == 8){
				if(partial.length() == 1){
					obj.resetList();
				}else{
					wordList = dicModel.getPartial(partial.substring(0, partial.length() - 1));
					obj.updateList(wordList);
				}
			}else if(c == event.VK_ENTER){
				meaning = dicModel.searchForWord(partial);
				obj.setMeaning(meaning);
			}
			else if(c == event.VK_DOWN){
				// meaning = dicModel.searchForWord(partial);
				// obj.setMeaning(meaning);
				obj.wordList.setSelectedIndex(0);
			}	
		}
	}

	/*action when new dictionary selected*/
	public class dicListListener implements ItemListener{
		private DictionaryViewFrame obj;
		String dicName;
		dicListListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void itemStateChanged(ItemEvent evt) {
	    // JComboBox cb = (JComboBox) evt.getSource();

	    Object item = evt.getItem();
	    if (evt.getStateChange() == ItemEvent.SELECTED) {
	      // Item was just selected
	    	dicName = item.toString();
	    	System.out.println(dicName);
	    	dicModel.resetTreeMap(dicName);
	    }
	    // else if (evt.getStateChange() == ItemEvent.DESELECTED) {
	    //   // Item is no longer selected
	    // }
	  }
	}

	/*action when search button is pressed*/
	public class searchListener implements ActionListener{
		private String meaning;
		private DictionaryViewFrame obj;
		searchListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void actionPerformed(ActionEvent event){
			String word = obj.getSearchWord();
			meaning = dicModel.searchForWord(word);
			obj.setMeaning(meaning);
			// try{
			// 	String word = obj.getSearchWord();
			// 	meaning = dicModel.searchForWord(word);
			// 	obj.setMeaning(meaning);
			// }
			// catch(/* need some error*/){
			// 	// handle the error
			// }
		}
	}
	/*action when an item of list is selected*/
	public class listSelectionListener implements ListSelectionListener{
		private String meaning;
		private DictionaryViewFrame obj;
		listSelectionListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void valueChanged(ListSelectionEvent le) {
		    String word = obj.wordList.getSelectedValue();
		    if (word != null) {
		    	// String word = name[idx];
		      System.out.println("Current selection: " + word);
		      // search for word, set text to jtext
		      obj.searchWord.setText(word);
		      meaning = dicModel.searchForWord(word);
			  obj.setMeaning(meaning);
		    } else {
		      System.out.println("Please choose a name");
		    }
		  }
	}
	
	public class deleteListener implements ActionListener{
		private DictionaryViewFrame obj;
		deleteListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void actionPerformed(ActionEvent event){
			int selection = JOptionPane.showConfirmDialog(null, "Do you really want to delete: " + obj.searchWord.getText(), "Confirm Delete", 2);
			if(selection == JOptionPane.OK_OPTION){
				dicModel.deleteWord(obj.searchWord.getText());
				dicModel.writeToFile();
				System.out.println("ok selected");
			
			} else if(selection == JOptionPane.CANCEL_OPTION){
				System.out.println("cancel selected");
			}
		}
	}

	public class addListener implements ActionListener{
		private DictionaryViewFrame obj;
		String value, key;
		addListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void actionPerformed(ActionEvent event){
			key = obj.searchWord.getText();
			value = obj.meaning.getText();
			if((key.length() == 0) && (value.length() == 0)){
				JOptionPane.showMessageDialog(null, "No empty field allowed!", "ERROR ADDING", JOptionPane.INFORMATION_MESSAGE);
			}else{
				int selection = JOptionPane.showConfirmDialog(null, "Do you really want to add: " + key, "Confirm Delete", 2);
				if(selection == JOptionPane.OK_OPTION){
					dicModel.addWord(key, value);
					dicModel.writeToFile();
					System.out.println("ok selected");
				
				} else if(selection == JOptionPane.CANCEL_OPTION){
					System.out.println("cancel selected");
				}
			}
		}
	}

	public class addDicListener implements ActionListener{
		private DictionaryViewFrame obj;
		String value, key, dicName;
		addDicListener(DictionaryViewFrame obj){
			this.obj = obj;
		}
		public void actionPerformed(ActionEvent event){
			key = obj.searchWord.getText();
			value = obj.meaning.getText();
			dicName = obj.dicNameInput.getText();
			if((key.length() == 0) || (value.length() == 0) || (dicName.length() == 0)){
				JOptionPane.showMessageDialog(null, "No empty field allowed!", "ERROR ADDING", JOptionPane.INFORMATION_MESSAGE);
			}else{
				int selection = JOptionPane.showConfirmDialog(null, "Do you really want create: " + dicName, "Confirm Delete", 2);
				if(selection == JOptionPane.OK_OPTION){
					dicModel.addWordNewTreeMap(key, value);
					dicModel.createDic(dicName);
					dicModel.readDicList();
					System.out.println("ok selected");
				
				} else if(selection == JOptionPane.CANCEL_OPTION){
					System.out.println("cancel selected");
				}
			}
		}
	}
}