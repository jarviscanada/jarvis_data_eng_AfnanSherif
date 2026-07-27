package ca.jrvs.apps.practice;

import ca.jrvs.apps.practice.Company;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DtoExercise {

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String json = """
        {
          "symbol":"AAPL",
          "companyName":"Apple Inc.",
          "exchange":"Nasdaq Global Select",
          "CEO":"Timothy D. Cook",
          "sector":"Technology"
        }
        """;


        // JSON -> Java Object
        Company company =
                mapper.readValue(json, Company.class);


        System.out.println(company);


        // Java Object -> JSON
        String output =
                mapper.writeValueAsString(company);


        System.out.println(output);
    }
}