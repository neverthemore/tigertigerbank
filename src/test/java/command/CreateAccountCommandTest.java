package command;

import domain.BankAccount;
import facade.AccountFacade;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;   // <--- этот импорт нужен

class CreateAccountCommandTest {

    @Test
    void execute_ShouldCallFacadeWithCorrectParameters() {
        AccountFacade facade = mock(AccountFacade.class);
        String input = "MyAccount\n500\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankAccount expectedAccount = new BankAccount("id123", "MyAccount", 500);
        when(facade.createAccount("MyAccount", 500)).thenReturn(expectedAccount);

        CreateAccountCommand command = new CreateAccountCommand(facade, scanner);

        command.execute();

        verify(facade, times(1)).createAccount("MyAccount", 500);
    }
}