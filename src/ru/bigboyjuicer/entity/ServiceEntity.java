package ru.bigboyjuicer.entity;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

@Data
public class ServiceEntity {
    private int id;
    private String title;
    private double cost;
    private int duration;
    private String desc;
    private double discount;
    private String imagePath;
    private ImageIcon image;

    public ServiceEntity(int id, String title, double cost, int duration, String desc, double discount, String imagePath) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.duration = duration;
        this.desc = desc;
        this.discount = discount;
        this.imagePath = imagePath;

        try {
            this.image = new ImageIcon(ImageIO.read(ServiceEntity.class.getClassLoader().getResource(imagePath)).getScaledInstance(50, 50, Image.SCALE_DEFAULT)
            );
        } catch (Exception e) {
        }
    }

    public ServiceEntity(String title, double cost, int duration, String desc, double discount, String imagePath) {
        this(-1, title, cost, duration, desc, discount, imagePath);
    }
}
