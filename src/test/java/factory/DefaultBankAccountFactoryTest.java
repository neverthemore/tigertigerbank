package factory;

import domain.BankAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBankAccountFactoryTest {

    @Test
    void create_ShouldReturnBankAccountWithGeneratedId() {
        BankAccountFactory factory = new DefaultBankAccountFactory();
        BankAccount account = factory.create("Test Account", 1000.0);

        assertNotNull(account.getId());
        assertEquals("Test Account", account.getName());
        assertEquals(1000.0, account.getBalance());
    }
}