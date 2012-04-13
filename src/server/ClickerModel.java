package server;

import shared.Answer;

public class ClickerModel {
	public static void clickerConnected(ClientObserver co){
		System.out.println("Got connection from " + co.eid + " at ip: " + co.ip.getHostAddress());
	}
	
	public static void answerSubmitted(Answer answer){
		System.out.println("Got answer "+ answer.getChoiceLetter() +" from " + answer.eid);
	}
}
