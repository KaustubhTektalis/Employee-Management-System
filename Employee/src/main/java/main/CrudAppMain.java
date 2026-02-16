package main;

import controller.MainMenu;
import util.MakeConnection;

public class CrudAppMain {

	public static void main(String[] args) {
		new MainMenu();
		MainMenu.Menu();
		
		
		MakeConnection.closePool();
	}
}
