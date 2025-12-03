package com.example.projeto_final;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "componentes")
public class Componente {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;      // Ex: RTX 4060
    public String tipo;      // Ex: GPU, CPU
    public double preco;     // Ex: 2000.00
    public int watts;        // Ex: 115

    public Componente(String nome, String tipo, double preco, int watts) {
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
        this.watts = watts;
    }
}