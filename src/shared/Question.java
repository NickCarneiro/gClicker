package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
	private String id;
	//answer choices
	private ArrayList<String> choices;
	private String question_text;
		
	public Question(String question){
		question_text = question;
		choices = new ArrayList<String>();
	}
	
	public void addChoice(String choice){
		choices.add(choice);
	}
	
	public int size(){
		if(choices == null){
			return 0;
		} else {
			return choices.size();
		}
		
	}
	public String getQuestionText(){
		return this.question_text == null ? "Empty Question" : this.question_text;
	}
	public String toString(){
		String question = question_text + "\n";
		int i = 0;
		for(String q : choices){
			char letter = (char) (i + 65);
			question += letter + ": ";
			question += q;
			question += "\n";
			i++;
		}
		return question;
	}
}
