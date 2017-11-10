package project;

import static executionEngine.DriverScript.OR;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import config.ActionKeywords;
import config.Constants;
import executionEngine.DriverScript;
import utility.Common;
import utility.DateTimeFunctions;
import utility.Log;
import utility.ScreenCapture;

public class ActionKeywordsSpecific extends ActionKeywords {

    public static void validateGrid(String object, String data, String print) {
        try {
            List<String> colunasGrid = Arrays.asList(OR.getProperty(object).split("\\s*;\\s*"));
            List<String> valoresCampos = Arrays.asList(OR.getProperty(data).split("\\s*;\\s*"));
            String valueQtdeRegistros = driver.findElement(By.xpath(OR.getProperty("lbl_grdQtdeRegistros"))).getText();
            int qtdeRegistros = Integer.parseInt(
                valueQtdeRegistros.substring(valueQtdeRegistros.lastIndexOf("de") + 3, valueQtdeRegistros.length()));

            if (qtdeRegistros > 0) {
                for (int i = 0; i < colunasGrid.size(); i++) {
                    WebElement elementWebField = driver.findElement(By.xpath(OR.getProperty(valoresCampos.get(i))));
                    String elementTypeField = elementWebField.getTagName();

                    if (elementTypeField.contains("input") || elementTypeField.contains("textarea")) {
                        if (elementWebField.getAttribute("value").length() > 0) {
                            for (int j = 0; j < qtdeRegistros; j++) {
                                assertTrue(Common.getXPathRowGrid(colunasGrid.get(i), j, driver).getText()
                                    .contains(elementWebField.getAttribute("value")));
                                Common.wait(1000);
                            }
                        }
                    }

                    else if (elementTypeField.contains("span")) {
                        if (elementWebField.getText().length() > 0) {
                            for (int j = 0; j < qtdeRegistros; j++) {
                                if (!elementWebField.getText().equals(Constants.OptionDefaultMDSelect)) {
                                    assertTrue(Common.getXPathRowGrid(colunasGrid.get(i), j, driver).getText()
                                        .contains(elementWebField.getText()));
                                }
                                Common.wait(1000);
                            }
                        }
                    }

                    else if (elementTypeField.contains("checkbox")) {
                        String valueOfCheckbox = "Não";
                        if (elementWebField.getAttribute("aria-checked").equals("true")) {
                            valueOfCheckbox = "Sim";
                        }

                        for (int j = 0; j < qtdeRegistros; j++) {
                            assertTrue(Common.getXPathRowGrid(colunasGrid.get(i), j, driver).getText()
                                .equals(valueOfCheckbox));
                            Common.wait(1000);
                        }
                    }

                    else if (elementTypeField.contains("select")) {
                        if (elementWebField.getAttribute("value") != null) {
                            if (elementWebField.getAttribute("value").length() > 0) {
                                for (int j = 0; j < qtdeRegistros; j++) {
                                    assertTrue(Common.getXPathRowGrid(colunasGrid.get(i), j, driver).getText()
                                        .equals(elementWebField.getAttribute("value")));
                                    Common.wait(1000);
                                }
                            }
                        }
                    }
                }
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                Log.info("Valida o resultado da consulta no grid");
            } else {
                Log.info("Valida o resultado da consulta no grid: registro não encontrado");
            }

        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível validar o resultado da consulta no grid --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkIconHide(String object, String data, String print) {
        try {
            assertTrue(
                !driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("md-svg-icon").contains(data));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o ícone está oculto");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o ícone '" + object + "' está oculto --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkIconVisible(String object, String data, String print) {
        try {
            assertTrue(driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("md-svg-icon").contains(data));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o ícone está visível");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o ícone '" + object + "' está visível --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkFilterOnlyMyItems(String object, String data, String print) {
        try {
            if (driver.findElements(By.xpath(OR.getProperty(object))).isEmpty()) {
                Log.info("Não há itens pendentes de aprovação para validação");
            } else {
                Common.wait(1000);
                final List<WebElement> itemsList1 = driver.findElements(By.xpath(OR.getProperty(object)));
                int numberItemsList1 = itemsList1.size();
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                driver.findElement(By.xpath(OR.getProperty("chbx_SomenteMeusItens"))).click();
                Common.wait(1000);
                final List<WebElement> itemsList2 = driver.findElements(By.xpath(OR.getProperty(object)));
                int numberItemsList2 = itemsList2.size();
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                assertTrue(numberItemsList1 > numberItemsList2);
                Common.wait(1000);
                driver.findElement(By.xpath(OR.getProperty("chbx_SomenteMeusItens"))).click();
                Common.wait(1000);
                final List<WebElement> itemsList3 = driver.findElements(By.xpath(OR.getProperty(object)));
                int numberItemsList3 = itemsList3.size();
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                assertTrue(numberItemsList2 < numberItemsList3);
                Log.info("Verifica se o checkbox (filtro) 'Somente meus itens' está funcionando");
            }
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar o checkbox (filtro) 'Somente meus itens' --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void validateFieldsClear(String object, String data, String print) {
        try {
            List<String> valoresCampos = Arrays.asList(OR.getProperty(object).split("\\s*;\\s*"));

            for (int i = 0; i < valoresCampos.size(); i++) {
                WebElement elementWebField = driver.findElement(By.xpath(OR.getProperty(valoresCampos.get(i))));
                String elementTypeField = elementWebField.getTagName();

                if (elementTypeField.contains("input") || elementTypeField.contains("textarea")) {
                    if (elementWebField.getAttribute("class").contains("datepicker")) {
                        assertTrue(
                            elementWebField.getAttribute("value").equals(DateTimeFunctions.getCurrentDateBrFormat()));
                    } 
                    else if (valoresCampos.get(i).equals("txtbx_CertificadoAPN")) {
                        assertTrue(
                            driver.findElement(By.xpath("//*[@id='textInput']")).getAttribute("class").contains("ng-empty"));
                    }                    
                    else {
                        assertTrue(elementWebField.getAttribute("value").isEmpty());
                    }
                }

                else if (elementTypeField.contains("span")) {
                    assertTrue(elementWebField.getText().equals(Constants.OptionDefaultMDSelect));
                }

                else if (elementTypeField.contains("checkbox")) {
                    if (OR.getProperty(valoresCampos.get(i)).contains("enabled")) {
                        assertTrue(elementWebField.getAttribute("aria-checked").equals("true"));
                    } else {
                        assertTrue(elementWebField.getAttribute("aria-checked").equals("false"));
                    }
                }

                else if (elementTypeField.contains("select")) {
                    assertTrue(elementWebField.getText().equals(Constants.OptionDefaultMDSelect) || elementWebField.getText().equals("Operador"));
                }
            }
            ScreenCapture.takePrintScreen(print);
            Log.info("Valida se o botão 'LIMPAR' está funcionando");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível validar se o botão 'LIMPAR' está funcionando --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }
    
    public static void cleanApprovalItems(String object, String data, String print) {
        try {            
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);           
            List<WebElement> itemsList = driver.findElements(By.xpath(OR.getProperty("lst_Aprovacoes")));
            int tamanhoLista = itemsList.size();
            for (int i = 0; i < tamanhoLista; i++){
                itemsList.get(0).click();
                clickWait("btn_ReprovarItem", "", "");
                click("btn_ConfirmarAprovReprov","","");
                Common.wait(1000);
                driver.navigate().refresh();
                Common.wait(4000);
                itemsList = driver.findElements(By.xpath(OR.getProperty("lst_Aprovacoes")));
            }
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);            
            Log.info("Limpa a lista de itens pendentes de aprovação.");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível limpar toda a lista de itens pendentes de aprovação --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }
}
