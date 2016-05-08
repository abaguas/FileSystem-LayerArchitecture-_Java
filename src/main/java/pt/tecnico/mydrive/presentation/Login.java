//package pt.tecnico.mydrive.presentation;
//
//import pt.tecnico.mydrive.service.LoginService;
//
//public class Login extends MyDriveCommand {
//
//	public Login(MyDriveShell sh) {
//		super(sh, "login", "login of an user");
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	void execute(String[] args) {
//		if (args.length < 2)
//		    throw new RuntimeException("USAGE: "+name()+" <path> <text>");
//		else{
//		    LoginService ls = new LoginService("root","***");
//		    ls.execute(); //FIXME token is third parameter
//		    getShell().setToken(ls.result());
//		    System.out.println("AQUIII: "+ls.result());
//		}
//	}
//
//}
