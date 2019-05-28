// This is the View
// Its only job is to display what the user sees
// It performs no calculations, but instead passes
// information entered by the user to whomever needs
// it. 
package dictionary;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.*;

class DictionaryViewFrame{

	protected JTextField searchWord  = new JTextField(15);		// get user input word
	protected JButton searchButton;								// search button
	protected JTextField dicNameInput  = new JTextField(15);	// get dictionary name to create new dictionary
	protected JTextArea meaning = new JTextArea(10, 10);		// translation field
	JPanel dicPanel = new JPanel();
	GridBagConstraints gbc = new GridBagConstraints();
	String[] blankArr = {""};	// blank array to reset suggestion list when no word is typed
	JList<String> wordList = new JList<String>(blankArr);	// word suggestion list
	JScrollPane list, translation;	// scroll pane of suggestion list and translation field
	static String[] dicNames;
	//DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<String>(testDicName);
	JComboBox<String> dicList = new JComboBox<String>(dicNames);

	DictionaryViewFrame(String buttonName, boolean textAreaEditable, boolean showList, boolean showDicList, boolean showDicNameInput){	
		
		JPanel searchPanel = new JPanel();
		searchButton = new JButton(buttonName);
		int buttonX = 20;
		if(showDicNameInput){
			buttonX = 21;
		}
		// searchPanel.add(searchButton);

		//**********************************SETUP VIEW**************************************8
		
		/* text field to type word into */
		
		dicPanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(5,5,5,5);		
		gbc.weightx = 250;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 20;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		// searchWord.setBorderPainted(false);
		searchWord.setBorder(null);
		// PromptSupport.setPrompt("Find word", searchWord);
		dicPanel.add(searchWord, gbc);


		/* search button */
		gbc.fill = GridBagConstraints.VERTICAL;
		if(showDicNameInput){
			gbc.anchor = GridBagConstraints.EAST;
		}
		gbc.weightx = 3000;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = buttonX;
		gbc.gridy = 0;
		// JPanel blankPanel = new JPanel();
		// blankPanel.add(searchButton);
		// dicPanel.add(blankPanel, gbc);
		// gbc.fill = GridBagConstraints.NONE;
		dicPanel.add(searchButton, gbc);
		// deletePanel.add(deleteButton, gbc);


		/*add dictionary list*/
		gbc.fill = GridBagConstraints.BOTH;
		// gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 3000;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 21;
		gbc.gridy = 0;
		if(showDicList){
			dicPanel.add(dicList, gbc);
		}

		gbc.fill = GridBagConstraints.BOTH;
		// gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 6000;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 20;
		gbc.gridy = 0;
		dicNameInput.setBorder(null);		
		if(showDicNameInput){
			dicPanel.add(dicNameInput, gbc);
		}

		/*word list for suggestion*/
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 250;
		gbc.weighty = 500;
		gbc.fill = GridBagConstraints.BOTH;
		list = new JScrollPane(wordList);
		if(showList){
			dicPanel.add(list, gbc);
		}
		// deletePanel.add(list, gbc);

		/*translation*/
		meaning.setEditable(textAreaEditable);
		meaning.setLineWrap(true);				
		meaning.setWrapStyleWord(true);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 500;
		gbc.weighty = 500;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		translation = new JScrollPane(meaning);
		dicPanel.add(translation, gbc);
		// deletePanel.add(translation, gbc);

		// this.add(dicPanel);
		// this.setVisible(true);
	}
	/*Set dictonary names to combo box*/
	static void setDicNames(String[] dicNamesReceived){
		dicNames = dicNamesReceived;
	}
	
	/*function to get current word in search box to send to model via controller*/
	public String getSearchWord(){
		
		return (searchWord.getText());
		
	}
	
	/*function to set translation received from model via controller*/
	public void setMeaning(String meaning){
		
		this.meaning.setText(meaning);
		this.meaning.setCaretPosition(0);
		
	}
	
	/*function to update suggestion list when user type a new character*/
	public void updateList(Set<String> wordList){
		// Object[] array = wordList.toArray();
		String[] words = new String[wordList.size()];
		words = wordList.toArray(words);
		// this.wordList.setListData(Arrays.copyOfRange(words, 0, 10));
		this.wordList.setListData(words);		
	}


	/*function to reset list to blank when there is no character in search box*/
	public void resetList(){
		this.wordList.setListData(blankArr);		
	}

	/*add listener to combo box*/
	public void addDicListItemListener(ItemListener listener){
		this.dicList.addItemListener(listener);
	}
	
	/*function to add listener to search button, listen to search button being pressed*/
	public void addButtonListener(ActionListener listenForButton){
		
		this.searchButton.addActionListener(listenForButton);
		
	}

	/*function to add listener to search box, listen to a key being pressed and update suggestion list*/
	public void addPartialListener(KeyListener listenForPartial){
		
		this.searchWord.addKeyListener(listenForPartial);
		
	}

	/*function to add listener to suggestion list, listen to an item being selected => update search box and set meaning*/
	public void addListListener(ListSelectionListener listener){
		this.wordList.addListSelectionListener(listener);
	}
	
	// Open a popup that contains the error message passed
	
	// void displayErrorMessage(String errorMessage){
		
	// 	JOptionPane.showMessageDialog(this, errorMessage);
		
	// }
	// public static void main(String args[]){
	// 	new DictionaryView();
	// }
}


public class DictionaryView extends JFrame{
	JTabbedPane tabbedPane = new JTabbedPane();
	DictionaryViewFrame search = new DictionaryViewFrame("Search", false, true, true, false);
	DictionaryViewFrame delete = new DictionaryViewFrame("Delete", false, true, true, false);
	DictionaryViewFrame add = new DictionaryViewFrame("Add", true, false, true, false);
	DictionaryViewFrame addDic = new DictionaryViewFrame("Add Dictionary", true, false, false, true);

	public DictionaryView(){
		this.setSize(600,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Dictionary");
		this.getContentPane().setForeground(Color.YELLOW);

		tabbedPane.addTab("SEARCH", search.dicPanel);
		tabbedPane.addTab("DELETE", delete.dicPanel);
		tabbedPane.addTab("ADD", add.dicPanel);
		tabbedPane.addTab("ADD DICTIONARY", addDic.dicPanel);
		this.add(tabbedPane);
		this.setVisible(true);
		
	}

	public static void main(String[] args){
		new DictionaryView();
	}
}