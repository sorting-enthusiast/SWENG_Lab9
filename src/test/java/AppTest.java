import org.example.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * JavaFX App
 */
@ExtendWith(ApplicationExtension.class)
public class AppTest {

    @Start
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(loadFXML("primary"), 640, 480));
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @TestFactory
    Stream<DynamicTest> test_factory(FxRobot robot) {
        return Stream.of(
                "16\n" +
                        "-A00 - 1B * 11 / 9 + 3D" + "$" + "-9F6",
                "16\n" +
                        "A00 / 0" + "$" + "Error: trying to divide by 0 (evaluated: \"0\")",
                "16\n" +
                        "1C  + 2D + 3F -          4E" + "$" + "3A",
                "8\n" +
                        "700 - 12 * 11 / 6 + 3" + "$" + "664",
                "2\n" +
                        "00001001 & 00001000 | ~00000001" + "$" + "11111110",
                "10\n" +
                        "59 ++ 1" + "$" + "Error: invalid expression",
                "8\n" +
                        "700 - 12 * 11 / 6 + 3" + "$" + "664",
                "10\n" +
                        "58 + 1" + "$" + "59",
                "10\n" +
                        "-100 + 50 * 3 / 2 - 5" + "$" + "-30",
                "8\n" +
                        "710 - 14 * 10 / 4 + 5" + "$" + "665",
                "16\n" +
                        "B2 - 3C * 2 + A0 / 5" + "$" + "5A",
                "2\n" +
                        "00001010 & 00001100 | 00000011" + "$" + "00001011",
                "8\n" +
                        "777 + 10 - 5 * 10 / 4" + "$" + "775",
                "2\n" +
                        "00001101 ^ 00001011 | 00000110" + "$" + "00000110",
                "8\n" +
                        "-7 + 5" + "$" + "-2"
        ).map(
                test -> DynamicTest.dynamicTest("Test: " + test, () -> {
                    try {
                        var split = test.split("\\$");
                        String[] lines = split[0].split("\n");
                        int base = Integer.parseInt(lines[0].trim());
                        var expr = lines[1].replace(" ", "");
                        String base_str;
                        switch (base) {
                            case 10:
                                base_str = "dec";
                                break;
                            case 2:
                                base_str = "bin";
                                break;
                            case 16:
                                base_str = "hex";
                                break;
                            case 8:
                                base_str = "oct";
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + base);
                        }
                        ;
                        robot.clickOn("#baseListBox");
                        robot.clickOn(base_str);
                        for (var c : expr.toCharArray()) {
                            switch (c) {
                                case '+':
                                    robot.clickOn("#btnAdd");
                                    break;
                                case '-':
                                    robot.clickOn("#btnSub");
                                    break;
                                case '*':
                                    robot.clickOn("#btnMul");
                                    break;
                                case '/':
                                    robot.clickOn("#btnDiv");
                                    break;
                                case '^':
                                    robot.clickOn("#btnXor");
                                    break;
                                case '&':
                                    robot.clickOn("#btnAnd");
                                    break;
                                case '|':
                                    robot.clickOn("#btnOr");
                                    break;
                                case '~':
                                    robot.clickOn("#btnNot");
                                    break;
                                default:
                                    robot.clickOn("#btn" + c);
                                    break;
                            }
                        }
                        robot.clickOn("#btnEval");
                        String expected_output = split[1];
                        FxAssert.verifyThat("#consoleTF",
                                TextInputControlMatchers.hasText(expected_output));
                    } finally {
                        robot.clickOn("#btnClear");
                    }
                })
        );
    }
}