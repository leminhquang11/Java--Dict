package dictionary;
import java.util.*;
import java.io.*; 
public class DictionaryModel{
	
	TreeMap<String, String> treeMap = new TreeMap<>();
	TreeMap<String, String> myTreeMap = new TreeMap<>();
	String line, word, translation;
	BufferedReader inBr;
	BufferedWriter outBr;
	String folder = "C:/Users/Admin/Downloads/MVCDictionary-master/dictionary/";
	File file;
	File out;
	File fileChooser;
	int noDic;
	ArrayList<String> dicNames;
	// String line;
	int currentDic = 0;

	DictionaryModel(){
		dicNames = new ArrayList<String>();
		this.translation = "";
		this.readDicList();

		file = new File(folder+dicNames.get(0));
		out = new File(folder+dicNames.get(0));
		// try{
		// 	file = new File(folder+dicNames.get(0));
		// 	out = new File(folder+dicNames.get(0));
		// 	}
		// catch(IOException e){
		// 	e.getMessage();
		// }
	}

	void readDicList(){
		fileChooser = new File(folder+"dictionaryList.txt");
		try{
			this.inBr = new BufferedReader(new FileReader(fileChooser));
			// noDic = Integer.parseInt(inBr.readLine());
			// dicNames = new String[noDic];
			line = inBr.readLine();
			while(line!=null){
				dicNames.add(line);
				line = inBr.readLine();
			}

			}
		catch(IOException e){
			e.getMessage();
		}
	}

	ArrayList<String> getDicNames(){
		return dicNames;
	}

	void addWordNewTreeMap(String key, String value){
		myTreeMap.clear();
		String valueNew = "@"+key+"\n"+value;
		myTreeMap.put(key, valueNew);
	}

	void createDic(String dicNameReceived){
		File fileOut = new File(folder+dicNameReceived);
		File fileList = new File(folder+"dictionaryList.txt");
		BufferedWriter myOutBr;
		try{
			myOutBr = new BufferedWriter(new FileWriter(fileOut));
			for (Map.Entry<String, String> entry : myTreeMap.entrySet()) {
			    // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); //this statement prints out my keys and values
			    myOutBr.write(entry.getValue());
			    myOutBr.write("\n");
			    System.out.println("Done");
			    myOutBr.flush();   // Flush the buffer and write all changes to the disk
			}
			OutputStream os = new FileOutputStream(fileList, true);
			os.write(dicNameReceived.getBytes(), 0, dicNameReceived.length());
			os.write("\n".getBytes(), 0, "\n".length());
			os.close();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}

		System.out.println("1");
	}

	void resetTreeMap(String dicName){
		// dicName += ".txt";
		file = new File(folder+dicName);
		out = new File(folder+dicName);
		// this.inBr = new BufferedReader(new FileReader(this.file));
		treeMap.clear();
		this.createTreeMap();
	}

	// word handler to insert into treemap

	String word_handle(String word){
		String str;
		String[] strCopy, strCopy2;
		strCopy = word.split("@");
		strCopy2 = strCopy[1].split("/");
		int len = strCopy2[0].length();
		if(strCopy2[0].charAt(len-1) == ' '){
			str = strCopy2[0].substring(0, strCopy2[0].length() - 1);
		}else{
			str = strCopy2[0];
		}
		return str;
	}
	// create treemap 
	void createTreeMap(){	  
	    int i;
	    int first_word_flag = 1;
	    int word_count = 0;
	    String currentTranlation;
	    try{
	    	this.inBr = new BufferedReader(new FileReader(this.file));
			do{
				line = (inBr.readLine());
				if(line == null){
					treeMap.put(word, translation);
					translation = "";
				}
				else if(!line.equals("")){
					if(line.charAt(0) == '@')
			        {
			            if(first_word_flag == 0)
			            {
			            	// System.out.println(this.word + "======" + this.translation);
			            	if((currentTranlation = treeMap.get(word)) != null){
			            		currentTranlation += translation;
			            		treeMap.put(word, currentTranlation);
			            	}else{
			                	treeMap.put(word, translation);
			                }
			                translation = "";
			                currentTranlation = "";
			            }
			            word = word_handle(line);
			            first_word_flag = 0;
			            word_count++;
			            if(!translation.equals(""))
			        		translation += "\n";
			            translation += line;
			        }
			        else{
			        	if(!translation.equals(""))
			        		translation += "\n";
			            translation += line;
			        }
				}
			} while(line != null);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	   
	    System.out.printf("Inserted %d words into TreeMap dictionary!\n", word_count);
	}
	// view key value pair in treemap to test
	void viewTreeMap(){
		this.treeMap.forEach((key, value) -> System.out.println(
        "Key = " + key + "\nvalue = " + value));

	}

	// function to get list of word start with a prefix as a parameter
	public <V> SortedMap<String, V> filterPrefix(TreeMap<String,V> baseMap, String prefix) {
	    if(prefix.length() > 0) {
	        char nextLetter = (char)((int)prefix.charAt(prefix.length() -1) + 1);
	        String end = prefix.substring(0, prefix.length()-1) + nextLetter;
	        return baseMap.subMap(prefix, end);
	    }
	    return baseMap;
	}

	// return a set of word start with a prefix to dictionary view (suggestion list)
	Set<String> getPartial(String prefix){
		// String[] wordList;
		// for(Map.Entry<String,String> entry : filterPrefix(treeMap, prefix).entrySet()) {
		//     System.out.println(entry.getKey());
		// }
		return(filterPrefix(treeMap, prefix).keySet());

	}

	// find meaning of word and return value back to the view
	String searchForWord(String word){
		return treeMap.get(word);
	}

	public void deleteWord(String key){
		treeMap.remove(key);
	}

	public void addWord(String key, String value){
		String valueNew = "@"+key+"\n"+value;
		treeMap.put(key, valueNew);
	}

	public void writeToFile(){
		try{
			this.outBr = new BufferedWriter(new FileWriter(this.out));
			for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			    // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); //this statement prints out my keys and values
			    outBr.write(entry.getValue());
			    outBr.write("\n");
			    System.out.println("Done");
			    outBr.flush();   // Flush the buffer and write all changes to the disk
			}
			treeMap.clear();
			this.createTreeMap();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	// public static void main(String args[]){
	// 	DictionaryModel obj = new DictionaryModel();
	// 	System.out.println("shit");
	// 	obj.createTreeMap();
	// 	// obj.getPartial();
	// 	obj.writeToFile();
	// }
}