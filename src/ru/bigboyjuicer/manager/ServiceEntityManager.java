package ru.bigboyjuicer.manager;

import ru.bigboyjuicer.App;
import ru.bigboyjuicer.entity.ServiceEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEntityManager {

    public static List<ServiceEntity> selectAll() throws SQLException {
        try (Connection c = App.getConnection()) {
            String sql = "SELECT * FROM service";

            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery(sql);

            List<ServiceEntity> list = new ArrayList<>();

            while (rs.next()) {
                list.add(
                        new ServiceEntity(
                                rs.getInt("ID"),
                                rs.getString("Title"),
                                rs.getDouble("Cost"),
                                rs.getInt("DurationInSeconds"),
                                rs.getString("Description"),
                                rs.getDouble("Discount"),
                                rs.getString("MainImagePath")
                        )
                );
            }

            return list;
        }
    }

    public static ServiceEntity selectById(int id) throws SQLException {
        try (Connection c = App.getConnection()) {
            String sql = "SELECT * FROM service WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ServiceEntity(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getDouble("Cost"),
                        rs.getInt("DurationInSeconds"),
                        rs.getString("Description"),
                        rs.getDouble("Discount"),
                        rs.getString("MainImagePath")
                );
            }

            throw new SQLException("Ошибка получения данных");
        }
    }

    public static void insert(ServiceEntity service) throws SQLException {
        try (Connection c = App.getConnection()) {
            String sql = "INSERT INTO service(Title, Cost, DurationInSeconds, Description, Discount, MainImagePath) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, service.getTitle());
            ps.setDouble(2, service.getCost());
            ps.setInt(3, service.getDuration());
            ps.setString(4, service.getDesc());
            ps.setDouble(5, service.getDiscount());
            ps.setString(6, service.getImagePath());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                service.setId(rs.getInt(1));
                return;
            }

            throw new SQLException("Entity wasn't added");
        }
    }

    public static void update(ServiceEntity service) throws SQLException {
        try (Connection c = App.getConnection()) {
            String sql = "UPDATE service SET Title=?, Cost=?, DurationInSeconds=?, Description=?, Discount=?, MainImagePath=? WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, service.getTitle());
            ps.setDouble(2, service.getCost());
            ps.setInt(3, service.getDuration());
            ps.setString(4, service.getDesc());
            ps.setDouble(5, service.getDiscount());
            ps.setString(6, service.getImagePath());
            ps.setInt(7, service.getId());

            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException {
        try(Connection c = App.getConnection()){
            String sql = "DELETE FROM service WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }

}
