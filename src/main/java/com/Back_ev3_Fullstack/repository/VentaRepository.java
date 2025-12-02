package com.Back_ev3_Fullstack.repository;

import com.Back_ev3_Fullstack.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    //ventas dia especifico
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
}