package org.epic.perleditor.editors.util;

import org.epic.perl.editor.test.BaseTestCase;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPerlValidatorErrors extends BaseTestCase
{
    @Test
    public void testWarning() throws Exception
    {
        PerlValidatorErrors errors = new PerlValidatorErrors();        
        String msg = "Scalar value @_[0] better written as $_[0]";
        
        PerlValidatorErrors.ErrorMessage errorMsg = errors.getErrorMessage(msg);
        
        assertTrue(!errorMsg.isUnknown());
        assertTrue(errorMsg.isWarning());
    }
}
