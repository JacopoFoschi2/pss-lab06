package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;
    private final static int ID = 0;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        mRossi = new AccountHolder("Matteo", "Rossi", ID);
        bankAccount = new StrictBankAccount(mRossi, 0);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(0, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        var balance = 100.0;
        bankAccount.deposit(ID, balance);
        var estimatedManagementFee = bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE + StrictBankAccount.MANAGEMENT_FEE;
        var estimatedBalance = balance - estimatedManagementFee;
        bankAccount.chargeManagementFees(ID);
        assertEquals(estimatedBalance, bankAccount.getBalance());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        var withdrawal = -100.0;
        assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(ID, withdrawal));
        assertEquals(0, bankAccount.getBalance());
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        var withdrawal = 100.0;
        assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(ID, withdrawal));
        assertEquals(0, bankAccount.getBalance());
    }
}
