package org.epic.perleditor.editors.util;

import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.util.Locale;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.easymock.MockControl;
import org.epic.perl.editor.test.BaseTestCase;
import org.epic.perl.editor.test.Log;

import org.junit.*;

public class TestPerlValidator extends BaseTestCase
{
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
    }
    
    private IResource    prepareMockResource()
    {
        MockControl cMockProject = MockControl.createControl(IProject.class);
        IProject mockProject = (IProject) cMockProject.getMock();
        mockProject.getLocation();
        cMockProject.setReturnValue(new Path("."), MockControl.ONE_OR_MORE);
        cMockProject.replay();
        
        MockControl cMockResource = MockControl.createControl(IResource.class);
        IResource mockResource = (IResource) cMockResource.getMock();
        mockResource.getProject();
        cMockResource.setReturnValue(mockProject, MockControl.ONE_OR_MORE);
        cMockResource.replay();

        return mockResource;
    }
    
    @Test
    public void testBrokenPipe() throws Exception
    {
        Assume.assumeThat("Check that we are runnning on en_US system locale", 
                        Locale.getDefault().toString(), startsWith(Locale.ENGLISH.toString()));
        
        IResource   mockResource = prepareMockResource();
        
        // We expect a "broken pipe" IOException when feeding a large
        // piece of source code containing an error in the header to
        // the Perl interpreter:
        PerlValidatorStub validator = new PerlValidatorStub();
        
        try
        {         
            validator.validate(
                mockResource,
                validator.readSourceFile(getFile("test.in/Tool.pm").getAbsolutePath(), null));
            
            Assert.assertTrue(PerlValidatorStub.gotBrokenPipe);
        }
        finally
        {
            validator.dispose();
        }
    }
    
    @Test
    public void testBrokenPipeNonEnLocale() throws Exception
    {
        Assume.assumeThat("Check that we are runnning on non en_US system locale", 
                        Locale.getDefault().toString(), not(startsWith(Locale.ENGLISH.toString())));

        IResource   mockResource = prepareMockResource();
        
        // We expect a "broken pipe" IOException when feeding a large
        // piece of source code containing an error in the header to
        // the Perl interpreter:
        PerlValidatorStub validator = new PerlValidatorStub();
        
        try
        {         
            validator.validate(
                mockResource,
                validator.readSourceFile(getFile("test.in/Tool.pm").getAbsolutePath(), null));

            Assert.fail("We'd never reach this point. Non \"en\" locale, and no RuntimeException was thrown");
        }
        catch (RuntimeException e)
        {
            // in case system is running non-en locale, IOException wording is reported via localized message
            // the best we can do here is to check that at least IOException was thrown from validate()
            
            Assert.assertThat("Caught RuntimeException doesn't have initialized cause", e.getCause(), notNullValue());
            Assert.assertThat("Broken pipe not reported via IOException",
                            (IOException) e.getCause(), isA(java.io.IOException.class));
        }
        finally
        {
            validator.dispose();
        }
    }
    
    @Test
    public void testParsedErrorLine()
    {
        String line =
            "Subroutine foo redefined at /blah/X.pm line 65." +
            " at /foo/Bar.pm line 22.";
        PerlValidatorBase.ParsedErrorLine pline =
            new PerlValidatorBase.ParsedErrorLine(line, new Log());
        
        Assert.assertEquals(
            "Subroutine foo redefined at /blah/X.pm line 65.",
            pline.getMessage());
        Assert.assertEquals(22, pline.getLineNumber());
        
        // test case for bug #1307071
        line = "syntax error at bug1307071.pl line 9, near \"if {\"";
        pline = new PerlValidatorBase.ParsedErrorLine(line, new Log());
        Assert.assertEquals("syntax error", pline.getMessage()); // could be better...
        Assert.assertEquals(9, pline.getLineNumber());
    }
}
