package edu.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{
	public int id;
	public static Integer last_id = 0;
	//answer choices
	private ArrayList<String> choices;
	//
	private ArrayList<Integer> answer_counts;
	private String question_text;

	//if message is true, this is a dummy question. It's only purpose is to tell the client its assigned clicker id.
	public boolean message = false;


	public int clicker_id;

	public Question(String question){
		question_text = question;
		choices = new ArrayList<String>();
		answer_counts = new ArrayList<Integer>();

		//not thread safe, but only one thread is instantiating questions.
		last_id++;
		//this is the id for the question itself
		this.id = last_id;


	}

	/**
	 * Add an answer choice that a client is able to select.
	 * eg: "Hydrogen"
	 * @param choice
	 */
	public void addChoice(String choice){
		choices.add(choice);
		answer_counts.add(0);
	}

	public synchronized void incrementAnswer(int choice){
		if(choice < 0 || choice > choices.size() - 1){
			System.err.println("Choice was not in valid range.");

		}else {
			answer_counts.set(choice, answer_counts.get(choice) + 1);
		}
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

	public String[] getAnswers() 
	{ 
		String returnMe[] = new String[choices.size()];

		return choices.toArray(returnMe); 
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

	public void printResults(){
		if(answer_counts == null){
			return;
		}

		int i = 0;
		for(Integer count : answer_counts){
			char letter = (char) (i + 65);
			System.out.println(letter + ": " + count);
			i++;
		}
	}
}