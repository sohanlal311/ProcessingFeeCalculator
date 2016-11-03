package com.sapient.data;

import java.util.BitSet;

public class Transaction {

	private final BitSet transTypes;
	private final BitSet priorities;

	public Transaction(int length) {
		this.transTypes = new BitSet(length);
		this.priorities = new BitSet(length);
	}

	public void addTransaction(TransactionType transType, boolean hasPriority) {
		this.transTypes.set(transType.getId());
		if (hasPriority)
			this.priorities.set(transType.getId());
	}

	public boolean isIntraDay() {
		return transTypes.get(TransactionType.BUY.getId()) && transTypes.get(TransactionType.SELL.getId());
	}

	public boolean isBuy() {
		return transTypes.get(TransactionType.BUY.getId());
	}

	public boolean isSell() {
		return transTypes.get(TransactionType.SELL.getId());
	}

	public boolean isDeposit() {
		return transTypes.get(TransactionType.DEPOSIT.getId());
	}

	public boolean isWithdraw() {
		return transTypes.get(TransactionType.WITHDRAW.getId());
	}

	public boolean isBuyPriority() {
		return isBuy() && priorities.get(TransactionType.BUY.getId());
	}

	public boolean isSellPriority() {
		return isSell() && priorities.get(TransactionType.SELL.getId());
	}

	public boolean isDepositPriority() {
		return isDeposit() && priorities.get(TransactionType.DEPOSIT.getId());
	}

	public boolean isWithdrawPriority() {
		return isWithdraw() && priorities.get(TransactionType.WITHDRAW.getId());
	}

}
