@MathOps
Feature: Basic operations

  Scenario: Sum several numbers
    Given I ADD "0.3"
    Then Result is "0.3"

  Scenario: Subtract several numbers
    Given I SUBTRACT "0.5"
    Then Result is "0.3"