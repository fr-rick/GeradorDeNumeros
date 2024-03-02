package com.example.geradordenumeros

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Random


class MainActivity : AppCompatActivity() {

    // criei uma variavel chamada prefs fora do escopo da função onCreate do tipo sharedPreferences
    // para poder usar em qualquer lugar do código
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Peguei os componentes da interface gráfica e joguei em variaveis para poder manipulalos
        val editNumber: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)

        // Bloco de código para persistir os dados, ou seja salvar os ultimos números gerados
        // como se estivesse criando um banco de dados
        prefs = getSharedPreferences("db", Context.MODE_PRIVATE)
        // jogo pra variavel result aquilo que foi salvo na chave "result" do cache, retorna null se
        // não tiver nada. Poderia retornar um valor padrão se não retornasse nada, só escrever
        // no lugar do null
        val result = prefs.getString("result", null)

        // ao inves de fazer if (x != null) pode fazer dessa forma com o let
        // com isso identifico se a variavel result é diferente de null e se for executo
        // o bloco de código
        result?.let {
            txtResult.text = it
        }

        btnGenerate.setOnClickListener {
            val number = editNumber.text.toString().toIntOrNull()
            numberGenerator(number, txtResult)

            // esse bloco de código serve para fechar o teclado qd eu clicar no botão
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            editNumber.setText("")
        }

    }

    private fun numberGenerator(number: Int?, txtResult: TextView) {

        // validando quando o campo é vazio ou não está entre 6 e 15
        if (number == null || number !in 6..15) {
            Toast.makeText(this, "Informe um número entre 6 e 15", Toast.LENGTH_LONG).show()
            return
        }

        // Continuando a lógica se deu sucesso
        val numbers = mutableSetOf<Int>()
        val random = Random()

        while (true) {
            val numberRandom = random.nextInt(59)
            // somei +1 para que nenhum número seja 0
            numbers.add(numberRandom + 1)

            if (numbers.size == number) {
                break
            }
        }

        // Utilizei o método sorted() para ordenar o array de números
        val orderedNumbers = numbers.sorted()
        // utilizei o método joinToString() para transformar meu array numa string sendo separada
        // pelo que eu passar no parametro do método
        txtResult.text = orderedNumbers.joinToString(" ")


        // cria o editor para que possa editar o cache do app
        // dou um editor.putString para guardar em cache na chave "result" a String
        // dou um editor.apply para commitar essas informações enviando pro cache

//        val editor = prefs.edit()
//        editor.putString("result", txtResult.text.toString())
//        editor.apply()

        // Está é outra forma de fazer a mesma coisa só que com o código mais limpo
        prefs.edit().apply {
            putString("result", txtResult.text.toString())
            apply()
        }

    }
}