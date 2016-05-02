package pt.tecnico.mydrive.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Set;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.RootUser;
import pt.tecnico.mydrive.service.ListDirectoryService;

public class List extends MyDriveCommand {

	public List(Shell sh) {
		super(sh, "ls", "lists the current directory");
	}

	@Override
	void execute(String[] args) {
		if (args.length < 1)
		    throw new RuntimeException("USAGE: "+name()+" <path> <text>");
		else{
			String s = null;

		    try{
		        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		        s = bufferRead.readLine();

		        System.out.println(s);
		    }
		    catch(IOException e)
		    {
		    	System.out.println("deu merda");
		        e.printStackTrace();
		    }
		    ListDirectoryService ls = new ListDirectoryService( Long.parseLong(s, 10)); //FIXME token is third parameter
		    ls.execute();
		    System.out.println(ls.result());
		}
	}

}
