package uj.java.w7.insurance;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class FloridaInsurance {

    final static String COUNT = "count.txt";
    final static String MOST_VALUABLE = "most_valuable.txt";
    final static String TIV_2012 = "tiv2012.txt";

    public static void main(String[] args) {
        List<Entry> dataList = new csvDataExtractor().getListOfData();

        var fi = new FloridaInsurance();
        fi.count(dataList);
        fi.tiv2012(dataList);
        fi.most_valuable(dataList);

    }

    private void count(List<Entry> dataList) {
        var result = dataList.stream().map(Entry::county).distinct().count();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(COUNT))) {
            bufferedWriter.write(Long.valueOf(result).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tiv2012(List<Entry> dataList) {
        var result = dataList.stream().map(Entry::tiv_2012).reduce(BigDecimal.ZERO, BigDecimal::add);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(TIV_2012))) {
            bufferedWriter.write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void most_valuable(List<Entry> dataList) {
        var result = dataList.stream()
                .map(el -> new CountyGrowth(el.county(), el.tiv_2012().subtract(el.tiv_2011())))
                .collect(Collectors.groupingBy(CountyGrowth::county, Collectors.reducing(BigDecimal.ZERO, CountyGrowth::growth, BigDecimal::add)))
                .entrySet()
                .stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(MOST_VALUABLE))) {
            bufferedWriter.write("county,value");
            bufferedWriter.newLine();
            for (var entry : result) {
                bufferedWriter.write(entry.getKey() + "," + entry.getValue());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}