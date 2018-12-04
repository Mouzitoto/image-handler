package kz.mouzitoto.imagehandler.services;

import kz.mouzitoto.imagehandler.exceptions.IHBadInputException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Method name convention for this class: methodThatWeAreTesting_passedArgs_expectedResult
 */

public class ValidateArgumentsTest {

    private ImageService imageService = new ImageService();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void validateArgs_empty_IHBadInputException() throws IHBadInputException {
        exceptionRule.expect(IHBadInputException.class);
        exceptionRule.expectMessage("Not enough arguments, must be 3 arguments.");

        imageService.validateArgs(new String[]{});
    }

    @Test
    public void validateArgs_lessThan3Args_IHBadInputException() throws IHBadInputException {
        exceptionRule.expect(IHBadInputException.class);
        exceptionRule.expectMessage("Not enough arguments, must be 3 arguments.");

        imageService.validateArgs(new String[]{"a", "b"});
    }

    @Test
    public void validateArgs_badSecondArg_IHBadInputException() throws IHBadInputException {
        exceptionRule.expect(IHBadInputException.class);
        exceptionRule.expectMessage("Bad image width argument (second argument); Must be only digits.");

        imageService.validateArgs(new String[]{"we are not validating image path", "bad parameter", "this will not be reached"});
    }

    @Test
    public void validateArgs_badThirdArg_IHBadInputException() throws IHBadInputException {
        exceptionRule.expect(IHBadInputException.class);
        exceptionRule.expectMessage("Bad image height argument (third argument); Must be only digits.");

        imageService.validateArgs(new String[]{"we are not validating image path", "480", "bad parameter"});
    }

    @Test
    public void validateArgs_allGood_success() throws IHBadInputException {
        imageService.validateArgs(new String[]{"we are not validating image path", "480", "640"});
    }
}