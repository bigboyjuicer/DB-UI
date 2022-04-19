package ru.bigboyjuicer;

import ru.bigboyjuicer.ui.ServiceTableForm;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Главный класс
 */
public class App {
    public static boolean ADMIN_MODE = false;

    /**
     * Главный метод, точка входа в программу
     * @param args
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ADMIN_MODE = "0000".equalsIgnoreCase(JOptionPane.showInputDialog(null, "Введите пароль администратора", "Режим администратора", JOptionPane.QUESTION_MESSAGE));

        new ServiceTableForm();

    }

    /**
     * Метод для получения соединения с базой данных
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/demoexam", "root", "1234");
    }
}
