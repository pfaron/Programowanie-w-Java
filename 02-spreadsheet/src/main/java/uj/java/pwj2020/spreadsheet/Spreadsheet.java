package uj.java.pwj2020.spreadsheet;

public class Spreadsheet {

    private String[][] spreadsheet;

    private record IntOperands(int leftOperand, int rightOperand) {
    }

    public String[][] calculate(String[][] spreadsheet) {

        this.spreadsheet = spreadsheet;

        for (int i = 0; i < spreadsheet.length; i++)
            for (int j = 0; j < spreadsheet[i].length; j++)
                solveCellValue(i, j);

        return spreadsheet;
    }

    private void solveCellValue(int i, int j) {

        switch (spreadsheet[i][j].charAt(0)) {
            case '$' -> spreadsheet[i][j] = getReferenceValue(spreadsheet[i][j]);
            case '=' -> spreadsheet[i][j] = getFormulaValue(spreadsheet[i][j]);
        }

    }

    private String switchCellOperation(String cell) {

        return switch (cell.charAt(0)) {
            case '$' -> getReferenceValue(cell);
            case '=' -> getFormulaValue(cell);
            default -> cell;
        };

    }

    private String getReferenceValue(String reference) {

        int x = StringUtil.horizontalIndex(reference);
        int y = StringUtil.verticalIndex(reference);

        return switchCellOperation(spreadsheet[x][y]);

    }

    private String getFormulaValue(String formula) {

        var parameters = StringUtil.extractFormulaParameters(formula);

        var operands = new IntOperands(Integer.parseInt(switchCellOperation(parameters.leftParameter())),
                Integer.parseInt(switchCellOperation(parameters.rightParameter())));

        String operation = formula.substring(1, formula.indexOf('('));

        return String.valueOf(solveFormulaOperation(operation, operands));
    }

    private int solveFormulaOperation(String operation, IntOperands operands) {

        return switch (operation) {
            case "ADD" -> operands.leftOperand + operands.rightOperand;
            case "SUB" -> operands.leftOperand - operands.rightOperand;
            case "MUL" -> operands.leftOperand * operands.rightOperand;
            case "DIV" -> operands.leftOperand / operands.rightOperand;
            case "MOD" -> operands.leftOperand % operands.rightOperand;
            default -> throw new RuntimeException("Unknown operation in formula.");
        };
    }
}