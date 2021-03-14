
package com.progressoft.induction.atm;

import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ATMClass implements ATM, BankingSystem  {
   public BigDecimal ATMNoney=new BigDecimal("2400");
   
    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
      List<Banknote> banknotes =new ArrayList<>();
        //check if the user have enough amount in /her balance
        //if amount in the banksystem  less than amount
        if (getAccountBalance(accountNumber).compareTo(amount)<0){
        throw new InsufficientFundsException();
        }
        else {
        //debit the amount from banksystem db:
        debitAccount(accountNumber,amount);
        }
        //after that, make withdraw
      
        if (amount.compareTo(ATMNoney)>0){
        throw new NotEnoughMoneyInATMException();
        }
        //recount the total money in the machine
        else {
      int count50=0;
      int count20=0;
      int count10=0;
      int count5 = 0;
      BigDecimal remains =new BigDecimal(amount.toString());
      
      if (remains.compareTo(Banknote.FIFTY_JOD.getValue())>=0)
      {
      count50=(remains.divide(Banknote.FIFTY_JOD.getValue())).intValue();
         remains = remains.subtract((Banknote.FIFTY_JOD.getValue()).multiply(BigDecimal.valueOf(count50)));
      
      }
      
      if (remains.compareTo(Banknote.TWENTY_JOD.getValue())>=0){
      count20=(remains.divide(Banknote.TWENTY_JOD.getValue())).intValue();
        remains = remains.subtract((Banknote.TWENTY_JOD.getValue()).multiply(BigDecimal.valueOf(count20)));
      
      }
      if (remains.compareTo(Banknote.TEN_JOD.getValue())>=0){
      count10=(remains.divide(Banknote.TEN_JOD.getValue())).intValue();
        remains = remains.subtract((Banknote.TEN_JOD.getValue()).multiply(BigDecimal.valueOf(count10)));
      
      }
      if (remains.compareTo(Banknote.FIVE_JOD.getValue())>=0){
       count5=(remains.divide(Banknote.FIVE_JOD.getValue())).intValue();
        remains = remains.subtract((Banknote.FIVE_JOD.getValue()).multiply(BigDecimal.valueOf(count5)));
      }
      
      //fill the banknotes:
        for (int i=0; i<count50; i++){
        banknotes.add(Banknote.FIFTY_JOD);
        }
        for (int i=0; i<count20; i++){
        banknotes.add(Banknote.TWENTY_JOD);
        }
        for (int i=0; i<count10; i++){
        banknotes.add(Banknote.TEN_JOD);
        }
        for (int i=0; i<count5; i++){
        banknotes.add(Banknote.FIVE_JOD);
        }
        
       ATMNoney= ATMNoney.subtract(amount);
        }
        return banknotes;
    }
    
    
    
    
    
    

    @Override
    public BigDecimal getAccountBalance(String accountNumber) {
        BigDecimal amountInBankDataBase =null;
        try {
            Connection conn =MyConnection.getConnection();
            PreparedStatement pst =conn.prepareStatement("SELECT balance FROM `banking_system`.`initial_balances` where account_number = ?");
            pst.setString(1, accountNumber);
            ResultSet rs = pst.executeQuery();
             if  (rs.next()){
             String value = rs.getBigDecimal(1).toString();
             amountInBankDataBase = new BigDecimal(value);
             return amountInBankDataBase;
             }
             else {
             throw new AccountNotFoundException();
             }
        } catch (SQLException ex) {
            Logger.getLogger(ATMClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return amountInBankDataBase;
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {
        try {
            Connection conn =MyConnection.getConnection();
            CallableStatement cst = conn.prepareCall("{call debit_amount (?,?)}");
            cst.setString(1, accountNumber);
            cst.setBigDecimal(2, new BigDecimal(amount.toString()) );
            
            cst.execute();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ATMClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    
    
    
     /*public static void main(String[] args) throws SQLException {
         int count50=0;
      BigDecimal remains =new BigDecimal("60");
      
      if (remains.compareTo(Banknote.FIFTY_JOD.getValue())>0)
      {
       count50=Integer.valueOf(remains.divide(Banknote.FIFTY_JOD.getValue()).toString());
          
       // remains.subtract(Banknote.FIFTY_JOD.getValue().multiply(BigDecimal.valueOf(count50)));
        System.out.println(count50);
      }
     
    }
      */
    }
    

