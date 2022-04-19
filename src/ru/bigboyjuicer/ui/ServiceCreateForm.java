package ru.bigboyjuicer.ui;

import ru.bigboyjuicer.entity.ServiceEntity;
import ru.bigboyjuicer.manager.ServiceEntityManager;
import ru.bigboyjuicer.util.BaseForm;
import ru.bigboyjuicer.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Форма добавления записи в таблицу
 */
public class ServiceCreateForm extends BaseForm {
    private JTextField titleField;
    private JTextField costField;
    private JSpinner durationSpinner;
    private JTextField descField;
    private JTextField discountField;
    private JTextField imageField;
    private JButton backButton;
    private JButton addButton;
    private JPanel mainPanel;

    public ServiceCreateForm() {
        super(475, 375);

        setContentPane(mainPanel);

        initButtons();

        setVisible(true);
    }

    public void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new ServiceTableForm();
        });

        addButton.addActionListener(e -> {

            String title = titleField.getText();
            if (title.length() > 100 || title.isEmpty()) {
                DialogUtil.showError(this, "Название не введено или слишком длинное");
                return;
            }

            double cost = -1;
            try {
                cost = Double.parseDouble(costField.getText());
            } catch (Exception ex) {
                DialogUtil.showError(this, "Стоимость введена не верно");
                return;
            }

            if (cost < 0) {
                DialogUtil.showError(this, "Стоимость введена не верно");
                return;
            }

            int duration = (int) durationSpinner.getValue();
            if (duration <= 0) {
                DialogUtil.showError(this, "Длительность введена не верно");
                return;
            }

            String desc = descField.getText();

            double discount = -1;
            try {
                discount = Double.parseDouble(discountField.getText());
            } catch (Exception ex) {
                DialogUtil.showError(this, "Скидка введена не верно");
                return;
            }

            if (discount < 0 || discount > 100) {
                DialogUtil.showError(this, "Скидка введена не верно");
                return;
            }

            String image = imageField.getText();
            if(image.length() > 1000){
                DialogUtil.showError(this, "Путь до картинки введен не верно");
                return;
            }

            ServiceEntity service = new ServiceEntity(title, cost, duration, desc, discount, image);

            try {
                ServiceEntityManager.insert(service);
                DialogUtil.showInfo(this, "Запись успешно добавлена");
                dispose();
                new ServiceTableForm();
            } catch (SQLException ex) {
                DialogUtil.showError(this, "Ошибка добавления данных: " + ex.getMessage());
            }


        });
    }
}
