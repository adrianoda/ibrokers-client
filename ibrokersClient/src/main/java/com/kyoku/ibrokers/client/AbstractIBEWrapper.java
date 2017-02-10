package com.kyoku.ibrokers.client;

import java.util.Set;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.SoftDollarTier;

public abstract class AbstractIBEWrapper implements EWrapper {

	@Override
	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void openOrderEnd() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAccountValue(String key, String value, String currency, String accountName) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAccountTime(String timeStamp) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void accountDownloadEnd(String accountName) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void bondContractDetails(int reqId, ContractDetails contractDetails) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void contractDetailsEnd(int reqId) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void execDetails(int reqId, Contract contract, Execution execution) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void execDetailsEnd(int reqId) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void receiveFA(int faDataType, String xml) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void scannerParameters(String xml) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void scannerDataEnd(int reqId) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void fundamentalData(int reqId, String data) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void marketDataType(int reqId, int marketDataType) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void commissionReport(CommissionReport commissionReport) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void positionEnd() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void accountSummary(int reqId, String account, String tag, String value, String currency) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void accountSummaryEnd(int reqId) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void verifyMessageAPI(String apiData) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void verifyCompleted(boolean isSuccessful, String errorText) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void displayGroupList(int reqId, String groups) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void displayGroupUpdated(int reqId, String contractInfo) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void connectionClosed() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void accountUpdateMulti(int arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void accountUpdateMultiEnd(int arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void orderStatus(int arg0, String arg1, double arg2, double arg3, double arg4, int arg5, int arg6, double arg7, int arg8, String arg9) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void position(String arg0, Contract arg1, double arg2, double arg3) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void positionMulti(int arg0, String arg1, String arg2, Contract arg3, double arg4, double arg5) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void positionMultiEnd(int arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void securityDefinitionOptionalParameter(int arg0, String arg1, int arg2, String arg3, String arg4, Set<String> arg5, Set<Double> arg6) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void securityDefinitionOptionalParameterEnd(int arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void softDollarTiers(int arg0, SoftDollarTier[] arg1) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updatePortfolio(Contract arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6, String arg7) {
		throw new UnsupportedOperationException();

	}

}
