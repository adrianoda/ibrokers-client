package com.kyoku.ibrokers.model;

import com.ib.client.Contract;

/**
 * Hold contract data and related price info
 * @author kyoku
 *
 */
public class PriceData implements Comparable<PriceData> {

	private Contract contract;

	private Double bid;

	private Double delta;

	public PriceData(Contract contract) {
		this.contract = contract;
	}

	// WRAPPED METHODS

	public String getSymbol() {
		return contract.symbol();
	}

	public String getSecType() {
		return contract.getSecType();
	}

	public String getCurrency() {
		return contract.currency();
	}

	public String getExchange() {
		return contract.exchange();
	}

	public String getRight() {
		return contract.right().getApiString();
	}

	public String getContractMonth() {
		return contract.lastTradeDateOrContractMonth();
	}

	public Double getStrike() {
		return contract.strike();
	}

	// GETTER SETTER

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}

	// OTHER


	@Override
	public String toString() {
		return "PriceData [getSymbol()="
				+ getSymbol()
				+ ", getSecType()="
				+ getSecType()
				+ ", getCurrency()="
				+ getCurrency()
				+ ", getExchange()="
				+ getExchange()
				+ ", getRight()="
				+ getRight()
				+ ", getContractMonth()="
				+ getContractMonth()
				+ ", getStrike()="
				+ getStrike()
				+ ", getBid()="
				+ getBid()
				+ ", getDelta()="
				+ getDelta()
				+ "]";
	}

	@Override
	public int compareTo(PriceData o) {
		return getStrike().compareTo(o.getStrike());
	}
	
}
