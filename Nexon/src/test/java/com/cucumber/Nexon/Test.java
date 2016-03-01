package com.cucumber.Nexon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Test {

	private static WebDriver driver;

	
	@Before
	  public static void setUp() throws Exception {
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();		
		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:8080/wd/hub"), cap);
//		cap.setBrowserName("internetExplorer");
//		cap.setVersion("10");
		
//		System.setProperty("webdriver.ie.driver", "C:/Test/IEDriverServer.exe");	
//		driver = new InternetExplorerDriver();
	    driver.manage().window().maximize();
	    
	  }
	
	
	@Given("^넥슨 홈화면에서 로그인$")
	public void 넥슨_홈화면에서_로그인() throws Throwable {
		//넥슨 웹페이지로 이동
		driver.get("http://www.nexon.com/Home/Game.aspx");  
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	    //로그인
	    
	    driver.findElement(By.id("txtNexonID")).sendKeys("christian999");
	    driver.findElement(By.id("txtPWD")).sendKeys("tkffkels99(");
	    driver.findElement(By.id("btnLogin")).click();
	    
	  //별명, 새쪽지, 캐시 엘리먼트 값 존재유무 확인
	    try{
	    	Thread.sleep(2000);
		    driver.findElement(By.id("btnCCClose")).click();
		    String Nickname = driver.findElement(By.xpath("//*[@id='LoginInfo']/div/a/span/span")).getText();
		    String NewMessage = driver.findElement(By.xpath("//*[@id='LoginInfo']/dl[1]/dt")).getText();
		    String Cash = driver.findElement(By.xpath("//*[@id='LoginInfo']/dl[2]/dt")).getText();  
		    assertEquals(Nickname, "심쪼잔");
		    assertEquals(NewMessage, "새쪽지");
		    assertEquals(Cash, "캐시");
	    }
		    catch(NoSuchWindowException e) {
		    	String Nickname = driver.findElement(By.xpath("//*[@id='LoginInfo']/div/a/span/span")).getText();
			    String NewMessage = driver.findElement(By.xpath("//*[@id='LoginInfo']/dl[1]/dt")).getText();
			    String Cash = driver.findElement(By.xpath("//*[@id='LoginInfo']/dl[2]/dt")).getText();   
			    assertEquals(Nickname, "심쪼잔");
			    assertEquals(NewMessage, "새쪽지");
			    assertEquals(Cash, "캐시");
		  	}
	      	       
	}

	@When("^넥슨 쪽지함으로 이동$")
	public void 넥슨_쪽지함으로_이동() throws Throwable {
		//쪽지함으로 이동
		driver.findElement(By.xpath("//*[@id='LoginInfo']/dl[1]/dd[2]/button")).click();
		Thread.sleep(3000);
		
		Set<String> allWindows = driver.getWindowHandles();
		if(!allWindows.isEmpty()){
			for(String windowId : allWindows){  		  	
		  	try {
		  		if(driver.switchTo().window(windowId).getTitle().substring(0, 3).equals("쪽지함"))
		  		{ 
		  			break;
		  			}
		  		}
		  	catch(NoSuchWindowException e) {
		  		e.printStackTrace();
		  	}
		}	
	}
}

	@When("^넥슨쪽지 내게 보내기$")
	public void 넥슨쪽지_내게_보내기() throws Throwable {
		//쪽지 보내기 화면으로 이동
		Thread.sleep(1000);
		String currentWindowId = driver.getWindowHandle();
		String subWindowHandler = null;
		
	    driver.findElement(By.xpath("//a[@title='넥슨']")).click();
	    Thread.sleep(3000);
	    
	    Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> iterator = allWindows.iterator();
		while (iterator.hasNext()){
			subWindowHandler = iterator.next();
		}
		driver.switchTo().window(subWindowHandler);
 	    
		Thread.sleep(3000);
		
		
		//받는사람 및 내용 입력 후 보내기 
		driver.findElement(By.xpath("//*[@id='G_gnxUserNameTextBoxFormessage_w']")).click();
		driver.findElement(By.xpath("//*[@id='G_gnxUserNameTextBoxFormessage_w']")).sendKeys("심쪼잔");
		driver.findElement(By.xpath("//*[@id='G_gnxSendMessageTextBox_w']")).sendKeys("안녕하세요!");
		
		driver.findElement(By.xpath("//*[@id='G_gnxSendMessageImageButton_w_image']")).click();
		Thread.sleep(1000);
		
		assertTrue(driver.findElement(By.xpath("//*[@id='secSendok']")).isDisplayed());
		driver.findElement(By.xpath("//*[@id='G_gnxWindowCloseImageButton_image']")).click();
		
		driver.switchTo().window(currentWindowId);
		
	}

	@Then("^쪽지 확인 후 삭제$")
	public void 쪽지_확인_후_삭제() throws Throwable {
		//받은 쪽지함에서 내게 보낸 쪽지 확인
	    driver.findElement(By.xpath("//*[@id='m_form']/div/div[2]/ul/li[1]/ul/li[1]/a")).click();
	    Thread.sleep(1000);

	    WebElement FromFriend = driver.findElement(By.xpath("//*[@id='listMboxWrapper0']/div[3]"));
	    WebElement Title = driver.findElement(By.xpath("//*[@id='listMboxWrapper0']/div[4]/label"));
	    WebElement result = driver.findElement(By.id("spanViewCurrentPage"));
	    
	    assertEquals(result.getText(), "(1)");
		assertEquals(FromFriend.getText(), "심쪼잔@넥슨");
		assertEquals(Title.getText(), "안녕하세요!");
		assertTrue(driver.findElement(By.className("view-mbox")).isDisplayed());
		
		//받은 쪽지 삭제
		driver.findElement(By.xpath("//*[@id='general']/a[2]/img")).click();
		Thread.sleep(1000);
		
		Alert alert = driver.switchTo().alert();
		alert.accept(); 
		
		//삭제 확인
		assertEquals(result.getText(), "(0)");
		
	}

	
	
	
	@After
	public void tearDown() throws Exception {
		driver.quit();	
	}
}
