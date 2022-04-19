package ru.bigboyjuicer.util;

import ru.bigboyjuicer.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BaseForm extends JFrame {

    public static String APP_TITLE = "Школа языков Леарн";

    public static Image APP_IMAGE = null;

    static {
        try {
            APP_IMAGE = ImageIO.read(BaseForm.class.getClassLoader().getResource("school_logo.png"));
        } catch (IOException e) {
        }
    }


    public BaseForm(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(width, height));

        setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2
        );


        setTitle(APP_TITLE + (App.ADMIN_MODE ? " [режим администратора]" : ""));
        if (APP_IMAGE != null) {
            setIconImage(APP_IMAGE);
        } else {
            DialogUtil.showError(this, "Не удалось загрузить иконку приложения");
        }
    }
}
