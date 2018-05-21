package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/13
 * @desc
 */
public class TotalAccountEntity implements Serializable {

    private double deposit;
    private double deposit_interest;
    private double wait_deposit_interest;
    private double enjoy;
    private double enjoy_interest;
    private double wait_enjoy_interest;
    private double all_reward_amount;
    private double reward_amount;
    private double wait_reward_amount;
    private double balance;
    private double appoint;
    private double withdraw_amount;

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getDeposit_interest() {
        return deposit_interest;
    }

    public void setDeposit_interest(double deposit_interest) {
        this.deposit_interest = deposit_interest;
    }

    public double getWait_deposit_interest() {
        return wait_deposit_interest;
    }

    public void setWait_deposit_interest(double wait_deposit_interest) {
        this.wait_deposit_interest = wait_deposit_interest;
    }

    public double getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(double enjoy) {
        this.enjoy = enjoy;
    }

    public double getEnjoy_interest() {
        return enjoy_interest;
    }

    public void setEnjoy_interest(double enjoy_interest) {
        this.enjoy_interest = enjoy_interest;
    }

    public double getWait_enjoy_interest() {
        return wait_enjoy_interest;
    }

    public void setWait_enjoy_interest(double wait_enjoy_interest) {
        this.wait_enjoy_interest = wait_enjoy_interest;
    }

    public double getAll_reward_amount() {
        return all_reward_amount;
    }

    public void setAll_reward_amount(double all_reward_amount) {
        this.all_reward_amount = all_reward_amount;
    }

    public double getReward_amount() {
        return reward_amount;
    }

    public void setReward_amount(double reward_amount) {
        this.reward_amount = reward_amount;
    }

    public double getWait_reward_amount() {
        return wait_reward_amount;
    }

    public void setWait_reward_amount(double wait_reward_amount) {
        this.wait_reward_amount = wait_reward_amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAppoint() {
        return appoint;
    }

    public void setAppoint(double appoint) {
        this.appoint = appoint;
    }

    public double getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(double withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

}
