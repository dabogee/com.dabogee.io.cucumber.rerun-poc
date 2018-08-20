package com.dabogee.io.cucumber.glue.steps;

import com.dabogee.io.cucumber.glue.enums.MathOpTypes;
import com.dabogee.io.cucumber.glue.models.ElementaryOperation;
import cucumber.api.java.en.Given;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;

import java.util.ArrayList;
import java.util.List;

public class CalculatorSteps {

    private List<ElementaryOperation> mathOps = new ArrayList<>();

    @Given("^I (ADD|SUBTRACT|MULTIPLE|DIVIDE) \"(\\d+.\\d+)\"$")
    public void executeOperation(MathOpTypes op, Double value) {
        mathOps.add(new ElementaryOperation(op, value));
    }

    @Given("Result is \"(\\d+.\\d+)\"")
    public void executeOperation(Double expected) {
        Double actual = .0;
        for (ElementaryOperation o : mathOps) {
            actual = o.getOp().getMathOp().execute(actual, o.getValue());
        }
        assertThat(actual).isEqualTo(expected, Offset.offset(.0001));
    }
}
