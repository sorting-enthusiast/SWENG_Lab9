package org.example;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


abstract class AbstractParser {
    String input;
    int cursor = 0;

    abstract int parse_lowest_priority();
    int parse(String input) {
        if (input.isEmpty()) throw new ArithmeticException("invalid expression");
        this.input = input;
        int value = parse_lowest_priority();
        if (cursor != input.length()) throw new ArithmeticException("invalid expression");
        return value;
    }
    protected char current_char() { return input.charAt(cursor); }
    protected boolean has_input() { return cursor < input.length(); }
    protected void skip_whitespace() { while (has_input() && current_char() == ' ') ++cursor; }
    protected boolean find(char c) {
        skip_whitespace();
        return has_input() && current_char() == c;
    }

}

abstract class ArithmeticParser extends AbstractParser {
    abstract protected int parse_num();
    protected int parse_high_priority() {
        int v = parse_num();
        for (;;) {
            if (find('*')) { ++cursor; v *= parse_num(); }
            else if (find('/')) {
                ++cursor;
                int rhs = parse_num();
                if (rhs == 0) throw new ArithmeticException("trying to divide by 0 (evaluated: \"0\")");
                v /= rhs;
            }
            else return v;
        }
    }
    @Override
    protected int parse_lowest_priority() {
        skip_whitespace();
        if (cursor >= input.length()) throw new ArithmeticException("no input");
        int v = parse_high_priority();
        for (;;) {
            if (find('+')) { ++cursor; v += parse_high_priority(); }
            else if (find('-')) { ++cursor; v -= parse_high_priority(); }
            else return v;
        }
    }
}
final class BinaryArithmeticParser extends ArithmeticParser {
    @Override
    protected int parse_num() {
        skip_whitespace();
        if (cursor + 8 > input.length()) throw new ArithmeticException("invalid expression");
        int end = cursor + 8;
        byte val = 0;
        for (; cursor < end; ++cursor) {
            val <<= 1;
            byte last_digit;
            switch (input.charAt(cursor)) {
                case '0': last_digit = 0; break;
                case '1': last_digit = 1; break;
                default: throw new ArithmeticException("invalid expression");
            }
            val |= last_digit;
        }
        return val;
    }
}
final class BinaryBitwiseParser extends AbstractParser {
    private byte parse_8_bit_int() {
        skip_whitespace();
        if (cursor + 8 > input.length()) throw new ArithmeticException("invalid expression");
        int end = cursor + 8;
        byte val = 0;
        for (; cursor < end; ++cursor) {
            val <<= 1;
            byte last_digit;
            switch (input.charAt(cursor)) {
                case '0': last_digit = 0; break;
                case '1': last_digit = 1; break;
                default: throw new ArithmeticException("invalid expression");
            }
            val |= last_digit;
        }
        return val;
    }

    private byte parse_neg() {
        boolean neg = find('~');
        if (neg) ++cursor;
        byte val = parse_8_bit_int();
        return neg ? (byte) ~val : val;
    }
    private byte parse_and() {
        byte v = parse_neg();
        while (find('&')) { ++cursor; v &= parse_neg(); }
        return v;
    }
    private byte parse_xor() {
        byte v = parse_and();
        while (find('^')) { ++cursor; v ^= parse_and(); }
        return v;
    }
    @Override
    int parse_lowest_priority() {
        byte v = parse_xor();
        while (find('|')) { ++cursor; v |= parse_xor(); }
        return v;
    }
}
final class Parser extends ArithmeticParser {
    int radix;
    public Parser(int radix) {
        this.radix = radix;
    }
    private boolean is_digit(char c) {
        return Character.digit(c, radix) != -1;
    }
    @Override
    protected int parse_num() {
        skip_whitespace();
        if (cursor >= input.length()) throw new ArithmeticException("invalid expression");
        int sign = current_char() == '-' ? -1 : 1;
        cursor += sign == -1 ? 1 : 0;
        skip_whitespace();
        int start = cursor;
        while (has_input() && is_digit(current_char())) { ++cursor; }
        if (start == cursor) throw new ArithmeticException("invalid expression");
        return sign * Integer.parseInt(input.substring(start, cursor), radix);
    }
}

public class PrimaryController {
    private static int base = 10;
    private static String input = "";
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnA;

    @FXML
    private Button btnB;

    @FXML
    private Button btnC;

    @FXML
    private Button btnD;

    @FXML
    private Button btnE;

    @FXML
    private Button btnF;

    @FXML
    private Button btnAdd;

    @FXML
    private ComboBox<String> baseListBox;

    @FXML
    private Button btnAnd;

    @FXML
    private Button btnNot;

    @FXML
    private Button btnOr;

    @FXML
    private Button btnXor;

    @FXML
    private Button btnClear;

    @FXML
    private TextField consoleTF;

    @FXML
    private Button btnDiv;

    @FXML
    private Button btn8;

    @FXML
    private Button btnEval;

    @FXML
    private Button btn5;

    @FXML
    private Button btn4;

    @FXML
    private Button btnMul;

    @FXML
    private Button btn9;

    @FXML
    private Button btn1;

    @FXML
    private Button btn7;

    @FXML
    private Button btn6;

    @FXML
    private Button btnSub;

    @FXML
    private Button btn3;

    @FXML
    private Button btn2;

    @FXML
    private Button btn0;

    @FXML
    void addA(ActionEvent event) {
        if (base == 16) {
            input += 'A';
            consoleTF.setText(input);
        }
    }

    @FXML
    void addAdd(ActionEvent event) {
        input += '+';
        consoleTF.setText(input);
    }

    @FXML
    void addAnd(ActionEvent event) {
        input += '&';
        consoleTF.setText(input);
    }

    @FXML
    void addB(ActionEvent event) {
        input += 'B';
        consoleTF.setText(input);
    }

    @FXML
    void addC(ActionEvent event) {
        input += 'C';
        consoleTF.setText(input);
    }

    @FXML
    void addD(ActionEvent event) {
        input += 'D';
        consoleTF.setText(input);
    }

    @FXML
    void addDiv(ActionEvent event) {
        input += '/';
        consoleTF.setText(input);
    }

    @FXML
    void addE(ActionEvent event) {
        input += 'E';
        consoleTF.setText(input);
    }

    @FXML
    void addEight(ActionEvent event) {
        input += 8;
        consoleTF.setText(input);
    }

    @FXML
    void addF(ActionEvent event) {
        input += 'F';
        consoleTF.setText(input);
    }

    @FXML
    void addFive(ActionEvent event) {
        input += 5;
        consoleTF.setText(input);
    }

    @FXML
    void addFour(ActionEvent event) {
        input += 4;
        consoleTF.setText(input);
    }

    @FXML
    void addMul(ActionEvent event) {
        input += '*';
        consoleTF.setText(input);
    }

    @FXML
    void addNine(ActionEvent event) {
        input += 9;
        consoleTF.setText(input);
    }

    @FXML
    void addNot(ActionEvent event) {
        input += '~';
        consoleTF.setText(input);
    }

    @FXML
    void addOne(ActionEvent event) {
        input += 1;
        consoleTF.setText(input);
    }

    @FXML
    void addOr(ActionEvent event) {
        input += '|';
        consoleTF.setText(input);
    }

    @FXML
    void addSeven(ActionEvent event) {
        input += 7;
        consoleTF.setText(input);
    }

    @FXML
    void addSix(ActionEvent event) {
        input += 6;
        consoleTF.setText(input);
    }

    @FXML
    void addSub(ActionEvent event) {
        input += '-';
        consoleTF.setText(input);
    }

    @FXML
    void addThree(ActionEvent event) {
        input += 3;
        consoleTF.setText(input);
    }

    @FXML
    void addTwo(ActionEvent event) {
        input += 2;
        consoleTF.setText(input);
    }

    @FXML
    void addXor(ActionEvent event) {
        input += '^';
        consoleTF.setText(input);
    }

    @FXML
    void addZero(ActionEvent event) {
        input += 0;
        consoleTF.setText(input);
    }

    private void switchToBase() {
        boolean hex = base == 16;
        btnA.setDisable(!hex);
        btnB.setDisable(!hex);
        btnC.setDisable(!hex);
        btnD.setDisable(!hex);
        btnE.setDisable(!hex);
        btnF.setDisable(!hex);
        boolean dec_plus = base >= 10;
        btn9.setDisable(!dec_plus);
        btn8.setDisable(!dec_plus);
        boolean oct_plus = base >= 8;
        btn7.setDisable(!oct_plus);
        btn6.setDisable(!oct_plus);
        btn5.setDisable(!oct_plus);
        btn4.setDisable(!oct_plus);
        btn3.setDisable(!oct_plus);
        btn2.setDisable(!oct_plus);
        boolean binary = base == 2;
        btnAnd.setDisable(!binary);
        btnOr.setDisable(!binary);
        btnXor.setDisable(!binary);
        btnNot.setDisable(!binary);
    }
    @FXML
    void chooseBase(ActionEvent event) {
        String chosen = baseListBox.getSelectionModel().getSelectedItem();
        int newBase = 10;
        switch (chosen) {
            case "dec": break;
            case "hex": newBase = 16; break;
            case "oct": newBase = 8; break;
            case "bin": newBase = 2; break;
        }
        if (!Objects.equals(input, "")) {
            try {
                int val = evalHelper();
                input = newBase == 2 ? String.format("%8s", Integer.toBinaryString(val & 0xFF)).replace(' ', '0') : Integer.toString(val, newBase).toUpperCase();
            } catch (Exception e) {
                input = "Error: " + e.getMessage();
            }
            consoleTF.setText(input);
        }
        base = newBase;
        switchToBase();
    }

    @FXML
    void clearInput(ActionEvent event) {
        input = "";
        consoleTF.setText(input);
    }
    private int evalHelper() {
        AbstractParser p;
        if (base == 2) {
            if (input.contains("|") || input.contains("&") || input.contains("^") || input.contains("~")) {
                p = new BinaryBitwiseParser();
            } else {
                p = new BinaryArithmeticParser();
            }
        } else {
            p = new Parser(base);
        }
        return p.parse(input);
    }
    @FXML
    void evalExpr(ActionEvent event) {
        try {
            int val = evalHelper();
            input = base == 2 ? String.format("%8s", Integer.toBinaryString(val & 0xFF)).replace(' ', '0') : Integer.toString(val, base).toUpperCase();
        } catch (Exception e) {
            input = "Error: " + e.getMessage();
        }
        consoleTF.setText(input);
    }

    @FXML
    void initialize() {
        assert btnA != null : "fx:id=\"A\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnB != null : "fx:id=\"B\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnC != null : "fx:id=\"C\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnD != null : "fx:id=\"D\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnE != null : "fx:id=\"E\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnF != null : "fx:id=\"F\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnAdd != null : "fx:id=\"addBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert baseListBox != null : "fx:id=\"baseListBox\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnAnd != null : "fx:id=\"bitAnd\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnNot != null : "fx:id=\"bitNot\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnOr != null : "fx:id=\"bitOr\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnXor != null : "fx:id=\"bitXor\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnClear != null : "fx:id=\"clearBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert consoleTF != null : "fx:id=\"consoleTF\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnDiv != null : "fx:id=\"divBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn8 != null : "fx:id=\"eightBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnEval != null : "fx:id=\"evalBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn5 != null : "fx:id=\"fiveBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn4 != null : "fx:id=\"fourBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnMul != null : "fx:id=\"mulBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn9 != null : "fx:id=\"nineBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn1 != null : "fx:id=\"oneBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn7 != null : "fx:id=\"sevenBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn6 != null : "fx:id=\"sixBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btnSub != null : "fx:id=\"subBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn3 != null : "fx:id=\"threeBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn2 != null : "fx:id=\"twoBtn\" was not injected: check your FXML file 'primary.fxml'.";
        assert btn0 != null : "fx:id=\"zeroBtn\" was not injected: check your FXML file 'primary.fxml'.";
        baseListBox.getItems().add("bin");
        baseListBox.getItems().add("oct");
        baseListBox.getItems().add("dec");
        baseListBox.getItems().add("hex");

        btnA.setDisable(true);
        btnB.setDisable(true);
        btnC.setDisable(true);
        btnD.setDisable(true);
        btnE.setDisable(true);
        btnF.setDisable(true);
        btnAnd.setDisable(true);
        btnOr.setDisable(true);
        btnXor.setDisable(true);
        btnNot.setDisable(true);
    }

}
