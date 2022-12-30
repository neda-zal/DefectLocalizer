package org.example.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.checks.DuplicationCheck;
import org.example.checks.HTMLCharacterUsageCheck;
import org.example.checks.HyphenBetweenNumbersCheck;
import org.example.checks.MultipleUppercaseLettersInSentenceCheck;
import org.example.checks.MultiplicationBetweenNumbersCheck;
import org.example.checks.NumberFractionCheck;
import org.example.checks.NumberingCheck;
import org.example.checks.QuotationCheck;
import org.example.checks.SpaceAfterDotCheck;
import org.example.checks.SpaceAfterNumberCheck;
import org.example.checks.ThousandSeparatorCheck;
import org.example.checks.UnusedLithuanianWordsCheck;
import org.example.model.Property;
import org.example.model.Violation;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class InadequacyCheckService {

  public List<Violation> checkViolations(List<Property> properties) {
    List<Violation> violations = new ArrayList<>();

    DuplicationCheck duplicationCheck = new DuplicationCheck(properties, violations);
    duplicationCheck.check();

    HyphenBetweenNumbersCheck hyphenBetweenNumbersCheck = new HyphenBetweenNumbersCheck(properties, violations);
    hyphenBetweenNumbersCheck.check();

    HTMLCharacterUsageCheck htmlCharacterUsageCheck = new HTMLCharacterUsageCheck(properties, violations);
    htmlCharacterUsageCheck.check();

    MultiplicationBetweenNumbersCheck multiplicationBetweenNumbersCheck = new MultiplicationBetweenNumbersCheck(
        properties, violations);
    multiplicationBetweenNumbersCheck.check();

    NumberFractionCheck numberFractionCheck = new NumberFractionCheck(properties, violations);
    numberFractionCheck.check();

    NumberingCheck numberingCheck = new NumberingCheck(properties, violations);
    numberingCheck.check();

    QuotationCheck quotationCheck = new QuotationCheck(properties, violations);
    quotationCheck.check();

    SpaceAfterDotCheck spaceAfterDotCheck = new SpaceAfterDotCheck(properties, violations);
    spaceAfterDotCheck.check();

    SpaceAfterNumberCheck spaceAfterNumberCheck = new SpaceAfterNumberCheck(properties, violations);
    spaceAfterNumberCheck.check();

    ThousandSeparatorCheck thousandSeparatorCheck = new ThousandSeparatorCheck(properties, violations);
    thousandSeparatorCheck.check();

    UnusedLithuanianWordsCheck unusedLithuanianWordsCheck = new UnusedLithuanianWordsCheck(
        properties, violations);
    unusedLithuanianWordsCheck.check();

    MultipleUppercaseLettersInSentenceCheck multipleUppercaseLettersInSentenceCheck
        = new MultipleUppercaseLettersInSentenceCheck(properties, violations);
    multipleUppercaseLettersInSentenceCheck.check();

    return violations;

  }

}
