package com.example.projeto_final; // <--- Verifique se este é o nome do seu pacote!

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar; // Importação adicionada
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Para pegar cores corretamente
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Declaração dos componentes da interface
    private EditText editNome, editWatts, editPreco, editFonte;
    private TextView txtLista, txtTotalPreco, txtTotalWatts, txtStatus;
    private ProgressBar progressBar; // Declaração da barra

    // Banco de dados e Threads
    private BancoDados db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar Banco de Dados
        db = Room.databaseBuilder(getApplicationContext(),
                BancoDados.class, "hardware-db").build();

        // 2. Vincular (Bind) os IDs do XML com as variáveis Java
        editFonte = findViewById(R.id.editFonte);
        editNome = findViewById(R.id.editNome);
        editWatts = findViewById(R.id.editWatts);
        editPreco = findViewById(R.id.editPreco);

        txtLista = findViewById(R.id.txtListaPecas);
        txtTotalPreco = findViewById(R.id.txtTotalPreco);
        txtTotalWatts = findViewById(R.id.txtTotalWatts);
        txtStatus = findViewById(R.id.txtStatus);

        progressBar = findViewById(R.id.progressWatts); // Vínculo da barra de progresso

        Button btnAdd = findViewById(R.id.btnAdicionar);
        Button btnLimpar = findViewById(R.id.btnLimpar);

        // 3. Carregar dados iniciais ao abrir o app
        atualizarTela();

        // 4. Ação do Botão Adicionar
        btnAdd.setOnClickListener(v -> {
            String nome = editNome.getText().toString();
            String wattsStr = editWatts.getText().toString();
            String precoStr = editPreco.getText().toString();

            // Validação simples
            if (nome.isEmpty() || wattsStr.isEmpty() || precoStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Converter texto para números
            int watts = Integer.parseInt(wattsStr);
            double preco = Double.parseDouble(precoStr);

            // Criar objeto
            Componente novoComponente = new Componente(nome, "Geral", preco, watts);

            // Salvar no Banco (Background)
            executor.execute(() -> {
                db.ComponenteDAO().inserir(novoComponente);

                // Voltar para UI para atualizar e limpar campos
                handler.post(() -> {
                    atualizarTela();
                    editNome.setText("");
                    editWatts.setText("");
                    editPreco.setText("");
                    Toast.makeText(this, "Peça adicionada!", Toast.LENGTH_SHORT).show();
                });
            });
        });

        // 5. Botão Limpar Banco
        btnLimpar.setOnClickListener(v -> {
            executor.execute(() -> {
                db.clearAllTables();
                handler.post(() -> {
                    atualizarTela();
                    Toast.makeText(this, "Setup limpo!", Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    // Método principal que calcula e atualiza a UI
    private void atualizarTela() {
        executor.execute(() -> {
            // Pegar lista do banco (Operação pesada)
            List<Componente> lista = db.ComponenteDAO().listarTodos();

            // Variáveis para cálculo
            StringBuilder sb = new StringBuilder();
            double totalPreco = 0;
            int totalWatts = 0;

            for (Componente c : lista) {
                sb.append("• ").append(c.nome)
                        .append(" | ").append(c.watts).append("W")
                        .append(" | R$ ").append(c.preco).append("\n\n");

                totalPreco += c.preco;
                totalWatts += c.watts;
            }

            // Preparar variáveis finais para usar na thread principal
            String textoLista = sb.toString();
            double finalPreco = totalPreco;
            int finalWatts = totalWatts;

            // --- ATUALIZAR INTERFACE (Main Thread) ---
            handler.post(() -> {
                txtLista.setText(textoLista.isEmpty() ? "Nenhuma peça instalada." : textoLista);
                txtTotalPreco.setText(String.format("Total: R$ %.2f", finalPreco));

                // Ler a fonte definida pelo usuário
                String fonteStr = editFonte.getText().toString();
                // Se estiver vazio, consideramos 1 para evitar divisão por zero
                int limiteFonte = fonteStr.isEmpty() ? 1 : Integer.parseInt(fonteStr);

                txtTotalWatts.setText(finalWatts + "W / " + limiteFonte + "W");

                // Atualizar a Barra de Progresso (0 a 100)
                int porcentagem = (finalWatts * 100) / limiteFonte;
                progressBar.setProgress(porcentagem);

                // --- LÓGICA DE CORES E STATUS (Material Design Colors) ---
                if (finalWatts > limiteFonte) {
                    // Cenario: SOBRECARGA (Vermelho)
                    txtStatus.setText("PERIGO: FONTE SOBRECARREGADA!");
                    txtStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_red));
                    txtStatus.setTextColor(ContextCompat.getColor(this, R.color.white));
                }
                else {
                    double margemSeguranca = limiteFonte * 0.85; // 85% de uso

                    if (finalWatts > margemSeguranca) {
                        // Cenario: ALERTA (Laranja)
                        txtStatus.setText("ATENÇÃO: Capacidade próxima do limite");
                        txtStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_orange));
                        txtStatus.setTextColor(ContextCompat.getColor(this, R.color.white));
                    } else {
                        // Cenario: OK (Verde)
                        txtStatus.setText("SISTEMA ESTÁVEL");
                        txtStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_green));
                        txtStatus.setTextColor(ContextCompat.getColor(this, R.color.white));
                    }
                }
            });
        });
    }
}