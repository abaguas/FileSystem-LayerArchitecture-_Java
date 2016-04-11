/*package pt.tecnico.mydrive.service;

public class LoginTest extends AbstractServiceTest{
	protected void populate(){

		MyDrive md = MyDrive.getInstance();
		User u1= new User("Utilizador1", "1234", "user1");


	}

	@Test
	public void SuccessfulLogin(){
		long token = LoginService("user1", "1234");
		assertNotNull (token);
		 //Sucesso
	}
	
	@Test(expected = InvalidUserOrPasswordException.class)
		public void UnsuccessfulLoginDueToWrongPassword() throws InvalidUserOrPasswordException{
		long token = LoginService("user1", "1235");
 //Erro
		}
	
	@Test(expected = InvalidUserOrPasswordException.class)
		public void UnsuccessfulLoginDueToNonExistentUser() throws InvalidUserOrPasswordException{
		long token = LoginService ("NonExistent", "1243");
//Erro
		}
	
	
	@Test
	public void initiateSessionPeriod(){
	}
	
	@Test
	public void reinitiateSessionPeriod(){
	}

}	
*/
