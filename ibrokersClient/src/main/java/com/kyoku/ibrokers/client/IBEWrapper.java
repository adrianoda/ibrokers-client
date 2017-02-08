package com.kyoku.ibrokers.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;

public class IBEWrapper extends AbstractIBEWrapper {

	private final static Logger logger = LoggerFactory.getLogger(IBEWrapper.class);

	private EClientSocket clientSocket;
	private List<ContractDetails> contractDetailsResponse;
	private boolean working = true;
	private Object lock = new Object();

	public void waitRequestComplention() {
		try {
			while (working) {
				synchronized (lock) {
					lock.wait();
				}
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void setupEClientSocket(EClientSocket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public List<ContractDetails> setupContractDetailsResponse() {
		contractDetailsResponse = new ArrayList<ContractDetails>();
		working = true;
		return contractDetailsResponse;
	}

	///////////////////////////////////////////////////////////////////////////////
	// EWrapper methods

	@Override
	public void connectAck() {
		if (clientSocket == null) {
			throw new IllegalArgumentException("clientSocket cannot be null");
		}
		if (clientSocket.isAsyncEConnect()) {
			logger.info("Acknowledging connection");
			clientSocket.startAPI();
		}
	}

	public void error(Exception e) {
		working = false;
		logger.error("A generic error occurred on request processing", e);
		throw new RuntimeException(e);
	}

	public void error(String str) {
		working = false;
		logger.error("An error occurred on request processing, errorMsg:[{}]", str);
		throw new UnsupportedOperationException();
	}

	public void error(int id, int errorCode, String errorMsg) {
		working = false;
		logger.error("An error occurred on request processing, ID:[{}], errorCode:[{}], errorMsg:[{}]", new Object[] { id, errorCode, errorMsg });
	}

	public void contractDetails(int reqId, ContractDetails contractDetails) {
		if (contractDetails == null) {
			throw new IllegalArgumentException("contractDetails cannot be null, invoke setupContractDetails before");
		}
		contractDetailsResponse.add(contractDetails);
	}

	public void contractDetailsEnd(int reqId) {
		completeRequest();
	}

	@Override
	public void managedAccounts(String accountsList) {
		logger.info("accountList: ", accountsList);
		completeRequest();
	}

	@Override
	public void nextValidId(int orderId) {
		logger.info("nextValidId: ", orderId);
		completeRequest();
	}

	public void currentTime(long time) {
		logger.info("Current time: {}", new Date(time));
	}

	private void completeRequest() {
		synchronized (lock) {
			working = false;
			lock.notifyAll();
		}
	}

}
