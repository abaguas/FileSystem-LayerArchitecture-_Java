package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.exception.InvalidPathException;
import pt.tecnico.mydrive.exception.MyDriveException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;



@RunWith(JMockit.class)
public class EnvironmentLinksTest extends AbstractServiceTest {

    private static String result;
    private static Session s;
    private static Link link1;
    private static Link link2;
    private static Link link3;
    private static Link link4;
    private static Link link5;
    private static Link link6;
    private static Link link7;
    private static Link link8;
    private static Link link9;
    private static Link link10;
    private static Link link11;
    private static Link link12;
    private static Link link13;
    private static Link link14;
    private static Link link15;
    private static Link link16;
    private static Link link17;
    private static Link link18;
    private static Link link19;
    private static Link link20;
    private static Link link21;
    private static String RES_PATH6;
    private static String RES_PATH7;
    private static String RES_PATH8;
    private static String RES_PATH9;
    private static String RES_PATH10;
    private static String RES_PATH11;
    private static String RES_PATH12;
    private static String RES_PATH13;
    private static String RES_PATH18;
    private static String RES_PATH19;
    private static String RES_PATH20;
    private static String RES_PATH21;
    private static String PATH1;
    private static String PATH2;
    private static String PATH3;
    private static String PATH4;
    private static String PATH5;
    private static String PATH6;
    private static String PATH7;
    private static String PATH8;
    private static String PATH9;
    private static String PATH10;
    private static String PATH11;
    private static String PATH12;
    private static String PATH13;
    private static String PATH14;
    private static String PATH15;
    private static String PATH16;
    private static String PATH17;
    private static String PATH18;
    private static String PATH19;
    private static String PATH20;
    private static String PATH21;

    private EnvironmentLinksService service;

    protected void populate() {
        
        MyDrive md = MyDrive.getInstance();
        User USER = md.getRootUser();
        Directory DIR = USER.getMainDirectory();
        PATH1 = "/a/$NAOEXISTO/b/c"; // falha -> InvalidPathName()
        PATH2 = "/$NAOEXISTO/b/c"; // falha -> InvalidPathName()
        PATH3 = "/a/$NAOEXISTO"; // falha -> InvalidPathName()
        PATH4 = "/a/$VAR2";   // falha -> InvalidPathName()
        PATH5 = "/a/$VAR2/b"; // falha -> InvalidPathName()
        PATH6 = "$VAR2";      //sucesso -> /
        PATH7 = "$DIR/a/b";   //sucesso -> souUmDirectorio/a/b (relativo)
        PATH8 = "/a/$DIR/b";  //sucesso -> /a/souUmDirectorio/b
        PATH9 = "/a/$DIR";    //sucesso -> /a/souUmDirectorio
        PATH10 = "$DIR2/a/b";   //sucesso -> /souUmDirectorio/a/b (absoluto)
        PATH11 = "/$souUmDirectorioNormal/a/b";   //sucesso -> igual
        PATH12 = "/a/$souUmDirectorioNormal/b";   //sucesso -> igual
        PATH13 = "/a/b/$souUmDirectorioNormal";   //sucesso -> igual
        PATH14 = "$PLAINFILE/a/b";   
        PATH15 = "/a/$PLAINFILE/b";  
        PATH16 = "/a/$PLAINFILE";    
        PATH17 = "$PLAINFILE2/a/b";   
        PATH18 = "/$VAR1/x/y";   //sucesso -> /a/b/x/y
        PATH19 = "/z/$VAR1/x/y";   //sucesso -> /z/a/b/x/y
        PATH20 = "/w/x/y/$VAR1";   //sucesso -> /w/x/y/a/b
        PATH21 = "$VAR3" ; //sucesso -> /a/b/c

        RES_PATH6 = "/";
        RES_PATH7 = "souUmDirectorio/a/b"; 
        RES_PATH8 = "/a/souUmDirectorio/b";
        RES_PATH9 = "/a/souUmDirectorio";
        RES_PATH10 = "/souUmDirectorio/a/b"; 
        RES_PATH11 = "/$souUmDirectorioNormal/a/b";   
        RES_PATH12 = "/a/$souUmDirectorioNormal/b";   
        RES_PATH13 = "/a/b/$souUmDirectorioNormal";   
        
        RES_PATH18 = "/a/b/x/y";
        RES_PATH19 = "/z/a/b/x/y";
        RES_PATH20 = "/w/x/y/a/b";
        RES_PATH21 = "/a/b/c";


        link1 = new Link("link1", USER, PATH1, DIR);
        link2 = new Link("link2", USER, PATH2, DIR);
        link3 = new Link("link3", USER, PATH3, DIR);
        link4 = new Link("link4", USER, PATH4, DIR);
        link5 = new Link("link5", USER, PATH5, DIR);
        link6 = new Link("link6", USER, PATH6, DIR);
        link7 = new Link("link7", USER, PATH7, DIR);
        link8 = new Link("link8", USER, PATH8, DIR);
        link9 = new Link("link9", USER, PATH9, DIR);
        link10 = new Link("link10", USER, PATH10, DIR);
        link11 = new Link("link11", USER, PATH11, DIR);
        link12 = new Link("link12", USER, PATH12, DIR);
        link13 = new Link("link13", USER, PATH13, DIR);
        link14 = new Link("link14", USER, PATH14, DIR);
        link15 = new Link("link15", USER, PATH15, DIR);
        link16 = new Link("link16", USER, PATH16, DIR);
        link17 = new Link("link17", USER, PATH17, DIR);
        link18 = new Link("link18", USER, PATH18, DIR);
        link19 = new Link("link19", USER, PATH19, DIR);
        link20 = new Link("link20", USER, PATH20, DIR);
        link21 = new Link("link21", USER, PATH21, DIR);

        s = new Session("root", "***", SessionManager.getInstance());
        s.addEnv("$VAR1", "a/b");
        s.addEnv("$VAR2", "/");
        s.addEnv("$VAR3", "/a/b/c");
        s.addEnv("$DIR", "souUmDirectorio");
        s.addEnv("$DIR2", "/souUmDirectorio");
        s.addEnv("$PLAINFILE", "souUmPlainFile");
        s.addEnv("$PLAINFILE2", "/souUmPlainFile");
    }

    @Test
    public void success6() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH6; }
       };

        service = new EnvironmentLinksService(s.getToken(), link6);
        service.execute();
        assertEquals(service.result(), RES_PATH6);
        assertTrue(service.result().contains(s.getEnv("$VAR2"))); 
    }

    @Test
    public void success7() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH7; }
       };

        service = new EnvironmentLinksService(s.getToken(), link7);
        service.execute();
        assertEquals(service.result(), RES_PATH7);
        assertTrue(service.result().contains(s.getEnv("$DIR"))); 
    }

    @Test
    public void success8() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH8; }
       };

        service = new EnvironmentLinksService(s.getToken(), link8);
        service.execute();
        assertEquals(service.result(), RES_PATH8);
        assertTrue(service.result().contains(s.getEnv("$DIR"))); 
    }

    @Test
    public void success9() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH9; }
       };

        service = new EnvironmentLinksService(s.getToken(), link9);
        service.execute();
        assertEquals(service.result(), RES_PATH9);
        assertTrue(service.result().contains(s.getEnv("$DIR"))); 
    }

    @Test
    public void success10() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH10; }
       };

        service = new EnvironmentLinksService(s.getToken(), link10);
        service.execute();
        assertEquals(service.result(), RES_PATH10);
        assertTrue(service.result().contains(s.getEnv("$DIR2"))); 
    }

    @Test
    public void success11() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH11; }
       };

        service = new EnvironmentLinksService(s.getToken(), link10);
        service.execute();
        assertEquals(service.result(), RES_PATH11);
        assertTrue(service.result().contains(s.getEnv("$VAR2"))); 
    }

    @Test
    public void success12() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH12; }
       };

        service = new EnvironmentLinksService(s.getToken(), link12);
        service.execute();
        assertEquals(service.result(), RES_PATH12);
        assertTrue(service.result().contains(s.getEnv("$VAR2"))); 
    }

    @Test
    public void success13() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH13; }
       };

        service = new EnvironmentLinksService(s.getToken(), link13);
        service.execute();
        assertEquals(service.result(), RES_PATH13);
        assertTrue(service.result().contains(s.getEnv("$VAR2"))); 
    }

    @Test
    public void success18() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH18; }
       };

        service = new EnvironmentLinksService(s.getToken(), link18);
        service.execute();
        assertEquals(service.result(), RES_PATH18);
        assertTrue(service.result().contains(s.getEnv("$VAR1"))); 
    }

    @Test
    public void success19() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH19; }
       };

        service = new EnvironmentLinksService(s.getToken(), link19);
        service.execute();
        assertEquals(service.result(), RES_PATH19);
        assertTrue(service.result().contains(s.getEnv("$VAR1"))); 
    }

    @Test
    public void success20() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH20; }
       };

        service = new EnvironmentLinksService(s.getToken(), link20);
        service.execute();
        assertEquals(service.result(), RES_PATH20);
        assertTrue(service.result().contains(s.getEnv("$VAR1"))); 
    }

    @Test
    public void success21() {
        new MockUp<EnvironmentLinksService>() {
          @Mock
          String result() { return RES_PATH21; }
       };

        service = new EnvironmentLinksService(s.getToken(), link21);
        service.execute();
        assertEquals(service.result(), RES_PATH21);
        assertTrue(service.result().contains(s.getEnv("$VAR3"))); 
    }

    @Test(expected = InvalidPathException.class)
    public void fail1() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
    	   @Mock
	       void dispatch() throws MyDriveException {
	           throw new InvalidPathException(PATH1); }
	       };

        new EnvironmentLinksService(s.getToken(), link1).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail2() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH2); }
           };

        new EnvironmentLinksService(s.getToken(), link2).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail3() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH3); }
           };

        new EnvironmentLinksService(s.getToken(), link3).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail4() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH4); }
           };

        new EnvironmentLinksService(s.getToken(), link4).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail5() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH5); }
           };

        new EnvironmentLinksService(s.getToken(), link5).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail14() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH14); }
           };

        new EnvironmentLinksService(s.getToken(), link14).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail15() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH15); }
           };

        new EnvironmentLinksService(s.getToken(), link15).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail16() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH16); }
           };

        new EnvironmentLinksService(s.getToken(), link16).execute();
    }

    @Test(expected = InvalidPathException.class)
    public void fail17() throws InvalidPathException {

        new MockUp<EnvironmentLinksService>() {
           @Mock
           void dispatch() throws MyDriveException {
               throw new InvalidPathException(PATH17); }
           };

        new EnvironmentLinksService(s.getToken(), link17).execute();
    }
}