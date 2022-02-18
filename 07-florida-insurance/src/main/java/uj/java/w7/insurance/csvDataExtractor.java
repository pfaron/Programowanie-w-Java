package uj.java.w7.insurance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class csvDataExtractor {


    public List<Entry> getListOfData() {
        List<Entry> dataList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(new File("FL_insurance.csv.zip"));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("FL_insurance.csv"))))){

            bufferedReader.readLine();
            while(bufferedReader.ready()){
                String[] line = bufferedReader.readLine().split(",");
                dataList.add(createNewEntryFromLine(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dataList;
    }

    private Entry createNewEntryFromLine(String[] line){
        return new Entry(
                Integer.parseInt(line[0]),
                line[1],
                line[2],
                Double.parseDouble(line[3]),
                Double.parseDouble(line[4]),
                Double.parseDouble(line[5]),
                Double.parseDouble(line[6]),
                BigDecimal.valueOf(Double.parseDouble(line[7])),
                BigDecimal.valueOf(Double.parseDouble(line[8])),
                Double.parseDouble(line[9]),
                Double.parseDouble(line[10]),
                Double.parseDouble(line[11]),
                Double.parseDouble(line[12]),
                Double.parseDouble(line[13]),
                Double.parseDouble(line[14]),
                line[15],
                line[16],
                Integer.parseInt(line[17])
        );
    }
}
