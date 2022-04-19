package ru.bigboyjuicer.ui;

import ru.bigboyjuicer.App;
import ru.bigboyjuicer.entity.ServiceEntity;
import ru.bigboyjuicer.manager.ServiceEntityManager;
import ru.bigboyjuicer.util.BaseForm;
import ru.bigboyjuicer.util.CustomTableModel;
import ru.bigboyjuicer.util.DialogUtil;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Форма вывода записей из базы данных
 */
public class ServiceTableForm extends BaseForm {
    private CustomTableModel<ServiceEntity> model;
    private JTable table;
    private JPanel mainPanel;
    private JButton addButton;
    private JButton costSortButton;
    private JComboBox discountFilterComboBox;
    private JLabel rowCountLabel;
    private JButton clearFilterButton;
    private JButton helpButton;
    private JButton dealButton;

    private boolean costSort = false;

    public ServiceTableForm() {
        super(1200, 800);

        setContentPane(mainPanel);

        initTable();
        initButtons();
        initBoxes();

        setVisible(true);
    }

    public void initTable(){
        table.getTableHeader().setReorderingAllowed(false);

        try {
            model = new CustomTableModel<>(
                    ServiceEntity.class,
                    new String[] {"ID", "Название", "Цена", "Длительность", "Описание", "Скидка", "Путь к картинке", "Изображение"},
                    ServiceEntityManager.selectAll()
            );

            table.setModel(model);

            updateRowCountLabel(model.getRows().size(), model.getRows().size());


            table.setRowHeight(50);

            TableColumn column = table.getColumn("Путь к картинке");
            column.setMinWidth(0);
            column.setPreferredWidth(0);
            column.setMaxWidth(0);


            if(App.ADMIN_MODE){
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() == 2){
                            int row = table.rowAtPoint(e.getPoint());
                            if(row != -1){
                                dispose();
                                new ServiceEditForm(model.getRows().get(row));
                            }
                        }
                    }
                });
            }

            if(model.getRows().size() == 0){
                DialogUtil.showInfo(this, "Таблица с данными пуста");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initButtons(){

        if(App.ADMIN_MODE){
            addButton.addActionListener(e -> {
                dispose();
                new ServiceCreateForm();
            });
        } else {
            addButton.setEnabled(false);
        }

        costSortButton.addActionListener(e -> {
            Collections.sort(model.getRows(), new Comparator<ServiceEntity>() {
                @Override
                public int compare(ServiceEntity o1, ServiceEntity o2) {
                    if(costSort){
                        return Double.compare(o2.getCost(), o1.getCost());
                    } else {
                        return Double.compare(o1.getCost(), o2.getCost());
                    }
                }
            });

            costSort = !costSort;

            model.fireTableDataChanged();

            if(model.getRows().size() == 0){
                DialogUtil.showInfo(this, "Таблица с данными пуста");
            }
        });

        clearFilterButton.addActionListener(e -> {
            discountFilterComboBox.setSelectedIndex(0);
        });

        helpButton.addActionListener(e -> {
            DialogUtil.showInfo(this, "Изменение данных - двойной клик по полю\nУдаление данных находится в окне изменения данных");
        });

        dealButton.addActionListener(e -> {
            DialogUtil.showInfo(this, "Связаться с разработчиком можно по почте: aboba");
        });
    }

    public void initBoxes(){
        discountFilterComboBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                applyFilters();
            }
        });
    }

    public void applyFilters(){
        try {
            List<ServiceEntity> list = ServiceEntityManager.selectAll();

            int max = list.size();

            switch(discountFilterComboBox.getSelectedIndex()){
                case 1:
                    list.removeIf(s -> s.getDiscount() >= 5);
                    break;
                case 2:
                    list.removeIf(s -> s.getDiscount() < 5 || s.getDiscount() >= 15);
                    break;
                case 3:
                    list.removeIf(s -> s.getDiscount() < 15 || s.getDiscount() >= 30);
                    break;
                case 4:
                    list.removeIf(s -> s.getDiscount() < 30 || s.getDiscount() >= 70);
                    break;
                case 5:
                    list.removeIf(s -> s.getDiscount() < 70);
                    break;
            }

            model.setRows(list);
            model.fireTableDataChanged();
            costSort = false;

            updateRowCountLabel(list.size(), max);

            if(model.getRows().size() == 0){
                DialogUtil.showInfo(this, "Таблица с данными пуста");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateRowCountLabel(int actual, int max){
        rowCountLabel.setText("Отображено записей: " + actual + " / " + max);
    }
}
