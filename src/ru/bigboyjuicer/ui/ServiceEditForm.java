package ru.bigboyjuicer.ui;

import ru.bigboyjuicer.entity.ServiceEntity;
import ru.bigboyjuicer.manager.ServiceEntityManager;
import ru.bigboyjuicer.util.BaseForm;
import ru.bigboyjuicer.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Форма изменения и удаления записи
 */
public class ServiceEditForm extends BaseForm {
    private JTextField titleField;
    private JTextField costField;
    private JSpinner durationSpinner;
    private JTextField descField;
    private JTextField discountField;
    private JTextField imageField;
    private JButton backButton;
    private JButton addButton;
    private JPanel mainPanel;
    private JButton deleteButton;
    private JTextField idField;
    private ServiceEntity service;

    public ServiceEditForm(ServiceEntity service) {
        super(475, 375);
        this.service = service;


        setContentPane(mainPanel);

        initButtons();
        initFields();

        setVisible(true);
    }

    public void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new ServiceTableForm();
        });

        deleteButton.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Вы точно хотите удалить данную запись?", "Удаление записи", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                try {
                    ServiceEntityManager.delete(service.getId());
                    DialogUtil.showInfo(this, "Запись успешно удалена");
                    dispose();
                    new ServiceTableForm();
                } catch (SQLException ex) {
                    DialogUtil.showError(this, "Ошибка удаления данных: " + ex.getMessage());
                }
            }
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

            service.setTitle(title);
            service.setCost(cost);
            service.setDuration(duration);
            service.setDesc(desc);
            service.setDiscount(discount);
            service.setImagePath(image);

            try {
                ServiceEntityManager.update(service);
                DialogUtil.showInfo(this, "Запись успешно изменена");
                dispose();
                new ServiceTableForm();
            } catch (SQLException ex) {
                DialogUtil.showError(this, "Ошибка изменения данных: " + ex.getMessage());
            }


        });
    }

    public void initFields(){
        idField.setEditable(false);
        idField.setText(String.valueOf(service.getId()));
        titleField.setText(String.valueOf(service.getTitle()));
        costField.setText(String.valueOf(service.getCost()));
        durationSpinner.setValue(service.getDuration());
        descField.setText(service.getDesc());
        discountField.setText(String.valueOf(service.getDiscount()));
        imageField.setText(service.getImagePath());
    }
}
