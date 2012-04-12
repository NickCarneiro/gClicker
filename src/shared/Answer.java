package shared;

import java.io.Serializable;

public class Answer implements Serializable{
	private int choice;
	public int question_id;
	
	public Answer(int choice, int question_id){
		this.question_id = question_id;
		this.choice = choice;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	
}
