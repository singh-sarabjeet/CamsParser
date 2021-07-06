package com.camsparser.service;

import com.camsparser.dto.FolioDTO;
import com.camsparser.dto.FundDTO;
import com.camsparser.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ParserService {

    public List<FundDTO> parseFile(String filePath) throws IOException {
        FileReader fr = new FileReader(filePath);
        int r = 0;
        StringBuilder sb = new StringBuilder();
        while ((r = fr.read()) != -1) {
            sb.append((char) r);  //appends to character to StringBuilder object
        }
        //log.info(sb.toString());

        String rawCamsData = sb.toString();

        String[] splitFolios = rawCamsData.split("Folio No:");
        // System.out.println(Arrays.toString(splitFolios));
        List<FundDTO> fundList = new ArrayList<>();
        for (int i = 1; i < splitFolios.length; i++) {
            FundDTO fund = parseFolio(splitFolios[i]);
            fundList.add(fund);
        }
        return fundList;
    }

    private FundDTO parseFolio(String folioText) {
        String[] splitLines = folioText.split("\n\n");

        FundDTO fund = new FundDTO();
        String[] fundLineSplit = splitLines[1].split("Registrar :");
        fund.setName(fundLineSplit[0].trim());
        if (splitLines[2].trim().startsWith("Opening")) {
            fund.setRegistrar(fundLineSplit[1].trim());
        } else {
            fund.setRegistrar(splitLines[2].trim());
        }

        FolioDTO folio = new FolioDTO();
        String folioNumber = getFolioNumber(splitLines[0]);
        folio.setFolioNo(Long.parseLong(folioNumber.trim()));

        List<TransactionDTO> folioTransactions = getFolioTransactionList(splitLines);
        folio.setTransactions(folioTransactions);
        List<FolioDTO> folioList = new ArrayList<FolioDTO>();
        folioList.add(folio);
        fund.setFolios(folioList);
        return fund;

    }

    private List<TransactionDTO> getFolioTransactionList(String[] folioSplitLines) {
        List<TransactionDTO> transactionList = new ArrayList<>();

        for (int i = 4; i < folioSplitLines.length; i++) {
            try {
                if (folioSplitLines[i].startsWith("Page")) {
                    i = i + 4;
                    continue;
                }
                if (!startsWithDate(folioSplitLines[i])) {
                    continue;
                }
                TransactionDTO transactionDTO = getParsedTransaction(folioSplitLines[i]);
                if (transactionDTO != null) {
                    log.info(transactionDTO.toString());
                    transactionList.add(transactionDTO);
                }

            } catch (ParseException e) {
                log.error(e.getMessage());
            }
        }
        return transactionList;
    }

    private boolean startsWithDate(String folioSplitLine) {
        Set<Character> set = new HashSet<>();
        set.add('0');
        set.add('1');
        set.add('2');
        set.add('3');
        return set.contains(folioSplitLine.charAt(0));
    }

    private TransactionDTO getParsedTransaction(String transactionLine) throws ParseException {

        TransactionDTO transaction = new TransactionDTO();
        String[] transactionDetails = transactionLine.trim().split(" ");
        Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(transactionDetails[0]);
        transaction.setDate(date);
        if (transactionDetails[1].startsWith("***")) {
            return null;
        }
        int lineLength = transactionDetails.length;
        try {
            transaction.setAmount(Float.parseFloat(getStringWithoutCharacters(transactionDetails[lineLength - 4])));
            transaction.setUnits(Float.parseFloat(getStringWithoutCharacters(transactionDetails[lineLength - 3])));
            transaction.setPrice(Float.parseFloat(getStringWithoutCharacters(transactionDetails[lineLength - 2])));
            transaction.setUnitBalance(Float.parseFloat(getStringWithoutCharacters(transactionDetails[lineLength - 1])));
        } catch (Exception e) {
            log.error("Error Occurred for line -{} -{}", transactionDetails, e.getMessage());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lineLength - 4; i++) {
            sb.append(transactionDetails[i]).append(" ");
        }
        transaction.setTransaction(sb.toString().trim());
        return transaction;
    }

    private String getFolioNumber(String folioLine) {
        String[] folioElements = folioLine.split(" ");
        return folioElements[1];
    }

    private String getStringWithoutCharacters(String value) {
        return value.replace(",", "");
    }
}

