package org.epic.perleditor.editors.perl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.epic.perl.editor.test.BaseTestCase;

import org.junit.*;

public class TestPerlCompletionProcessor extends BaseTestCase
{
    @Test
    public void testModulePrefixRegexp()
    {
        String text1 = "my $x = F1oo_bar::Blah::";
        String text2 = "$y + Foo::";
        String text3 = "foo(Foo_bar::Blah->";
        String text4 = "$test?$abc:Foo->";
        
        Pattern pattern = PerlCompletionProcessor.MODULE_PREFIX_PATTERN;
        
        Assert.assertEquals("F1oo_bar::Blah::", find(pattern, text1));
        Assert.assertEquals("Foo::", find(pattern, text2));
        Assert.assertEquals("Foo_bar::Blah->", find(pattern, text3));
        Assert.assertEquals("Foo->", find(pattern, text4));
    }
    
    @Test
    public void testVarPrefixRegexp()
    {
        String text1 = "my $abc = $y_1->";
        String text2 = "my $abc = $y_1->boo()";
        String text3 = "my $abc = $y_1::";
        String text4 = "my $abc = $y_1::boo()";
        
        Pattern pattern = PerlCompletionProcessor.VAR_PREFIX_PATTERN;
        
        Assert.assertTrue(pattern.matcher(text1).find());
        Assert.assertFalse(pattern.matcher(text2).find());
        Assert.assertTrue(pattern.matcher(text3).find());
        Assert.assertFalse(pattern.matcher(text4).find());
    }
    
    private String find(Pattern pattern, String text)
    {
        Matcher m = pattern.matcher(text);        
        return m.find() ? m.group(0) : null;
    }
}
