package org.talend.services.demos.client;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

import org.talend.services.demos.common.Utils;
import org.talend.services.demos.library._1_0.Library;
import org.talend.services.demos.library._1_0.SeekBookError;
import org.talend.types.demos.library.common._1.ListOfBooks;
import org.talend.types.demos.library.common._1.SearchFor;

/**
 * The Class LibraryTester.
 */
public class LibraryTester {

    /** The Library proxy will be injected either by spring or by a direct call to the setter  */
	Library library;
    
    /**
     * Gets the library.
     *
     * @return the library
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Sets the library.
     *
     * @param library the new library
     */
    public void setLibrary(Library library) {
        this.library = library;
    }

    /**
     * Test request response positive.
     *
     * @throws SeekBookError the seek book error
     */
    public void testRequestResponsePositive() throws SeekBookError {
        // Test the positive case where author(s) are found and we retrieve
        // a list of books
    	System.out.println("***************************************************************");        	
    	System.out.println("*** Request-Response operation ********************************");
    	System.out.println("***************************************************************");    
        System.out.println("\nSending request for authors named Icebear");
        SearchFor request = new SearchFor();
        request.getAuthorLastName().add("Icebear");
        ListOfBooks response = library.seekBook(request);
        System.out.println("\nResponse received:");
        Utils.showBooks(response);

        TestCase.assertEquals(1, response.getBook().size());
        TestCase.assertEquals("Icebear", response.getBook().get(0).getAuthor().get(0).getLastName());    	
    }
    
    /**
     * Test request response business fault.
     *
     * @throws SeekBookError the seek book error
     */
    @SuppressWarnings("unused")
    public void testRequestResponseBusinessFault() throws SeekBookError {
    	
        // Test for an unknown Customer name and expect the NoSuchCustomerException
    	System.out.println("***************************************************************");          
        System.out.println("*** Request-Response operation with Business Fault ************");
        System.out.println("***************************************************************");    
        try {
        	SearchFor request = new SearchFor();
            System.out.println("\nSending request for authors named Grizzlybear");
            request.getAuthorLastName().add("Grizzlybear");
            ListOfBooks response = library.seekBook(request);
            TestCase.fail("We should get a SeekBookError here");
        } catch (SeekBookError e) {
            TestCase.assertNotNull("FaultInfo must not be null", e.getFaultInfo());
            TestCase.assertEquals("No book available from author Grizzlybear",
            		e.getFaultInfo().getException().get(0).getExceptionText());
            
            System.out.println("\nSeekBookError exception was received as expected:\n");
            
            Utils.showSeekBookError(e);

        }   	
    }    
    
    /**
     * Test oneway positive.
     *
     * @throws SeekBookError the seek book error
     */
    public void testOnewayPositive() throws SeekBookError {
    	
        // The implementation of updateCustomer is set to sleep for some seconds. 
        // Still this method should return instantly as the method is declared
        // as a one way method in the WSDL
        
    	System.out.println("***************************************************************");          
        System.out.println("*** Oneway operation ******************************************");
        System.out.println("***************************************************************");
        
        String isbnNumber = "111-22222";
        Date birthDate = (new GregorianCalendar(101, Calendar.MARCH, 5)).getTime();
        String zip = "12345";
        Date borrowed = new Date();
        
        System.out.println("Sending createLending request with parameters:");
        Utils.showLendingRequest(isbnNumber, birthDate, zip, borrowed);
        
        library.createLending(isbnNumber, birthDate, zip, borrowed);	
    }    
    
    
    
	/**
	 * Test library.
	 *
	 * @throws SeekBookError the seek book error
	 */
	public void testLibrary() throws SeekBookError {

    	// Positive TestCase for Request-Response operation	
		testRequestResponsePositive();
    	
    	// Negative TestCase for Request-Response operation (with Business Fault)
    	// testRequestResponseBusinessFault();
    	
    	// Positive TestCase for Onway operation
    	testOnewayPositive();
        
        System.out.println("***************************************************************");
        System.out.println("*** All calls were successful *********************************");
        System.out.println("***************************************************************");
        
    }

}
