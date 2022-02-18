package uj.java.w7.insurance;

import java.math.BigDecimal;

public record Entry(
        int policyID,
        String state_code,
        String county,
        double eq_site_limit,
        double hu_site_limit,
        double fl_site_limit,
        double fr_site_limit,
        BigDecimal tiv_2011,
        BigDecimal tiv_2012,
        double eq_site_deductible,
        double hu_site_deductible,
        double fl_site_deductible,
        double fr_site_deductible,
        double point_latitude,
        double point_longitude,
        String line,
        String construction,
        int point_granularity
) {}
