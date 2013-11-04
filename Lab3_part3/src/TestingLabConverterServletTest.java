import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;

public class TestingLabConverterServletTest {
	
	public static TestingLabConverterServlet s = null;
	public static MockHttpServletRequest request = null;
	public static MockHttpServletResponse response = null;
	
	public static String getExpectedStringMockedWeather(String f, String c){
		String expectedString = new String ();
		
		expectedString ="<html><head><title>Temperature Converter Result</title></head><body><h2>"
			    		+f+" Farenheit = "+c+" Celsius </h2>\n"
				+ "<p><h3>The temperature in Austin is 451 degrees Farenheit</h3>\n"
				+ "</body></html>\n";
		
		return expectedString;
	}
	
	@Before
	public void setUp() throws Exception {
		s = new TestingLabConverterServlet();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
		CityTemperatureServiceProvider.turnOnMockMode();
	}

	@After
	public void tearDown() throws Exception {
		s = null;
		request = null;
		response = null;
	}

	@Test
	public void testDoPost() throws Exception {

		String inputTempF = new String("212");
		String expectedOutputTempC = new String ("100");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}

	@Test
	public void testOneSigFigWhenLessThanZeroF() throws Exception {

		//Spec under Test: Temperature results should be 2 places of precision for temperatures 
		//from 0 to 212 degrees Farenheit, inclusive, and 1 place of precision otherwise.
		//Test Condition:  Check for 1 sig fig when less than 0F
		
		String inputTempF = new String("-1");
		String expectedOutputTempC = new String ("-18.3");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}

	@Test
	public void testTwoSigFigWhenZeroF() throws Exception {

		//Spec under Test: Temperature results should be 2 places of precision for temperatures 
		//from 0 to 212 degrees Farenheit, inclusive, and 1 place of precision otherwise.
		//Test Condition:  Check for 2 sig figs when equal to 0F
		
		String inputTempF = new String("0");
		String expectedOutputTempC = new String ("-17.78");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testTwoSigFigWhenBtwnZeroAndTwoTwelve() throws Exception {

		//Spec under Test: Temperature results should be 2 places of precision for temperatures 
		//from 0 to 212 degrees Farenheit, inclusive, and 1 place of precision otherwise.
		//Test Condition:  Check for 2 sig figs when temperature is between 0F and 212F
		
		String inputTempF = new String("100");
		String expectedOutputTempC = new String ("37.78");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testTwoSigFigWhenTwoTwelve() throws Exception {

		//Spec under Test: Temperature results should be 2 places of precision for temperatures 
		//from 0 to 212 degrees Farenheit, inclusive, and 1 place of precision otherwise.
		//Test Condition:  Check for 2 sig figs when equal to 212F
		
		String inputTempF = new String("212");
		String expectedOutputTempC = new String ("100.00");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testOneSigFigWhenGreaterThanTwoTwelve() throws Exception {

		//Spec under Test: Temperature results should be 2 places of precision for temperatures 
		//from 0 to 212 degrees Farenheit, inclusive, and 1 place of precision otherwise.
		//Test Condition:  Check for 1 sig figs when greater than 212F
		
		String inputTempF = new String("213");
		String expectedOutputTempC = new String ("100.6");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testFloatingPointInputCapable() throws Exception {

		//Spec under Test: Temperature inputs are floating point numbers in decimal
		//notation (i.e., 97 or -3.14, but not 9.73E2)
		//Test Condition:  Check for ability to take in floating point temperatures. 
		
		String inputTempF = new String("90.7");
		String expectedOutputTempC = new String ("32.61");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testNotScientificNotationCapable() throws Exception {

		//Spec under Test: Temperature inputs are floating point numbers in decimal
		//notation (i.e., 97 or -3.14, but not 9.73E2)
		//Test Condition:  Check for ability to NOT take in scientific notation temperatures. 
		
		String inputTempF = new String("1.73E2");
		String expectedOutputTempC = new String ("78.33");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertFalse(expectedResult.equals(response.getOutputStreamContents()));
	}
	
	@Test
	public void testReturnsWithNumberFormatExceptionWhenNecessary() throws Exception {

		//Spec under Test: Temperature inputs that are not valid should return Got a 
		//NumberFormatException on (input string).
		//Test Condition:  Check for ability to return message stating: 
		//"Got a NumberFormatException on (input)".  Assume that hypertext and 
		//other padding can be ignored as long as the above message is embedded in the
		//hypertext. 
		
		String inputTempF = new String("ninety");
		
		String expectedResult = "Got a NumberFormatException on ninety";

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertTrue(response.getOutputStreamContents().contains(expectedResult));
	}
	
	@Test
	public void testCapInsensitiveForParameterNames() throws Exception {

		//Spec under Test: The temperature parameter should be passed in as 
		//farenheitTemperature=-40 in the URL; the parameter “farenheitTemperature” 
		//should be case insensitive.
		//Test Condition:  Check for ability to take in farenheitTemperature with various
		//capitalizations. 
		
		String inputTempF = new String("90.7");
		String expectedOutputTempC = new String ("32.61");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("FarenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doPost(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testDoGet() throws Exception {

		// CityTemperatureServiceProvider.turnOffMockMode();
		String inputTempF = new String("212");
		String expectedOutputTempC = new String ("100");
		
		String expectedResult = getExpectedStringMockedWeather(inputTempF, expectedOutputTempC);

		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doGet(request, response);
		response.verify();
		assertEquals(expectedResult, response.getOutputStreamContents());
	}
	
	@Test
	public void testLiveWeatherFeedForReasonableResult() throws Exception {

		//CityTemperatureServiceProvider provides data with different format (both 
		//overall content of text and temperature scale data of Kelvin vs. Fahrenheit)
		//than anticipated by TestingLabConverterServlet.  I am assuming that we are not
		//supposed to change either TestingLabConverterServlet or CityTemperatureServiceProvider,
		//so this test illustrates the disconnect in expectations between data source and the servlet.
		
		//Activate CityTemperatureService to query URL for weather info. 
		CityTemperatureServiceProvider.turnOffMockMode();
		
		String inputTempF = new String("212");
		//String expectedOutputTempC = new String ("100");
		
		request.setupAddParameter("farenheitTemperature", inputTempF);
		response.setExpectedContentType("text/html");
		s.doGet(request, response);
		response.verify();
		
		String outputString = response.getOutputStreamContents();
		
		String anchor1 = "The temperature in Austin is ";
		String anchor2 = " degrees Farenheit</h3>";
		int offset = outputString.indexOf(anchor1) + anchor1.length();
		int end = outputString.indexOf(anchor2);
		
		String tempStringFromLiveWeatherFeed = outputString.substring(offset, end);
		
		Double tempFromLiveWeatherFeed = Double.parseDouble(tempStringFromLiveWeatherFeed);
		
		assertTrue("Ensure Temp is within temperature range of (-40F to 130F)", 
				(tempFromLiveWeatherFeed<130)&&(tempFromLiveWeatherFeed>-40));

	}

}
