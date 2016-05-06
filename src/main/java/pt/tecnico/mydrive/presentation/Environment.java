//package pt.tecnico.mydrive.presentation;
//
//import pt.tecnico.mydrive.service.LoginService;
//
//public class Environment extends MyDriveCommand {
//	
//	public Environment(Shell sh) {
//		super(sh, "env", "create or change environment variable");
//	}
//
//	@Override
//	void execute(String[] args) {
//		if (args.length < 0 || args.length > 2) {
//		    throw new RuntimeException("USAGE: " + name() +" [<name> [<value>]]");
//		}
//		else {
//			long token = getToken();
//			AddVariableService avs = null;
//			if (args == 0) {
//				new AddVariableService(token, null, null);
//				avs.execute();
//			}
//			else if (args == 1){
//				new AddVariableService(token, args[0], null);
//				avs.execute();
//			}
//			else {
//				avs = new AddVariableService(token, args[0], args[1]);
//				avs.execute();
//			}
//		}
//		for (VariableDto v: avs.result()) {
//			System.out.println(avs.getName()+" = "+avs.getValue());
//		}
//	}
//
//}
