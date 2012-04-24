package edu.shared;

import java.io.Serializable;

public class Answer implements Serializable{
	private int choice;
	public int question_id;
	public String eid;
	
	public Answer(int choice, int question_id, String eid){
		this.question_id = question_id;
		this.choice = choice;
		this.eid = eid;
	}

	public int getChoice() {
		return choice;
	}
	
	public char getChoiceLetter(){
		int choice_ascii = choice + 65;
		return  (char) choice_ascii;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	
}
