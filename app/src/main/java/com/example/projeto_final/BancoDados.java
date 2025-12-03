package com.example.projeto_final;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Componente.class}, version = 1)
public abstract class BancoDados extends RoomDatabase {
    public abstract ComponenteDAO ComponenteDAO();
}