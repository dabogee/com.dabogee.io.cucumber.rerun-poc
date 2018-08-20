package com.dabogee.io.cucumber.glue.models;

import com.dabogee.io.cucumber.glue.enums.MathOpTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElementaryOperation {

    private MathOpTypes op;
    private Double value;
}
