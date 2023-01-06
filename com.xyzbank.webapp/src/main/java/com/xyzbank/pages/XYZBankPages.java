package com.xyzbank.pages;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class XYZBankPages {

	WebDriver driver = null;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public WebDriverWait wait;

	By addcustomerbtn = By.xpath("//button[@type='submit']");
	By addcustomertab = By.xpath("//button[contains(@ng-click,'addCust()')]");
	By customerstab = By.xpath("//*[contains(@class,'border box')]//button[contains(@ng-click,'showCust')]");
	By listofrowsintable = By.xpath("//table/tbody/tr");
	

	@BeforeSuite
	public static void startTest() {
		try {
			ExtentHtmlReporter reporter = new ExtentHtmlReporter(
					System.getProperty("user.dir") +"/test-output/extentreports/XYZBankAplnReports.html");
			extent = new ExtentReports();
			extent.attachReporter(reporter);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	
	public void launchApplication() {
		logger.info("Login into XYZ Bank Application");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
		driver.manage().window().maximize();
		wait=new WebDriverWait(driver, 30);
	}
	

	public void clickOnManagerOrCustomerLoginBtn(String ManagerOrCustomer) {

		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//button[contains(@class,'btn-primary') and contains(text(),'"+ ManagerOrCustomer +"')]")));
			WebElement managerloginbtnele = driver.findElement(By.xpath(
					"//button[contains(@class,'btn-primary') and contains(text(),'"+ ManagerOrCustomer +"')]"));
			if (managerloginbtnele.isEnabled()) {
				logger.info(ManagerOrCustomer +" login button displayed ");
				managerloginbtnele.click();
				logger.pass("Clicked on "+ ManagerOrCustomer +" login button");
			}
		} catch (Exception ex) {
			logger.fail(ManagerOrCustomer +" login button not displayed ");

		}

	}

	public void verifyAddCustomerButtonPresenceAndClick() {
		try {

			wait.until(ExpectedConditions.elementToBeClickable(addcustomertab));
			WebElement addcustomerbtnele = driver.findElement(addcustomertab);
			if (addcustomerbtnele.isDisplayed() && addcustomerbtnele.isEnabled()) {
				logger.info("AddCustomer tab is displayed ");
				driver.findElement(addcustomertab).click();
				logger.pass("Clicked on AddCustomer tab button");
			}
		} catch (Exception ex) {
			logger.fail(" Add Customer Login Button is not displayed"+ ex.getMessage());
		}
	}

	public String verifyCustomerAddedSuccessfullyAlertBoxAndAcceptIt() {
		String alertMsg = null;
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			logger.pass(" Customer Added Successfully Alert box displayed ");
			alertMsg = driver.switchTo().alert().getText();
			Thread.sleep(400);
			logger.pass("Alert Message: "+ alertMsg);
			driver.switchTo().alert().accept();
			logger.info(" Customer Added Successfully Alert box closed ");
		} catch (Exception ex) {

			logger.fail("Customer Added Successfully Alert box not displayed "+ ex.getMessage());
		}

		return alertMsg;

	}

	public void enterAddCustomerFieldDetails() throws Exception {
		List<String> labels = Arrays.asList("First", "Last", "Post");
		logger.info("  Adding Customers  ");
		for (Customer cust : inputData()) {
			for (String labeltext : labels) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//form[@name='myForm']//descendant::label[contains(text(),'"+ labeltext
								+"')]/following-sibling::input")));
				WebElement textbox = driver
						.findElement(By.xpath("//form[@name='myForm']//descendant::label[contains(text(),'"+ labeltext
								+"')]/following-sibling::input"));
				if (textbox.getAttribute("placeholder").contains("First Name")) {
					textbox.sendKeys(cust.getFirstName());
				} else if (textbox.getAttribute("placeholder").contains("Last Name")) {
					textbox.sendKeys(cust.getLastName());

				} else if (textbox.getAttribute("placeholder").contains("Post Code")) {
					textbox.sendKeys(cust.getPostCode());

				}
			}
			logger.info(" Click on AddCustomer button ");
			wait.until(ExpectedConditions.elementToBeClickable(addcustomerbtn));
			driver.findElement(addcustomerbtn).click();
			Thread.sleep(200);
			logger.pass(" Clicked on AddCustomer button ");
			verifyCustomerAddedSuccessfullyAlertBoxAndAcceptIt();
		}
		logger.info("  Added Customers Successfully ");
	}

	static class Customer {

		private String firstName;
		private String lastName;
		private String postCode;

		public Customer(String firstName, String lastName, String postCode) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.postCode = postCode;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getPostCode() {
			return postCode;
		}

	}

	public static List<Customer> inputData() {
		List<Customer> listCustomers = new ArrayList<>();
		listCustomers.add(new Customer("Christopher", "Connely", "L789C349"));
		listCustomers.add(new Customer("Frank", "Christopher", "A897N450"));
		listCustomers.add(new Customer("Christopher", "Minka", "M098Q585"));
		listCustomers.add(new Customer("Connely", "Jackson", "L789C349"));
		listCustomers.add(new Customer("Jackson", "Frank", "L789C349"));
		listCustomers.add(new Customer("Minka", "Jackson", "A897N450"));
		listCustomers.add(new Customer("Jackson", "Connely", "L789C349"));
		listCustomers.add(new Customer("Lawrence", "Zimmerman", "L789C349"));
		listCustomers.add(new Customer("Mariotte", "Tova", "L789C349"));

		return listCustomers;

	}

	

	public List<Customer> getTableData() {
		 List<Customer> listCustomer = new ArrayList<>();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(listofrowsintable));
		List<WebElement> listOfRows = driver.findElements(listofrowsintable);

		for (int row = 1; row <= listOfRows.size(); row++) {
			List<WebElement> listOfColmns = driver.findElements(By.xpath("//table/tbody/tr["+ row +"]"));
			if (listOfColmns.size() > 0) {
				for (int colmn = 1; colmn < 4; colmn++) {
					listCustomer.add(new Customer(getColumsData(row, 1), getColumsData(row, 2), getColumsData(row, 3)));

				}
			} else {

			}
		}
		return listCustomer;

	}

	public String getColumsData(int row, int colum) {

		return driver.findElement(By.xpath("//table/tbody/tr["+ row +"]/td["+ colum +"]")).getText();

	}

	public void clickOnCustomerTab() throws Exception {
		try {
			logger.info(" Click on Customers Tab ");
			wait.until(ExpectedConditions.elementToBeClickable(customerstab));
			WebElement managerBtnLogin = driver.findElement(customerstab);
			if (managerBtnLogin.isDisplayed() && managerBtnLogin.isEnabled()) {
				driver.findElement(customerstab).click();
				logger.pass(" Clicked on Customers Tab ");
			}
		} catch (Exception ex) {
			logger.fail("Customers Tab is not displayed");

		}
	}

	public boolean verifyAddedCustomersAvailableInTheTable() {
		boolean s = false;
		logger.info("  Verify Added Customers are available under Customers Table ");
		for (Customer cust : inputData()) {
			 List<Customer> listCustomer=getTableData();
			s = listCustomer.stream()
					.filter(o -> o.getFirstName().equals(cust.getFirstName())
							&& o.getLastName().equals(cust.getLastName()) && o.getPostCode().equals(cust.getPostCode()))
					.findFirst().isPresent();

			if (s) {
				logger.pass(" Verified Added Customers are available under Customers Tab Succsessfully ");
				return s;

			}
		}
		return s;
	}

	public static List<Customer> inputDataToDeleteSpeceficCustomers() {
		List<Customer> listCustomersDelete = new ArrayList<>();
		listCustomersDelete.add(new Customer("Christopher", "Connely", "L789C349"));
		listCustomersDelete.add(new Customer("Jackson", "Frank", "L789C349"));
		return listCustomersDelete;
	}

	public void deleteSpeceficCustomers() throws Exception {
		String actualfirstname;
		String actuallastname;
		String actualpostcode;
		logger.info("  Delete few Customers under Customers Tab ");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(listofrowsintable));
		List<WebElement> listOfRows = driver.findElements(listofrowsintable);

		for (Customer cust : inputDataToDeleteSpeceficCustomers()) {
			for (int row = 1; row <= listOfRows.size(); row++) {
				int colmn = 1;
				actualfirstname = driver.findElement(By.xpath("//table/tbody/tr["+ row +"]/td["+ colmn +"]"))
						.getText();
				actuallastname = driver.findElement(By.xpath("//table/tbody/tr["+ row +"]/td["+ (colmn + 1) +"]"))
						.getText();
				actualpostcode = driver.findElement(By.xpath("//table/tbody/tr["+ row +"]/td["+ (colmn + 2) +"]"))
						.getText();

				System.out.println("actualfirstname "+ actualfirstname +" actuallastname "+ actuallastname
						+" actualpostcode "+ actualpostcode);
				System.out.println("expectedfirstname "+ cust.getFirstName() +" expectedlastname "
						+ cust.getLastName() +"  expectedpostcode "+ cust.getPostCode());

				if (cust.getFirstName().equalsIgnoreCase(actualfirstname)
						&& cust.getLastName().equalsIgnoreCase(actuallastname)
						&& cust.getPostCode().equalsIgnoreCase(actualpostcode)) {
					WebElement deleteBtn = driver
							.findElement(By.xpath("//*[contains(@class,'ng-scope')]//table/tbody/tr["+ row
									+"]/td[5]/button[text()='Delete']"));
					wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
					deleteBtn.click();
					row = listOfRows.size() + 1;// to fail the inner for loop
				}
			}
		}
		logger.info("  Deleted specefic Customers under Customers Tab ");

	}

	public boolean verifyDeletedCustomersAreNotAvailableInTheTable() throws InterruptedException {
		boolean s = false;
		List<Customer> listCustomer=getTableData();
		logger.info("  Verify Deleted Customers are not available under Customers Table ");
		for (Customer cust : inputDataToDeleteSpeceficCustomers()) {
			s = listCustomer.stream()
					.filter(o -> o.getFirstName().equals(cust.getFirstName())
							&& o.getLastName().equals(cust.getLastName()) && o.getPostCode().equals(cust.getPostCode()))
					.findFirst().isPresent();
			if (!s) {
				logger.pass(" Verified Deleted Customers are not available under Customers Table as expected ");
				return s;

			}
		}
		return s;

	}

	public void tearDown() {
		driver.quit();
	}

	@AfterClass
	public void closeBrowser() {
		tearDown();
	}

	@AfterMethod
	public static void endTest() {
		extent.flush();
	}
	
}
