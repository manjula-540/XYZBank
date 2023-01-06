package com.xyzbank.tests;





import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.xyzbank.pages.XYZBankPages;




public class AddDeleteVeifyCustomersTest extends XYZBankPages {


	@Test
	@Parameters({"loginuser"})
	public void addCustomersUnderManager(String userType) throws Exception {
		logger=extent.createTest("Login to XYZ Bank");
		launchApplication();
		clickOnManagerOrCustomerLoginBtn(userType);
		verifyAddCustomerButtonPresenceAndClick();
		enterAddCustomerFieldDetails();
		clickOnCustomerTab();
		getTableData();
		verifyAddedCustomersAvailableInTheTable();
		deleteSpeceficCustomers();
		verifyDeletedCustomersAreNotAvailableInTheTable();
		
	}	
	
	}
