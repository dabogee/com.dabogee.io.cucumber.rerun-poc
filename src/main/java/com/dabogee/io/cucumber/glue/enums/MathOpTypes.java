package com.dabogee.io.cucumber.glue.enums;

public enum MathOpTypes {

    ADD((before, inc) -> before + inc),
    SUBTRACT((before, inc) -> before - inc),
    MULTIPLE((before, inc) -> before * inc),
    DIVIDE((before, inc) -> before / inc);

    private MathOp mathOp;

    MathOpTypes(MathOp mathOp) {
        this.mathOp = mathOp;
    }

    public MathOp getMathOp() {
        return mathOp;
    }
}
