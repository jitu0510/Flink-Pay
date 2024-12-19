package com.rmgYantra.loginapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rmgYantra.loginapp.model.InboundTransactionForBank1;
import com.rmgYantra.loginapp.model.InboundTransactionForBank2;
import com.rmgYantra.loginapp.repo.InboundTransactionRepoForBank1;
import com.rmgYantra.loginapp.repo.InboundTransactionRepoForBank2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rmgYantra.loginapp.exceptions.TransactionIdAlreadyExistsException;
import com.rmgYantra.loginapp.model.Transaction;
import com.rmgYantra.loginapp.repo.TransactionRepo;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepo transactionRepo;

	@Autowired
	private InboundTransactionRepoForBank1 inboundTransactionRepoForBank1;

	@Autowired
	private InboundTransactionRepoForBank2 inboundTransactionRepoForBank2;
	
	@Transactional
	public List<Transaction> addTransactions(List<Transaction> transactions)  {
		List<Transaction> savedTransactions = new ArrayList<Transaction>();
		for(Transaction transaction : transactions) {
			
			Optional<Transaction> dbTransaction = transactionRepo.findById(transaction.getTransactionId());
			//System.out.println(dbTransaction.isEmpty());
			if(dbTransaction.isEmpty() == false)
				throw new TransactionIdAlreadyExistsException("Transaction_Id: "+transaction.getTransactionId()+" Already Exists");
			Transaction t = transactionRepo.save(transaction);
			savedTransactions.add(t);
		}
		return savedTransactions;
	}
	
	@Transactional
	public Transaction addTransaction(Transaction transaction) {
		Transaction savedTransaction = transactionRepo.save(transaction);
		return savedTransaction;
	}

	public ResponseEntity<?> fetchTransactionDetailsFromFileAndSaveInDb(MultipartFile xmlFile) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // Handle namespaces in XML
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xmlFile.getInputStream());

		// Initialize XPath
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();

		// Register namespace context for XPath
		XPathExpression senderFullNameExpr = xpath.compile("//*[local-name()='Sender']/*[local-name()='FullName']/*[local-name()='X1']/text()");
		XPathExpression receiverFullNameExpr = xpath.compile("//*[local-name()='Receiver']/*[local-name()='FullName']/*[local-name()='X1']/text()");
		XPathExpression senderBICFIExpression = xpath.compile("//*[local-name()='Fr']/*[local-name()='FIId']/*[local-name()='FinInstnId']/*[local-name()='BICFI']/text()");
		XPathExpression receiverBICFIExpression = xpath.compile("//*[local-name()='To']/*[local-name()='FIId']/*[local-name()='FinInstnId']/*[local-name()='BICFI']/text()");
		XPathExpression referenceIdExpression = xpath.compile("//*[local-name()='BizMsgIdr']/text()");
		XPathExpression timeStampExpression = xpath.compile("//*[local-name()='CreDtTm']/text()");
		XPathExpression debtorAccountNumberExpression = xpath.compile("//*[local-name()='DbtrAcct']/*[local-name()='Id']/*[local-name()='Othr']/*[local-name()='Id']/text()");
		XPathExpression creditorAccountNumberExpression = xpath.compile("//*[local-name()='CdtrAcct']/*[local-name()='Id']/*[local-name()='Othr']/*[local-name()='Id']/text()");
		XPathExpression remittanceInformationExpression = xpath.compile("//*[local-name()='RmtInf']/*[local-name()='Ustrd']/text()");
		XPathExpression paymentDateExpression = xpath.compile("//*[local-name()='IntrBkSttlmDt']/text()");
		XPathExpression amountExpression = xpath.compile("//*[local-name()='IntrBkSttlmAmt']/text()");
		XPathExpression settlementDateExpression = xpath.compile("//*[local-name()='IntrBkSttlmDt']/text()");
		XPathExpression ccyExpression = xpath.compile("//*[local-name()='IntrBkSttlmAmt']/@Ccy");

		// Fetch values
		String senderFullName = (String) senderFullNameExpr.evaluate(document, XPathConstants.STRING);
		String receiverFullName = (String) receiverFullNameExpr.evaluate(document, XPathConstants.STRING);
		String senderBICFI = (String) senderBICFIExpression.evaluate(document, XPathConstants.STRING);
		String receiverBICFI = (String) receiverBICFIExpression.evaluate(document, XPathConstants.STRING);
		String referenceId = (String) referenceIdExpression.evaluate(document, XPathConstants.STRING);
		String timeStamp = (String) timeStampExpression.evaluate(document, XPathConstants.STRING);
		String debtorAccountNumber = (String) debtorAccountNumberExpression.evaluate(document, XPathConstants.STRING);
		String creditorAccountNumber = (String) creditorAccountNumberExpression.evaluate(document, XPathConstants.STRING);
		String remittanceInformation = (String) remittanceInformationExpression.evaluate(document, XPathConstants.STRING);
		String amount = (String) amountExpression.evaluate(document, XPathConstants.STRING);
		String settlementDate = (String) settlementDateExpression.evaluate(document, XPathConstants.STRING);
		String ccyValue = (String) ccyExpression.evaluate(document, XPathConstants.STRING);

		if(creditorAccountNumber.startsWith("1051")) {
			InboundTransactionForBank1 transaction = new InboundTransactionForBank1();
			transaction.setReferenceNumber(referenceId);
			transaction.setAmount(amount);
			transaction.setRemittanceInformation(remittanceInformation);
			transaction.setDebtorAccountName(senderFullName);
			transaction.setDebtorAccountNumber(debtorAccountNumber);
			transaction.setCreditorAccountName(receiverFullName);
			transaction.setCreditorAccountNumber(creditorAccountNumber);
			transaction.setDebtorAgentBICCode(senderBICFI);
			transaction.setCreditorAgentBICCode(receiverBICFI);
			transaction.setSettlementDate(settlementDate);
			transaction.setTimeStamp(timeStamp);
			transaction.setCurrency(ccyValue);

			InboundTransactionForBank1 savedTransaction = inboundTransactionRepoForBank1.save(transaction);
			return new ResponseEntity<>(savedTransaction, HttpStatus.OK);
		}else{
			InboundTransactionForBank2 transaction = new InboundTransactionForBank2();
			transaction.setReferenceNumber(referenceId);
			transaction.setAmount(amount);
			transaction.setRemittanceInformation(remittanceInformation);
			transaction.setDebtorAccountName(senderFullName);
			transaction.setDebtorAccountNumber(debtorAccountNumber);
			transaction.setCreditorAccountName(receiverFullName);
			transaction.setCreditorAccountNumber(creditorAccountNumber);
			transaction.setDebtorAgentBICCode(senderBICFI);
			transaction.setCreditorAgentBICCode(receiverBICFI);
			transaction.setSettlementDate(settlementDate);
			transaction.setTimeStamp(timeStamp);
			transaction.setCurrency(ccyValue);

			InboundTransactionForBank2 savedTransaction = inboundTransactionRepoForBank2.save(transaction);
			return new ResponseEntity<>(savedTransaction, HttpStatus.OK);

		}
	}

	public ResponseEntity<?> fetchMultipleTransactionDetailsFromFileAndSaveInDb(MultipartFile xmlFile) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // Handle namespaces in XML
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xmlFile.getInputStream());

		// Initialize XPath
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();

		// Get all Transaction elements
		NodeList transactionNodes = (NodeList) xpath.evaluate("//*[local-name()='Transaction']", document, XPathConstants.NODESET);
		// Iterate through each Transaction node
		//List<InboundTransactionForBank1> inboundTransactionListForBank1 = new ArrayList<>();
		//List<InboundTransactionForBank2> inboundTransactionListForBank2 = new ArrayList<>();
		List<Object> inboundTransactionList = new ArrayList<>();

		for (int i = 0; i < transactionNodes.getLength(); i++) {
			Element transactionElement = (Element) transactionNodes.item(i);

			// Register namespace context for XPath
			String senderFullName = (String) xpath.evaluate(".//*[local-name()='Sender']/*[local-name()='FullName']/*[local-name()='X1']/text()", transactionElement, XPathConstants.STRING);
			String receiverFullName = (String) xpath.evaluate(".//*[local-name()='Receiver']/*[local-name()='FullName']/*[local-name()='X1']/text()", transactionElement, XPathConstants.STRING);
			String senderBICFI = (String) xpath.evaluate(".//*[local-name()='Fr']/*[local-name()='FIId']/*[local-name()='FinInstnId']/*[local-name()='BICFI']/text()", transactionElement, XPathConstants.STRING);
			String receiverBICFI = (String) xpath.evaluate(".//*[local-name()='To']/*[local-name()='FIId']/*[local-name()='FinInstnId']/*[local-name()='BICFI']/text()", transactionElement, XPathConstants.STRING);
			String referenceId = (String) xpath.evaluate(".//*[local-name()='BizMsgIdr']/text()", transactionElement, XPathConstants.STRING);
			String timeStamp = (String) xpath.evaluate(".//*[local-name()='CreDtTm']/text()", transactionElement, XPathConstants.STRING);
			String debtorAccountNumber = (String) xpath.evaluate(".//*[local-name()='DbtrAcct']/*[local-name()='Id']/*[local-name()='Othr']/*[local-name()='Id']/text()", transactionElement, XPathConstants.STRING);
			String creditorAccountNumber = (String) xpath.evaluate(".//*[local-name()='CdtrAcct']/*[local-name()='Id']/*[local-name()='Othr']/*[local-name()='Id']/text()", transactionElement, XPathConstants.STRING);
			String remittanceInformation = (String) xpath.evaluate(".//*[local-name()='RmtInf']/*[local-name()='Ustrd']/text()", transactionElement, XPathConstants.STRING);
			String amount = (String) xpath.evaluate(".//*[local-name()='IntrBkSttlmAmt']/text()", transactionElement, XPathConstants.STRING);
			String settlementDate = (String) xpath.evaluate(".//*[local-name()='IntrBkSttlmDt']/text()", transactionElement, XPathConstants.STRING);
			String ccyValue = (String) xpath.evaluate(".//*[local-name()='IntrBkSttlmAmt']/@Ccy", transactionElement, XPathConstants.STRING);

			if (creditorAccountNumber.startsWith("1051")) {
				// Create and populate InboundTransaction
				InboundTransactionForBank1 transaction = new InboundTransactionForBank1();
				transaction.setReferenceNumber(referenceId);
				transaction.setAmount(amount);
				transaction.setRemittanceInformation(remittanceInformation);
				transaction.setDebtorAccountName(senderFullName);
				transaction.setDebtorAccountNumber(debtorAccountNumber);
				transaction.setCreditorAccountName(receiverFullName);
				transaction.setCreditorAccountNumber(creditorAccountNumber);
				transaction.setDebtorAgentBICCode(senderBICFI);
				transaction.setCreditorAgentBICCode(receiverBICFI);
				transaction.setSettlementDate(settlementDate);
				transaction.setTimeStamp(timeStamp);
				transaction.setCurrency(ccyValue);

				// Save the transaction and add it to the list
				InboundTransactionForBank1 savedTransaction = inboundTransactionRepoForBank1.save(transaction);
				inboundTransactionList.add(savedTransaction);
			} else {
				// Create and populate InboundTransaction
				InboundTransactionForBank2 transaction = new InboundTransactionForBank2();
				transaction.setReferenceNumber(referenceId);
				transaction.setAmount(amount);
				transaction.setRemittanceInformation(remittanceInformation);
				transaction.setDebtorAccountName(senderFullName);
				transaction.setDebtorAccountNumber(debtorAccountNumber);
				transaction.setCreditorAccountName(receiverFullName);
				transaction.setCreditorAccountNumber(creditorAccountNumber);
				transaction.setDebtorAgentBICCode(senderBICFI);
				transaction.setCreditorAgentBICCode(receiverBICFI);
				transaction.setSettlementDate(settlementDate);
				transaction.setTimeStamp(timeStamp);
				transaction.setCurrency(ccyValue);

				// Save the transaction and add it to the list
				InboundTransactionForBank2 savedTransaction = inboundTransactionRepoForBank2.save(transaction);
				inboundTransactionList.add(savedTransaction);
			}
		}
			return new ResponseEntity<>(inboundTransactionList, HttpStatus.OK);
	}

}
