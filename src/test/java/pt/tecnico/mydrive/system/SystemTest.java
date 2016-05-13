package pt.tecnico.mydrive.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.presentation.*;
import pt.tecnico.mydrive.service.AbstractServiceTest;

public class SystemTest extends AbstractServiceTest {

    private MyDriveShell sh;

    protected void populate() {
        sh = new MyDriveShell();
    }

    @Test
    public void success() {
        new Import(sh).execute(new String[] { "info/UsersAndFiles.xml" } );
        new Login(sh).execute(new String[] { "Sofia", "9876543121" } );
        new Key(sh).execute(new String[] { } );
        new Login(sh).execute(new String[] { "Carlos", "123456789" } );
        new Key(sh).execute(new String[] { "Sofia" } );
        new ChangeWorkingDirectory(sh).execute(new String[] { ".." } );
        new List(sh).execute(new String[] { } );
        new ChangeWorkingDirectory(sh).execute(new String[] { ".." } );
        new List(sh).execute(new String[] { "/home/Sofia" } );
        new Write(sh).execute(new String[] { "home/Sofia/WriteMe" , "newContent1" } );
        new Execute(sh).execute(new String[] { "home/Sofia/ExecuteMeWithArgs" , "arg1" } );
        new Execute(sh).execute(new String[] { "home/Sofia/ExecuteMeWithOutArgs"  } );
        new ChangeWorkingDirectory(sh).execute(new String[] { "home/Sofia" } );
        new Write(sh).execute(new String[] { "WriteMe" , "newContent2" } );
        new Execute(sh).execute(new String[] { "ExecuteMeWithArgs" , "arg1" } );
        new Execute(sh).execute(new String[] { "ExecuteMeWithOutArgs"  } );
        new Environment(sh).execute(new String[] { "$USER1", "content1" } );
        new Environment(sh).execute(new String[] { "$USER2", "content2" } );
        new Environment(sh).execute(new String[] { "$USER1" } );
        new Environment(sh).execute(new String[] {  } );
        new Export(sh).execute(new String[] { } );
        new Quit(sh).execute(new String[] { } );
    }
}
