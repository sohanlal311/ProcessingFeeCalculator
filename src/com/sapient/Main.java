package com.sapient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.sapient.data.ClientDateKey;
import com.sapient.data.Transaction;
import com.sapient.data.TransactionType;
import com.sapient.exceptions.ParseException;
import com.sapient.exceptions.ReportGenerationException;

public class Main {

	public static void main(String[] args) {
		Map<ClientDateKey, Map<String, Transaction>> map = parseInputFile("input2.txt");
		Map<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> result = generateResult(map);
		generateReport(result, "result.txt");
	}

	private static void generateReport(Map<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> result,
			String fileName) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File(fileName));
			for (Entry<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> e1 : result.entrySet()) {
				String key1 = e1.getKey();
				Map<TransactionType, Map<Integer, Map<Boolean, Double>>> value1 = e1.getValue();
				for (Entry<TransactionType, Map<Integer, Map<Boolean, Double>>> e2 : value1.entrySet()) {
					TransactionType key2 = e2.getKey();
					Map<Integer, Map<Boolean, Double>> value2 = e2.getValue();
					for (Entry<Integer, Map<Boolean, Double>> e3 : value2.entrySet()) {
						Integer key3 = e3.getKey();
						Map<Boolean, Double> value3 = e3.getValue();
						for (Entry<Boolean, Double> e4 : value3.entrySet()) {
							Boolean key4 = e4.getKey();
							Double value4 = e4.getValue();
							StringBuilder builder = new StringBuilder();
							builder.append(key1).append(",").append(key2).append(",").append(key3).append(",")
									.append(key4).append(",").append(value4);
							fileWriter.write(builder.toString() + "\n");
						}
					}
				}
			}
		} catch (IOException e) {
			throw new ReportGenerationException("Report generation failed.", e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					throw new ReportGenerationException("Report generation failed.", e);
				}
			}
		}
	}

	private static Map<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> generateResult(
			Map<ClientDateKey, Map<String, Transaction>> map) {
		Map<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> result = new HashMap<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>>();
		ClientDateKey clientDateKey;
		Map<String, Transaction> transDetails;
		int sumBuy = 0;
		int sumSell = 0;
		int sumDeposit = 0;
		int sumWithdraw = 0;
		if (map != null && map.size() > 0) {
			for (Entry<ClientDateKey, Map<String, Transaction>> e : map.entrySet()) {
				clientDateKey = e.getKey();
				transDetails = e.getValue();
				for (Transaction trans : transDetails.values()) {
					if (trans.isBuy()) {
						sumBuy = 50;
						if (trans.isBuyPriority()) {
							sumBuy = 500;
						} else if (trans.isIntraDay()) {
							sumBuy = 10;
						}
						putValueInMap(result, clientDateKey, TransactionType.BUY, trans.isBuyPriority(), sumBuy);
					}

					if (trans.isSell()) {
						sumSell = 50;
						if (trans.isSellPriority()) {
							sumSell = 500;
						} else if (trans.isIntraDay()) {
							sumSell = 10;
						}
						putValueInMap(result, clientDateKey, TransactionType.SELL, trans.isSellPriority(), sumSell);
					}

					if (trans.isDeposit()) {
						sumDeposit = 50;
						if (trans.isDepositPriority()) {
							sumDeposit = 500;
						}
						putValueInMap(result, clientDateKey, TransactionType.DEPOSIT, trans.isDepositPriority(),
								sumDeposit);
					}

					if (trans.isWithdraw()) {
						sumWithdraw = 50;
						if (trans.isWithdrawPriority()) {
							sumWithdraw = 500;
						}
						putValueInMap(result, clientDateKey, TransactionType.WITHDRAW, trans.isSellPriority(),
								sumWithdraw);
					}

				}
			}
		}
		return result;
	}

	private static void putValueInMap(Map<String, Map<TransactionType, Map<Integer, Map<Boolean, Double>>>> result,
			ClientDateKey clientDateKey, TransactionType transType, boolean hasPriority, int value) {
		Map<TransactionType, Map<Integer, Map<Boolean, Double>>> map2 = result.get(clientDateKey.getClientId());
		if (map2 == null) {
			map2 = new HashMap<TransactionType, Map<Integer, Map<Boolean, Double>>>();
			result.put(clientDateKey.getClientId(), map2);
		}
		Map<Integer, Map<Boolean, Double>> map3 = map2.get(transType);
		if (map3 == null) {
			map3 = new HashMap<Integer, Map<Boolean, Double>>();
			map2.put(transType, map3);
		}
		Map<Boolean, Double> map4 = map3.get(clientDateKey.getDate());
		if (map4 == null) {
			map4 = new HashMap<Boolean, Double>();
			map3.put(clientDateKey.getDate(), map4);
		}

		Double existingValue = map4.get(hasPriority);
		map4.put(hasPriority, existingValue != null ? existingValue + value : value);
	}

	private static Map<ClientDateKey, Map<String, Transaction>> parseInputFile(String fileName) {
		String clientId;
		String secId;
		TransactionType transType;
		int transDate;
		boolean hasPriority;
		ClientDateKey clientDateKey;
		Map<ClientDateKey, Map<String, Transaction>> map = new HashMap<ClientDateKey, Map<String, Transaction>>();

		Scanner sc = null;
		try {
			sc = new Scanner(new File(fileName), "UTF-8");
			sc.nextLine(); // skip header line
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				String[] split = s.split(",");
				if (split.length == 7) { // input is valid if length is 7
					clientId = split[1];
					secId = split[2];
					transType = TransactionType.valueOf(split[3]);
					transDate = Integer.valueOf(split[4].replaceAll("/", ""));
					hasPriority = split[6].length() == 1 && split[6].charAt(0) == 'Y';

					clientDateKey = new ClientDateKey(clientId, transDate);
					Map<String, Transaction> map2 = map.get(clientDateKey);
					if (map2 == null) {
						map2 = new HashMap<String, Transaction>();
						map.put(clientDateKey, map2);
					}
					Transaction transaction = map2.get(secId);
					if (transaction == null) {
						transaction = new Transaction(TransactionType.values().length);
						map2.put(secId, transaction);
					}
					transaction.addTransaction(transType, hasPriority);
				}
			}
			return map;
		} catch (FileNotFoundException e) {
			throw new ParseException("Invalid input.", e);
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}
}
