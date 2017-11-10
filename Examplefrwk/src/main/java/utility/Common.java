package utility;

import static executionEngine.DriverScript.OR;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Common {

    public static WebElement getXPathRowGrid(String valorCampo, int indice, WebDriver driver) {
        String xpathColunaGrid = OR.getProperty(valorCampo);
        String newXPathInicio = xpathColunaGrid.substring(0, xpathColunaGrid.lastIndexOf("]/td[") - 1);
        String newXPathFim = xpathColunaGrid.substring(xpathColunaGrid.lastIndexOf("table/tbody/tr[1") + 16,
            xpathColunaGrid.length());
        String newXPathColunaGrid = newXPathInicio + ++indice + newXPathFim;
        return driver.findElement(By.xpath(newXPathColunaGrid));
    }

    public static void wait(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Log.error("Not able to Wait --- " + e.getMessage());
        }
    }

    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().contains(fileName)) {
                return flag = true;
            }
        }
        return flag;
    }

    public static boolean isFileDeleted(String downloadPath, String fileName) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();
        File file = new File(downloadPath + "\\" + fileName);
        if (file.exists()) {
            file.delete();
        }
        for (int i = 0; i < dir_contents.length; i++) {
            if (!dir_contents[i].getName().contains(fileName)) {
                return flag = true;
            }
        }
        return flag;
    }

    public static String getElementXPath(WebDriver driver, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        String xpathOriginal = (String) executor.executeScript(
            "gPt=function(c){if(c.id!==''){return'//*[@id=\"'+c.id+'\"]'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]);",
            element);
        return xpathOriginal.replace("\"", "'").replace("[1]", "");
    }
}
