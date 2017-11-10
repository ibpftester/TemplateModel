package utility;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import config.Constants;
import executionEngine.DriverScript;

public class ScreenCapture {

    static String namePath = Constants.NamePath;
    static String idPath = (DateTimeFunctions.getCurrentDate() + "_" + DateTimeFunctions.getTimeScreenShot())
        .substring(0, 19);

    public static void takePrintScreen(String flag) {
        if(flag.equals("True")){
            try {
                String nameSubPath = Constants.NamePath + "\\" + idPath + "\\" + DriverScript.sTestCaseID;

                createPaths();

                createSubPath(nameSubPath);

                ImageIO.write(generateImage(), "jpg", new File(Constants.PathScreenshots + "\\" + nameSubPath + "\\"
                    + DateTimeFunctions.getTimeScreenShot() + ".jpg"));
            } catch (IOException e) {
                Log.error("Falha ao registrar o print de sucesso do caso de teste --- " + e.getMessage());
            }            
        }
    }

    public static void takeErrorPrintScreen() {
        try {
            String nameSubPath = Constants.NamePath + "\\" + idPath + "\\Erros\\" + DriverScript.sTestCaseID;

            createPaths();

            if (!new File(Constants.PathScreenshots + "\\" + namePath + "\\" + idPath + "\\Erros").exists()) {
                new File(Constants.PathScreenshots + "\\" + namePath + "\\" + idPath + "\\Erros").mkdir();
            }

            createSubPath(nameSubPath);

            ImageIO.write(generateImage(), "jpg", new File(Constants.PathScreenshots + "\\" + nameSubPath + "\\"
                + "Error_" + DateTimeFunctions.getTimeScreenShot() + ".jpg"));
        } catch (IOException e) {
            Log.error("Falha ao registrar o print de erro do caso de teste --- " + e.getMessage());
        }
    }

    public static void createPaths() {
        if (!new File(Constants.PathScreenshots).exists()) {
            new File(Constants.PathScreenshots).mkdir();
        }

        if (!new File(Constants.PathScreenshots + "\\" + namePath).exists()) {
            new File(Constants.PathScreenshots + "\\" + namePath).mkdir();
        }

        if (!new File(Constants.PathScreenshots + "\\" + namePath + "\\" + idPath).exists()) {
            new File(Constants.PathScreenshots + "\\" + namePath + "\\" + idPath).mkdir();
        }
    }

    public static void createSubPath(String nameSubPath) {
        if (!new File(Constants.PathScreenshots + "\\" + nameSubPath).exists()) {
            new File(Constants.PathScreenshots + "\\" + nameSubPath).mkdir();
        }
    }

    public static BufferedImage generateImage() {
        BufferedImage screencapture = null;
        try {
            screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (HeadlessException | AWTException e) {
            Log.error("Falha ao gerar a imagem para o print do caso de teste --- " + e.getMessage());
        }
        return screencapture;
    }
}
