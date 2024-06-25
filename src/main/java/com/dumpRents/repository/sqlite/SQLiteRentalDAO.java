package com.dumpRents.repository.sqlite;

import com.dumpRents.model.entities.Client;
import com.dumpRents.model.entities.Rental;
import com.dumpRents.model.entities.RentalStatus;
import com.dumpRents.model.entities.RubbleDumpster;
import com.dumpRents.model.entities.valueObjects.Address;
import com.dumpRents.model.entities.valueObjects.Cep;
import com.dumpRents.persistence.dao.RentalDAO;

import javax.swing.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  SQLiteRentalDAO implements RentalDAO {
    @Override
    public List<Rental> findRentalByPeriod(LocalDate initialDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<Rental> findRentalByClient(Client client) {
        return List.of();
    }

    @Override
    public List<Rental> findRentalByRubbleDumpster(RubbleDumpster rubbleDumpster) {
        return List.of();
    }

    @Override
    public List<Rental> findRentalByStatus(RentalStatus status) {
        return List.of();
    }

    @Override
    public Integer create(Rental rental) {

        String sql = "INSERT INTO Rental(initialDate,rentalStatus,street,district,number,city,cep,id_Client,id_RubbleDumpster) VALUES (?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            stmt.setDate(1, Date.valueOf(rental.getInitialDate()));
            stmt.setString(2, rental.getRentalStatus().toString());
            stmt.setString(3, rental.getAddress().getStreet());
            stmt.setString(4, rental.getAddress().getDistrict());
            stmt.setString(5, rental.getAddress().getNumber());
            stmt.setString(6, rental.getAddress().getCity());
            stmt.setString(7, rental.getAddress().getCep().toString());
            stmt.setInt(8,rental.getClient().getId());
            stmt.setInt(9,rental.getRubbleDumpster().getId());
            stmt.execute();

            ResultSet rs = stmt.getGeneratedKeys();
            int generatedKey = rs.getInt(1);
            return generatedKey;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Rental resultSetToRental(ResultSet rs) throws SQLException{
        SQLiteRubbleDumbsterDAO dumbsterDAO = new SQLiteRubbleDumbsterDAO();
        SQLiteClientDAO clientDAO = new SQLiteClientDAO();

        Client client = clientDAO.findOne(rs.getInt("id_Client")).get();
        RubbleDumpster rubbleDumpster = dumbsterDAO.findById(rs.getInt("id_RubbleDumpster")).get();

             return new Rental(
                     LocalDate.parse(rs.getString("initialDate")),
                     LocalDate.parse(rs.getString("withdrawalRequestDate")),
                     LocalDate.parse(rs.getString("withdrawalDate")),
                     LocalDate.parse(rs.getString("endDate")),
                     (double) rs.getInt("finalAmount"),
                     RentalStatus.toEnum(rs.getString("rentalStatus")),
                     client
                     ,
                     new Address(rs.getString("street"),
                             rs.getString("district"),
                             rs.getString("number"),
                             rs.getString("city"),
                             new Cep(rs.getString("cep"))),
                     rubbleDumpster);
    }

    @Override
    public Optional<Rental> findOne(Integer key) {
        String sql = "SELECT * from Rental where id = ?";
        Rental rental= null;
        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            stmt.setInt(1,key);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                rental = resultSetToRental(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(rental);
    }

    @Override
    public List<Rental> findAll() {
        String sql = "SELECT * from Rental";
        List<Rental> rentals= new ArrayList<>();
        try(PreparedStatement stmt = ConnectionFactory.createPreparedStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Rental rental = resultSetToRental(rs);
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    @Override
    public boolean update(Rental type) {
        return false;
    }

    @Override
    public boolean deleteByKey(Integer key) {
        return false;
    }

    @Override
    public boolean delete(Rental type) {
        return false;
    }
}