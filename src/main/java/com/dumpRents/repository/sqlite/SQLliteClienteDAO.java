package com.dumpRents.repository.sqlite;

import com.dumpRents.model.entities.Client;
import com.dumpRents.model.entities.valueObjects.*;
import com.dumpRents.persistence.dao.ClientDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLliteClienteDAO implements ClientDAO {
    @Override
    public Optional<Client> findByCpf(Cpf cpf) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Integer create(Client client) {
        String sql = "INSERT INTO cliente (name,cpf,phone1,phone2,email,street,district,number,city,cep) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getCpf().toString());
            stmt.setString(3, client.getPhone1().toString());
            stmt.setString(4, client.getPhone2().toString());
            for (Email email : client.getEmailList()) {
                stmt.setString(5, email.toString());
            }
            stmt.setString(6, client.getAddress().getStreet());
            stmt.setString(7, client.getAddress().getDistrict());
            stmt.setString(8, client.getAddress().getNumber());
            stmt.setString(9, client.getAddress().getCity());
            stmt.setString(10, client.getAddress().getCep().toString());
            stmt.execute();

            ResultSet resultSet = stmt.getGeneratedKeys();
            int GeneratedKey = resultSet.getInt(1);
            return GeneratedKey;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Optional<Client> findOne(Integer key) {


        return Optional.empty();
    }
private Client resultSetEntity(ResultSet rs) throws SQLException {
           List<Email> emails = new ArrayList<>();
           emails.add(new Email(rs.getString("email")));
           return new Client(rs.getString("name"),
                   new Address(rs.getString("street"),
                           rs.getString("district"),
                           rs.getString("number"),
                           rs.getString("city"),new Cep(rs.getString("cep"))),

                   new Cpf(rs.getString("cpf")),
                   new Phone(rs.getString("phone1")),
                   new Phone(rs.getString("phone2")),
                   emails,
                   rs.getInt("ID"));


    }
    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public boolean update(Client type) {
        return false;
    }

    @Override
    public boolean deleteByKey(Integer key) {
        return false;
    }

    @Override
    public boolean delete(Client type) {
        return false;
    }
}
