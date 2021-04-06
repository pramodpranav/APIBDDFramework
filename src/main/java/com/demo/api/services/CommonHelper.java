package com.demo.api.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.demo.api.commonutils.Config;
import com.demo.api.pojoclasses.request.CreateUser;

public class CommonHelper {

	/**
	 * 
	 * @param length
	 * @return
	 */
	public static long generateRandomNumber(int length) {
		long randomNumber = 1;
		int retryCount = 1;

		// retryCount added for generating specified length's number
		while (retryCount > 0) {
			String strNum = Double.toString(Math.random());
			strNum = strNum.replace(".", "");

			if (strNum.length() > length) {
				strNum = strNum.substring(0, length);
			} else {
				int remainingLength = length - strNum.length() + 1;
				randomNumber = generateRandomNumber(remainingLength);
				strNum = strNum.concat(Long.toString(randomNumber));
			}

			randomNumber = Long.parseLong(strNum);

			if (String.valueOf(randomNumber).length() < length) {
				retryCount++;
			} else {
				retryCount = 0;
			}

		}

		return randomNumber;
	}
	
	public static String generateRandomAlphabetsString(int length) {
		Random rd = new Random();
		String aphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			sb.append(aphaNumericString.charAt(rd.nextInt(aphaNumericString.length())));
		}

		return sb.toString();
	}
	

	public static String replaceArgumentsWithRunTimeProperties(Config testConfig, String input) {
		String value = null;
		if(input.contains("{$set:"))
			return input;
		if (input.contains("{$")) {
			int index = input.indexOf("{$");
			String key = input.substring(index + 2, input.indexOf("}", index + 2));
			value = testConfig.getRunTimeProperty(key);
			input = input.replace("{$" + key + "}", value);
			return replaceArgumentsWithRunTimeProperties(testConfig, input);
		} else {
			return input;
		}

	}
	public static void verifyValueOfUserInfo(Config testConfig,CreateUser expectedUser,CreateUser actualUser) {
		if(expectedUser.getId().equals(actualUser.getId())) {
			LoggerHelper.logPass("[Pass] Verify id :" + expectedUser.getId(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify id, expected id : " + expectedUser.getId() + " Actual id : " + actualUser.getId(), testConfig);
		}
		
		if(expectedUser.getUsername().equals(actualUser.getUsername())) {
			LoggerHelper.logPass("[Pass] Verify user name :" + expectedUser.getUsername(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify user name, expected user name : " + expectedUser.getUsername() + " Actual user name : " + actualUser.getUsername(), testConfig);
		}
		
		if(expectedUser.getFirstName().equals(actualUser.getFirstName())) {
			LoggerHelper.logPass("[Pass] Verify firstname :" + expectedUser.getFirstName(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify first name, expected first name : " + expectedUser.getFirstName() + " Actual id : " + actualUser.getFirstName(), testConfig);
		}
		
		if(expectedUser.getLastName().equals(actualUser.getLastName())) {
			LoggerHelper.logPass("[Pass] Verify last name :" + expectedUser.getLastName(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify last name, expected last name : " + expectedUser.getLastName() + " Actual last name : " + actualUser.getLastName(), testConfig);
		}
		
		if(expectedUser.getEmail().equals(actualUser.getEmail())) {
			LoggerHelper.logPass("[Pass] Verify email :" + expectedUser.getEmail(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify email, expected email : " + expectedUser.getEmail() + " Actual email : " + actualUser.getEmail(), testConfig);
		}
		
		if(expectedUser.getPassword().equals(actualUser.getPassword())) {
			LoggerHelper.logPass("[Pass] Verify password :" + expectedUser.getPassword(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify password, expected password : " + expectedUser.getPassword() + " Actual password : " + actualUser.getPassword(), testConfig);
		}
		if(expectedUser.getUserStatus().equals(actualUser.getUserStatus())) {
			LoggerHelper.logPass("Verify user status :" + expectedUser.getUserStatus(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify user status, expected user status : " + expectedUser.getUserStatus() + " Actual user status : " + actualUser.getUserStatus(), testConfig);
		}
		
		if(expectedUser.getPhone().equals(actualUser.getPhone())) {
			LoggerHelper.logPass("Verify password :" + expectedUser.getPhone(), testConfig);
		}
		else {
			LoggerHelper.logFail("[Failed] to Verify phone, expected phone : " + expectedUser.getPhone() + " Actual phone : " + actualUser.getPhone(), testConfig);
		}
	}
	
	public static String readFile(String filePath) throws IOException {
	    File file = new File(filePath);
	    return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
	}
	
	public static List<List<String>> readxlsxFile(String filePath, String sheetName) throws IOException {

		String filename = filePath;
		FileInputStream fis = null;
		List<List<String>> listofRowData = new ArrayList<List<String>>();
		try {
			if (filename.endsWith(".xlsx")) {
				XSSFWorkbook workbook = null;
				XSSFSheet sheet = null;

				fis = new FileInputStream(filename);

				workbook = new XSSFWorkbook(fis);
				sheet = workbook.getSheet(sheetName);
				Iterator<Row> rows = sheet.rowIterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					List<String> data = new ArrayList<String>();
					for (int z = 0; z < row.getLastCellNum(); z++) {
						String str = convertXSSFCellToString(row.getCell(z));
						data.add(str);
					}
					listofRowData.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			fis.close();
		}
		return listofRowData;
	}

	private static String convertXSSFCellToString(XSSFCell cell) {
		String value = null;
		try {
			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				value = Double.toString(cell.getNumericCellValue());
			} else if (cell.getCellTypeEnum() == CellType.STRING) {
				value = cell.getRichStringCellValue().toString();
			} else if (cell.getCellTypeEnum() == CellType._NONE) {
				value = "";
			} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
				value = Boolean.toString(cell.getBooleanCellValue());
			} else if (cell.getCellTypeEnum() == CellType.BLANK) {
				value = "";
			} else if (cell.getCellTypeEnum() == CellType.ERROR) {
				value = "";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}
}
