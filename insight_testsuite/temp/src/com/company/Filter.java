package com.company;

import java.util.Map;
import java.util.Set;

public class Filter {
    public static String filter (String line, Map<String, Integer> donors, Set<String> repeatDonors, Map<String, Recipient> contribution, Integer percentile) {

        if(line.isEmpty() || line.length() == 0 || line.replaceAll(" ", "").isEmpty() ||
                line.replaceAll(" ", "").length() == 0) {
            return "";
        }

        String[] element = line.trim().split("\\|");
        String CMTE_ID = element[0];
        String NAME = element[7];
        String ZIP_CODE = element[10];
        String TRANSACTION_DT = element[13];
        String TRANSACTION_AMT = element[14];

        //  Ignore invalid record
        if(!element[15].isEmpty() || CMTE_ID.isEmpty() || CMTE_ID.length() == 0 || CMTE_ID.contains(" ") || NAME.isEmpty()){   // not a individual, invalid CMTE_ID, invalid NAME
            return "";
        }
        if(TRANSACTION_DT.isEmpty() || TRANSACTION_DT.length() != 8 || !Date.isValidDate(TRANSACTION_DT)) {   //  invalid date
            return "";
        }
        if(ZIP_CODE.isEmpty() || ZIP_CODE.length() < 5 || ZIP_CODE.length() > 9) {   //  invalid zip code
            return "";
        }
        if(TRANSACTION_AMT.isEmpty() || TRANSACTION_AMT.length() == 0 || TRANSACTION_AMT.contains(" ")) {   //  invalid amount
            return "";
        }

        //  Identify repeat donors
        ZIP_CODE = ZIP_CODE.substring(0,5);
        String donor = NAME + "_" + ZIP_CODE;
        Integer currentYear = Integer.parseInt(TRANSACTION_DT.substring(4,8));

        if(!repeatDonors.contains(donor)) {
            if(donors.containsKey(donor) && currentYear > donors.get(donor)) {   //  become a repeat donor, donors HashMap <name_zipcode, year>
                repeatDonors.add(donor);
                donors.remove(donor);   //  move to repeatDonors, repeatDonors HashSet <name_zipcode>
            } else {
                donors.put(donor, currentYear);    //  new donor or update to the earlier year, still not a repeat donor
                return "";
            }
        }

        //  Calculations for repeat donors only
        Long percentileAmount;
        String donorContribute = currentYear + CMTE_ID + ZIP_CODE;
        Double currentAmount = Double.parseDouble(TRANSACTION_AMT);
        if(!contribution.containsKey(donorContribute)) {    //  a new contribution
            Recipient recipient = new Recipient();
            contribution.put(donorContribute, recipient); // contribution HashMap <year+recipient+zipcode, Recipient>
        }
        contribution.get(donorContribute).insertAmount(currentAmount);
        percentileAmount = contribution.get(donorContribute).getPercentile(percentile);

        //  Remove float digit (".0") if amount is an integer
        Double amt = contribution.get(donorContribute).getTotalAmount();
        String amount;
        if (amt - Math.floor(amt) < 1e-10) {  //  if the amount is an integer, output without ".0"
            amount = String.valueOf(amt.intValue());
        } else {
            amount = String.valueOf(amt);
        }

        String output = CMTE_ID + "|" + ZIP_CODE + "|" + currentYear + "|" + percentileAmount + "|"
                + amount + "|" + contribution.get(donorContribute).getContributionNumber();

        return output;
    }
}
