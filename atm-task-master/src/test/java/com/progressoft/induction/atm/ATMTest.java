package com.progressoft.induction.atm;


import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
//import javax.transaction.Transactional;
import org.junit.jupiter.api.Disabled;

class ATMTest {

    private ATM atm;

    @BeforeEach
    void setUp() throws SQLException {
        //TODO: initialize the atm here
        Connection con = MyConnection.getConnection();
        Statement st =con.createStatement();
        String sql="UPDATE `banking_system`.`initial_balances` SET `balance` = '1000'";
        st.execute(sql);
        atm = new ATMClass();
    }

    @Test
    void givenAccountNumberThatDoesNotExist_whenWithdraw_thenShouldThrowException() {
        Assertions.assertThrows(AccountNotFoundException.class,
                () -> atm.withdraw("14141414141", new BigDecimal("120.0")));
    }

    
    @Test
    void givenValidAccountNumber_whenWithdrawAmountLargerThanTheAccountBalance_thenShouldThrowException() {
        Assertions.assertThrows(InsufficientFundsException.class,
                () -> atm.withdraw("123456789", new BigDecimal("20000.0")));
    }

   // @Disabled
    @Test
    void whenWithdrawAmountLargerThanWhatInMachine_thenShouldThrowException() {
        atm.withdraw("123456789", new BigDecimal("1000.0"));
        atm.withdraw("111111111", new BigDecimal("1000.0"));

        Assertions.assertThrows(NotEnoughMoneyInATMException.class,
                () -> atm.withdraw("444444444", new BigDecimal("500.0")));
    }

   
    @Test
    void whenWithdraw_thenSumOfReceivedBanknotesShouldEqualRequestedAmount() {
        BigDecimal requestedAmount = new BigDecimal(700);
        List<Banknote> receivedBanknotes = atm.withdraw("111111111", requestedAmount);

        BigDecimal sumOfAllBanknotes = receivedBanknotes.stream().map(Banknote::getValue).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        Assertions.assertEquals(sumOfAllBanknotes.compareTo(requestedAmount), 0);
    }

  // @Disabled
    @Test
    void givenAllFundsInAccountAreWithdrwan_whenWithdraw_shouldThrowException() {
        atm.withdraw("222222222", new BigDecimal("500"));
        atm.withdraw("222222222", new BigDecimal("500"));

        Assertions.assertThrows(InsufficientFundsException.class,
                () -> atm.withdraw("222222222", new BigDecimal("500")));
    }
}
