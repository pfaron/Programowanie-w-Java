package uj.java.pwj2020.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Calc {

    public BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        ZonedDateTime startDateTime = parseZonedDateTime(start);
        ZonedDateTime endDateTime = parseZonedDateTime(end);

        BigDecimal amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        if (!startDateTime.isBefore(endDateTime))
            return amount;

        long days = startDateTime.until(endDateTime, ChronoUnit.DAYS);
        amount = amount.add(dailyRate.multiply(BigDecimal.valueOf(days)));

        long minutes = startDateTime.until(endDateTime, ChronoUnit.MINUTES) % (24 * 60);

        if (minutes == 0)
            return amount;

        if (minutes < 8 * 60) {
            amount = amount.add(dailyRate.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP));
        } else if (minutes < 12 * 60) {
            amount = amount.add(dailyRate.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
        } else {
            amount = amount.add(dailyRate);
        }

        return amount;
    }

    private ZonedDateTime parseZonedDateTime(String data) {
        String[] parts = data.split(" ");
        ZoneId zoneId = ZoneId.of(parts[2]);

        String sb = parts[0] + "T" + parts[1] + ":00" +
                zoneId.getRules().getOffset(LocalDateTime.now()) +
                "[" + zoneId + "]";

        return ZonedDateTime.parse(sb);
    }
}
