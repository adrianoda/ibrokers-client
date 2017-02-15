package com.kyoku.ibrokers.client.wrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.TickType;
import com.kyoku.ibrokers.model.PriceData;

/**
 * Convenient wrapper to handle TWS responses 
 * @author kyoku
 *
 */
public class IBEWrapper extends AbstractIBEWrapper {

	private final static Logger logger = LoggerFactory.getLogger(IBEWrapper.class);

	private EClientSocket clientSocket;
	private List<ContractDetails> contractDetailsResponse;
	private PriceData priceData;
	private boolean working = true;
	private Object lock = new Object();

	/**
	 * Wait for current request completion
	 */
	public void waitRequestCompletion() {
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

	/**
	 * Setup client socket to this wrapper
	 * @param clientSocket
	 */
	public void setupEClientSocket(EClientSocket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Setup common list to share contract details data
	 * @return
	 */
	public List<ContractDetails> setupContractDetailsResponse() {
		contractDetailsResponse = new ArrayList<ContractDetails>();
		working = true;
		return contractDetailsResponse;
	}

	/**
	 * Setup common model to share market request data
	 * @param contract
	 * @return
	 */
	public PriceData setupReqMktData(Contract contract) {
		working = true;
		priceData = new PriceData(contract);
		return priceData;
	}

	///////////////////////////////////////////////////////////////////////////////
	// EWrapper methods

	@Override
	public void connectAck() {
		if (clientSocket == null) {
			throw new IllegalArgumentException("clientSocket cannot be null");
		}
		if (clientSocket.isAsyncEConnect()) {
			logger.debug("Acknowledging connection");
			clientSocket.startAPI();
		}
	}

	// ERROR HANDLING

	public void error(Exception e) {
		completeRequest();
		logger.error("A generic error occurred on request processing", e);
		// throw new RuntimeException(e);
	}

	public void error(String str) {
		completeRequest();
		logger.error("An error occurred on request processing, errorMsg:[{}]", str);
		// throw new UnsupportedOperationException();
	}

	public void error(int id, int errorCode, String errorMsg) {
		completeRequest();
		logger.error("An error occurred on request processing, ID:[{}], errorCode:[{}], errorMsg:[{}]", new Object[] { id, errorCode, errorMsg });
	}

	// HANDLED REQUESTS

	// CONTRACTS
	public void contractDetails(int reqId, ContractDetails contractDetails) {
		if (contractDetails == null) {
			throw new IllegalArgumentException("contractDetails cannot be null, invoke setupContractDetails before");
		}
		contractDetailsResponse.add(contractDetails);
	}

	public void contractDetailsEnd(int reqId) {
		logger.debug("contractDetailsEnd");
		completeRequest();
	}

	// MARKET REQUESTS
	@Override
	public void tickGeneric(int tickerId, int tickType, double value) {
		logger.debug("tickGeneric: {} {}", TickType.get(tickType), value);
	}

	@Override
	public void tickString(int tickerId, int tickType, String value) {
		logger.debug("tickString: {} {}", TickType.get(tickType), value);
	}

	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		logger.debug("tickPrice: field {} - price {} - canAutoExecute {}", new Object[] { TickType.get(field), price, canAutoExecute });
		TickType tickType = TickType.get(field);
		if (tickType == TickType.BID) {
			priceData.setBid(price);
		}
	}

	@Override
	public void tickSize(int tickerId, int field, int size) {
		logger.debug("tickSize: field {} - size {}", TickType.get(field), size);
	}

	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega,
			double theta, double undPrice) {
		logger.debug("tickOptionComputation: field {} - impliedVol {} - delta {} - optPrice {} - pvDividend {} - gamma {} - vega {} - theta {} - undPrice {}",
			new Object[] { TickType.get(field), impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice });
	}

	@Override
	public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
		logger.debug("tickEFP: {} {}", TickType.get(tickType), basisPoints);
	}

	@Override
	public void tickSnapshotEnd(int reqId) {
		logger.debug("tickSnapshotEnd");
		completeRequest();
	}

	///////////////////////////////////////////////////////////////////////////////
	// Commons and private

	@Override
	public void managedAccounts(String accountsList) {
		logger.debug("accountList: ", accountsList);
		completeRequest();
	}

	@Override
	public void nextValidId(int orderId) {
		logger.debug("nextValidId: ", orderId);
		completeRequest();
	}

	public void currentTime(long time) {
		logger.debug("Current time: {}", new Date(time));
	}

	/**
	 * Finalize current request
	 */
	private void completeRequest() {
		synchronized (lock) {
			working = false;
			lock.notifyAll();
		}
	}

}
