package org.epic.core.util;

import java.util.Iterator;
import java.util.List;

import org.epic.perl.editor.test.BaseTestCase;
import org.junit.Test;
import org.junit.Assert;


public class TestCommandLineTokenizer extends BaseTestCase
{
    @Test
    public void testAll() throws Exception
    {
        _test("first second third", "first,second,third");
        _test("/usr/bin/perl", "/usr/bin/perl");
        _test("c:\\perl\\bin\\perl.exe", "c:\\perl\\bin\\perl.exe");
        _test("c:\\perl\\bin\\perl.exe blah", "c:\\perl\\bin\\perl.exe,blah");
        _test(
            "\"c:/Program Files/Perl/bin/perl.exe\" \"first arg\" \"second\" 3rd",
            "c:/Program Files/Perl/bin/perl.exe,first arg,second,3rd");
        _test(
            "somewhat\\weird \\command\\\"line",
            "somewhat\\weird,\\command\"line");
        _test(
            "foo=\"one two three\"",
            "foo=one two three");
        _test(
            "foo=\\\"one two three\\\"",
            "foo=\"one,two,three\"");
        _test(
            "foo=\"\\\"one two three\\\"\"",
            "foo=\"one two three\"");
        _test(
            "\"somewhat\\weird \\command\\\"line\" bang",
            "somewhat\\weird \\command\"line,bang");
        _test("\"trailing backslash0r\\", "trailing backslash0r\\");
        _test("\"forgotten quote", "forgotten quote");
        
        Assert.assertTrue(CommandLineTokenizer.tokenize("   ").isEmpty());
        Assert.assertEquals(1, CommandLineTokenizer.tokenize("  moo  ").size());
    }
    
    private void _test(String in, String out)
    {        
        List<String> ret = CommandLineTokenizer.tokenize(in);
        Assert.assertFalse(ret.isEmpty());
        
        StringBuffer buf = new StringBuffer();

        for (Iterator<String> i = ret.iterator(); i.hasNext();)
        {
            buf.append(i.next());
            if (i.hasNext()) buf.append(',');
        }
        Assert.assertEquals(out, buf.toString());
    }
}
