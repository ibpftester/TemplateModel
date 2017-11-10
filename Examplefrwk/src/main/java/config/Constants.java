package config;

import project.ConstantsSpecific;

public class Constants {

    public static final String URL = ConstantsSpecific.URLSpecific;

    public static final int Tempo_Load_URL = ConstantsSpecific.Tempo_Load_URLSpecific;
    public static final int Tempo_Espera = ConstantsSpecific.Tempo_EsperaSpecific;

    public static final String Path_TestData = ConstantsSpecific.Path_TestDataSpecific;
    public static final String Path_OR = ConstantsSpecific.Path_ORSpecific;

    public static final String Path_Drivers = "C://";
    public static final String File_TestData = "DataEngine.xlsx";

    public static final String OptionDefaultMDSelect = ConstantsSpecific.OptionDefaultMDSelectSpecific;

    public static final String PathScreenshots = System.getProperty("user.dir");
    public static final String NamePath = "Evidencias";

    public static final int Col_TestCaseID = 0;
    public static final int Col_TestScenarioID = 1;
    public static final int Col_PageObject = 4;
    public static final int Col_ActionKeyword = 5;
    public static final int Col_Print = 6;
    public static final int Col_DataSet = 7;
    public static final int Col_TestStepResult = 8;

    public static final int Col_RunMode = 2;
    public static final int Col_Result = 3;

    public static final String Sheet_TestSteps = "Test Steps";
    public static final String Sheet_TestCases = "Test Cases";

    public static final String KEYWORD_FAIL = "FAIL";
    public static final String KEYWORD_PASS = "PASS";
}
