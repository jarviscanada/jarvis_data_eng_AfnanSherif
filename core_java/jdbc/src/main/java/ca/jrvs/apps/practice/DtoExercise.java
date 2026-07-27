package ca.jrvs.apps.practice;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DtoExercise {

    // Sample JSON document
    public static final String companyStr =
            "{\n"
                    + "   \"symbol\":\"AAPL\",\n"
                    + "   \"companyName\":\"Apple Inc.\",\n"
                    + "   \"exchange\":\"Nasdaq Global Select\",\n"
                    + "   \"description\":\"Apple Inc.\",\n"
                    + "   \"CEO\":\"Timothy D. Cook\",\n"
                    + "   \"sector\":\"Technology\",\n"
                    + "   \"financials\":[\n"
                    + "      {\n"
                    + "         \"reportDate\":\"2018-12-31\",\n"
                    + "         \"grossProfit\":32031000000,\n"
                    + "         \"costOfRevenue\":52279000000,\n"
                    + "         \"operatingRevenue\":84310000000,\n"
                    + "         \"totalRevenue\":84310000000,\n"
                    + "         \"operatingIncome\":23346000000,\n"
                    + "         \"netIncome\":19965000000\n"
                    + "      }\n"
                    + "   ],\n"
                    + "   \"dividends\":[\n"
                    + "      {\n"
                    + "         \"exDate\":\"2018-02-09\",\n"
                    + "         \"paymentDate\":\"2018-02-15\",\n"
                    + "         \"recordDate\":\"2018-02-12\",\n"
                    + "         \"declaredDate\":\"2018-02-01\",\n"
                    + "         \"amount\":0.63\n"
                    + "      }\n"
                    + "   ]\n"
                    + "}";


    public static void main(String[] args) throws Exception {


        ObjectMapper mapper = new ObjectMapper();


        // JSON -> Java Object (Deserialization)
        Company company =
                mapper.readValue(
                        companyStr,
                        Company.class
                );


        System.out.println("Company:");
        System.out.println(
                company.getCompanyName()
        );


        System.out.println(
                "Financial reports: "
                        + company.getFinancials().size()
        );


        System.out.println(
                "Dividends: "
                        + company.getDividends().size()
        );


        // Java Object -> JSON (Serialization)
        String json =
                mapper.writeValueAsString(company);


        System.out.println("\nSerialized JSON:");
        System.out.println(json);

    }

}