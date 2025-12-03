package com.example.projeto_final;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ComponenteDAO {
    @Insert
    void inserir(Componente componente);

    @Delete
    void deletar(Componente componente);

    @Query("SELECT * FROM componentes")
    List<Componente> listarTodos();
}