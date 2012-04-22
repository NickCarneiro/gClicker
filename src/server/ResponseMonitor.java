package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import shared.Answer;

/**
 * this class waits for the response from clients once a question is sent.
 * It's spun off as a thread so the ClientObserver doesn't block while waiting for responses to questions.
 * @author burt
 *
 */
public class ResponseMonitor implements Runnable {
	QuestionManager qm;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public ResponseMonitor(QuestionManager qm, ObjectInputStream in){
		this.qm = qm;
		this.in = in;
	}

	public void run(){
		Answer answer;
		try {
			answer = (Answer)in.readObject();


			//add answer to current question
			if(qm.current_question.id == answer.question_id){
				//if this response is for the current question, increment the appropriate index
				qm.current_question.incrementAnswer(answer.getChoice());
				qm.model.answerSubmitted(answer.eid, answer.getChoice());
			}
		} catch (EOFException e){
			
			System.out.println("Got EOF exception in ResponseMonitor");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
