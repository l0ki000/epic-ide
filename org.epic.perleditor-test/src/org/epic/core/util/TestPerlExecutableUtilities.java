package org.epic.core.util;

import org.epic.perl.editor.test.BaseTestCase;

import org.junit.Assert;
import org.junit.Test;

public class TestPerlExecutableUtilities extends BaseTestCase
{
    @Test
    public void testTranslatePathForCygwin() throws Exception
    {
        _test("C:\\Program Files\\foobar", "/cygdrive/c/program files/foobar");
        _test("\\Program Files\\foobar", "/program files/foobar");
        _test("foobar/BAZ", "foobar/baz");
        _test("x:\\foo", "/cygdrive/x/foo");
        _test("", "");
    }
    
    // TODO more, more tests here!!!
    
    private void _test(String in, String out)
    {
        Assert.assertEquals(out, PerlExecutableUtilities.translatePathForCygwin(in));
    }
}
