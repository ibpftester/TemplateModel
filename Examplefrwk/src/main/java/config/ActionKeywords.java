package config;

import static executionEngine.DriverScript.OR;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import executionEngine.DriverScript;
import utility.Common;
import utility.DateTimeFunctions;
import utility.Log;
import utility.ScreenCapture;

public class ActionKeywords {

    public static WebDriver driver;

    public static void openBrowser(String object, String data, String print) {
        try {
            if (data.equals("Mozilla")) {
                System.setProperty("webdriver.gecko.driver", Constants.Path_Drivers + "geckodriver.exe");
                DesiredCapabilities dc = DesiredCapabilities.firefox();
                dc.setCapability("marionette", true);
                driver = new FirefoxDriver(dc);
            } else if (data.equals("IE")) {
                System.setProperty("webdriver.ie.driver", Constants.Path_Drivers + "IEDriverServer.exe");
                driver = new InternetExplorerDriver();
            } else if (data.equals("Chrome")) {
                System.setProperty("webdriver.chrome.driver", Constants.Path_Drivers + "chromedriver.exe");
                driver = new ChromeDriver();
            }
            Log.info("Abre o browser " + data);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.info("Não foi possível abrir o browser --- " + e);
            DriverScript.bResult = false;
        }
    }

    public static void navigate(String object, String data, String print) {
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get(Constants.URL);
            Common.wait(Constants.Tempo_Load_URL * 1000);
            Log.info("Acessa a URL " + Constants.URL);
            ScreenCapture.takePrintScreen(print);
        } catch (Exception e) {
            Log.info("Não foi possível acessar a URL " + Constants.URL + " --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void click(String object, String data, String print) {
        try {
            driver.findElement(By.xpath(OR.getProperty(object))).click();
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Clica no elemento " + object);
        } catch (Exception e) {
            Log.error("Não foi possível clicar no elemento " + object + " --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void clickWait(String object, String data, String print) {
        try {
            driver.findElement(By.xpath(OR.getProperty(object))).click();
            Common.wait(5000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Clica no elemento " + object + " e aguarda 5 segundos");
        } catch (Exception e) {
            Log.error("Não foi possível clicar no elemento " + object + " e aguardar 5 segundos --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void input(String object, String data, String print) {
        try {
            driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Informa o texto " + data + " no campo " + object);
        } catch (Exception e) {
            Log.error("Não foi possível preencher o campo --- " + object + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void inputID(String object, String data, String print) {
        try {
            String id = DateTimeFunctions.getCurrentDate().substring(5, 10).replace(".", "")
                + DateTimeFunctions.getTimeScreenShot().substring(0, 5).replace(".", "");
            driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data + id);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Informa o texto " + data + id + " no campo " + object);
        } catch (Exception e) {
            Log.error("Não foi possível preencher o campo --- " + object + " com ID " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void uploadFile(String object, String data, String print) {
        try {
            WebElement input = driver.findElement(By.xpath(OR.getProperty(object)));
            Common.wait(1000);
            input.sendKeys(data);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Seleciona (upload) o arquivo " + data);
        } catch (Exception e) {
            Log.error("Não foi possível selecionar e fazer o upload do arquivo " + data + " --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void waitFor(String object, String data, String print) throws Exception {
        try {
            Thread.sleep(Constants.Tempo_Espera * 1000);
            Log.info("Aguarda por " + Constants.Tempo_Espera + " segundos");
        } catch (Exception e) {
            Log.error("Não foi possível aguardar --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void closeBrowser(String object, String data, String print) {
        try {
            driver.quit();
            Log.info("Fecha o browser");
        } catch (Exception e) {
            Log.error("Não foi possível fechar o browser --- " + e);
            DriverScript.bResult = false;
        }
    }

    public static void validateMessage(String object, String data, String print) {
        try {
            assertEquals(driver.findElement(By.xpath(OR.getProperty(object))).getText(), data);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Valida o texto '" + data + "'");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível validar o texto '" + data + "'" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementHide(String object, String data, String print) {
        try {
            assertTrue(driver.findElement(By.xpath(OR.getProperty(object))).isDisplayed() == false);
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento está oculto");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' está oculto --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementVisible(String object, String data, String print) {
        try {
            assertTrue(driver.findElement(By.xpath(OR.getProperty(object))).isDisplayed() == true);
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento está visível");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' está visível --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementDisabled(String object, String data, String print) {
        try {
            String statusField = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("disabled");
            assertTrue(statusField.equals("true") || statusField.equals("disabled"));
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento está desabilitado");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' está desabilitado --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementEnabled(String object, String data, String print) {
        try {
            assertTrue(driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("disabled") == (null));
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento está habilitado");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' está habilitado --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementNotExist(String object, String data, String print) {
        try {
            assertTrue(driver.findElements(By.xpath(OR.getProperty(object))).isEmpty());
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento existe");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' existe --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkElementExists(String object, String data, String print) {
        try {
            assertTrue(!driver.findElements(By.xpath(OR.getProperty(object))).isEmpty());
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se o elemento não existe");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar se o elemento '" + object + "' não existe --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void clearField(String object, String data, String print) {
        try {
            driver.findElement(By.xpath(OR.getProperty(object))).clear();
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Limpa o campo " + object);
        } catch (Exception e) {
            Log.error("Não foi possível limpar o campo '" + object + "' --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void selectItemCombo(String object, String data, String print) {
        try {
            Select itemSelected = new Select(driver.findElement(By.xpath(OR.getProperty(object))));
            itemSelected.selectByVisibleText(data);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Seleciona o item '" + data + "' no combo " + object);
        } catch (Exception e) {
            Log.error("Não foi possível selecionar o item '" + data + "' no combo --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void selectItemComboByIndex(String object, String data, String print) {
        try {
            Select itemSelected = new Select(driver.findElement(By.xpath(OR.getProperty(object))));
            itemSelected.selectByIndex(Integer.parseInt(data));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Seleciona o '" + data + "' item do combo " + object);
        } catch (Exception e) {
            Log.error("Não foi possível selecionar o '" + data + "' item do combo --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void selectItemMdSelect(String object, String data, String print) {
        try {
            driver.findElement(By.xpath(OR.getProperty(object))).click();
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            final List<WebElement> optionsToSelect = driver
                .findElements(By.xpath("//md-select-menu/md-content/md-option"));

            for (final WebElement option : optionsToSelect) {
                if (option.getText().equals(data)) {
                    Common.wait(1000);
                    option.click();
                    Common.wait(1000);
                    ScreenCapture.takePrintScreen(print);
                    break;
                }
            }
            Log.info("Seleciona o item '" + data + "' no combo");
        } catch (Exception e) {
            Log.error("Não foi possível selecionar o item '" + data + "' no combo --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkAllListMdCheckboxStatus(String object, String data, String print) {
        try {
            final List<WebElement> checkboxList = driver.findElements(By.xpath(OR.getProperty(object)));
            for (final WebElement checkbox : checkboxList) {
                assertTrue(checkbox.getAttribute("aria-checked").toString().equals(data));
            }
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Verifica se todos os checkbox listados estão com status '" + data + "'");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar o status dos checkbox listados --- " + e);
            DriverScript.bResult = false;
            moveScrollAtElement(object, null, "False");
            Common.wait(1000);
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void clickElementInListByIndex(String object, String data, String print) {
        try {
            final List<WebElement> itemsList = driver.findElements(By.xpath(OR.getProperty(object)));
            int index = Integer.parseInt(data);
            if (index < 0 || index > itemsList.size()) {
                Log.error("Parâmetro '" + data + "' inválido para a função");
            } else {
                itemsList.get(index - 1).click();
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                Log.info("Clica no item " + data + " da lista");
            }
        } catch (Exception e) {
            Log.error("Não foi possível clicar no item '" + data + "' da lista --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void checkNumberElementsInList(String object, String data, String print) {
        try {
            final List<WebElement> itemsList = driver.findElements(By.xpath(OR.getProperty(object)));
            int number = Integer.parseInt(data);
            if (number < 0 || number > itemsList.size()) {
                Log.error("Parâmetro '" + data + "' inválido para a função");
            } else {
                assertTrue(itemsList.size() == number);
                Common.wait(1000);
                ScreenCapture.takePrintScreen(print);
                Log.info("Verifica se a lista tem " + data + " elemento(s)");
            }
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível verificar o número de elementos da lista --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void moveScrollHorizontallyToEnd(String object, String data, String print) {
        try {
            ((JavascriptExecutor) driver)
                .executeScript("document.querySelector('table th:last-child').scrollIntoView();");
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll horizontalmente");
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll horizontalmente '" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void moveScrollVerticallyDown(String object, String data, String print) {
        try {
            ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,250)", "");
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll verticalmente");
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll verticalmente '" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void moveScrollVerticallyUp(String object, String data, String print) {
        try {
            ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,-250)", "");
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll verticalmente");
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll verticalmente '" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }
    
    public static void moveScrollAtElement(String object, String data, String print) {
        try {
            WebElement element = driver.findElement(By.xpath(OR.getProperty(object)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll ao elemento " + object);
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll ao elemento '" + object + "' --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void validateFileDownload(String object, String data, String print) {
        try {
            assertTrue(Common.isFileDownloaded(object, data));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Valida o download do arquivo '" + data + "'");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível validar o download do arquivo '" + data + " --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void deleteFileDownloaded(String object, String data, String print) {
        try {
            assertTrue(Common.isFileDeleted(object, data));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Deleta o arquivo baixado '" + data + "'");
        } catch (Exception | AssertionError e) {
            Log.error("Não foi possível deletar o arquivo baixado '" + data + " --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void zoomLevelDecrease(String object, String data, String print) {
        try {
            WebElement html = driver.findElement(By.tagName("html"));
            html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
        } catch (Exception e) {
            Log.error("Não foi possível diminuir o zoom '" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void zoomLevelIncrease(String object, String data, String print) {
        try {
            WebElement html = driver.findElement(By.tagName("html"));
            html.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
        } catch (Exception e) {
            Log.error("Não foi possível aumentar o zoom '" + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void moveScrollMdListDown(String object, String data, String print) {
        try {
            final List<WebElement> itemsList1 = driver.findElements(By.xpath(OR.getProperty(object)));
            int numberItemsList1 = itemsList1.size();
            WebElement element = driver.findElement(By.xpath(OR.getProperty(object)+ "[" + numberItemsList1 + "]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll ao elemento " + object);
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll ao elemento '" + object + "' --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }

    public static void moveScrollMdListUp(String object, String data, String print) {
        try {
            WebElement element = driver.findElement(By.xpath(OR.getProperty(object)+ "[" + 1 + "]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            Common.wait(1000);
            ScreenCapture.takePrintScreen(print);
            Log.info("Move o scroll ao elemento " + object);
        } catch (Exception e) {
            Log.error("Não foi possível mover o scroll ao elemento '" + object + "' --- " + e);
            DriverScript.bResult = false;
            ScreenCapture.takeErrorPrintScreen();
        }
    }
}
