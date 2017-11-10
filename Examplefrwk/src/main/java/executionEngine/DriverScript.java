package executionEngine;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import utility.ExcelUtils;
import utility.Log;
import config.ActionKeywords;
import config.Constants;
import project.ActionKeywordsSpecific;

public class DriverScript {

    public static Properties OR;
    public static ActionKeywordsSpecific actionKeywords;
    public static String sActionKeyword;
    public static String sPageObject;
    public static Method method[];
    public static int iTestStep;
    public static int iTestLastStep;
    public static String sTestCaseID;
    public static String sRunMode;
    public static String sData;
    public static String sPrint;
    public static boolean bResult;

    public DriverScript() throws NoSuchMethodException, SecurityException {
        actionKeywords = new ActionKeywordsSpecific();
        method = actionKeywords.getClass().getMethods();
    }

    public static void main(String[] args) throws Exception {
        ExcelUtils.setExcelFile(Constants.Path_TestData);
        DOMConfigurator.configure("log4j.xml");
        String Path_OR = Constants.Path_OR;
        FileInputStream fs = new FileInputStream(Path_OR);
        OR = new Properties(System.getProperties());
        OR.load(new InputStreamReader(fs, Charset.forName("UTF-8")));

        DriverScript startEngine = new DriverScript();
        startEngine.execute_TestCase();
    }

    private void execute_TestCase() throws Exception {
        ExcelUtils.clearCellResults(Constants.Col_TestStepResult, Constants.Sheet_TestSteps);
        ExcelUtils.clearCellResults(Constants.Col_Result, Constants.Sheet_TestCases);
        int iTotalTestCases = ExcelUtils.getRowCount(Constants.Sheet_TestCases);
        int controllerFail = 0;
        for (int iTestcase = 1; iTestcase < iTotalTestCases; iTestcase++) {
            bResult = true;
            sTestCaseID = ExcelUtils.getCellData(iTestcase, Constants.Col_TestCaseID, Constants.Sheet_TestCases);
            sRunMode = ExcelUtils.getCellData(iTestcase, Constants.Col_RunMode, Constants.Sheet_TestCases);
            if (sRunMode.equals("Yes")) {
                iTestStep = ExcelUtils.getRowContains(sTestCaseID, Constants.Col_TestCaseID, Constants.Sheet_TestSteps);
                iTestLastStep = ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
                Log.startTestCase(sTestCaseID);
                bResult = true;
                for (; iTestStep < iTestLastStep; iTestStep++) {
                    sActionKeyword = ExcelUtils.getCellData(iTestStep, Constants.Col_ActionKeyword,
                        Constants.Sheet_TestSteps);
                    sPageObject = ExcelUtils.getCellData(iTestStep, Constants.Col_PageObject,
                        Constants.Sheet_TestSteps);
                    sData = ExcelUtils.getCellData(iTestStep, Constants.Col_DataSet, Constants.Sheet_TestSteps);
                    sPrint = ExcelUtils.getCellData(iTestStep, Constants.Col_Print, Constants.Sheet_TestSteps);
                    execute_Actions();
                    if (bResult == false) {                        
                        if (controllerFail < 3){
                            if (controllerFail == 2){
                                ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestcase, Constants.Col_Result,
                                    Constants.Sheet_TestCases);
                                controllerFail = 0;
                            }
                            else{
                                controllerFail++;
                                iTestcase--;
                            }
                        }
                        Log.endTestCase(sTestCaseID);
                        break;
                    }
                }
                if (bResult == true) {
                    ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestcase, Constants.Col_Result,
                        Constants.Sheet_TestCases);
                    controllerFail = 0;
                    Log.endTestCase(sTestCaseID);
                }
            }
        }
    }

    private static void execute_Actions() throws Exception {
        for (int i = 0; i < method.length; i++) {
            if (method[i].getName().equals(sActionKeyword)) {
                method[i].invoke(actionKeywords, sPageObject, sData, sPrint);
                if (bResult == true) {
                    ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestStep, Constants.Col_TestStepResult,
                        Constants.Sheet_TestSteps);
                    break;
                } else {
                    ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestStep, Constants.Col_TestStepResult,
                        Constants.Sheet_TestSteps);
                    ActionKeywords.closeBrowser("","","");
                    break;
                }
            }
        }
    }

}
