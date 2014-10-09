package testapi;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestEvent 
{
  @Parameters({"apiUrl", "apiUser", "apiPass"})
  @Test
  public void testFetchEvent() {
  }
}
